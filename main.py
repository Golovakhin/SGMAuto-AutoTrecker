from fastapi import FastAPI
from pydantic import BaseModel
from typing import List, Optional, Dict
from aiogram import Bot, Dispatcher, F
from aiogram.types import Message, ReplyKeyboardMarkup, KeyboardButton
from datetime import datetime
import asyncio
import uvicorn

BOT_TOKEN = "8564706654:AAELxRqQlOOVQwcjs_dSHwj8a2LDw6LOFL4"

class UserModel(BaseModel):
    id: Optional[int]
    email: str
    password: str
    userName: str
    phone: str
    tgID: str

class CarModel(BaseModel):
    id: Optional[int]
    mark: str
    model: str
    generation: str
    startYear: int
    endYear: int
    imageURl: Optional[str] = None

class UserCarModel(BaseModel):
    id: Optional[int]
    userId: int
    carId: int
    gosNomer: str
    vin: str
    year: int

class ExpenseModel(BaseModel):
    id: Optional[int]
    userId: int
    userCarId: int
    expenseType: str
    amount: float
    date: str
    mileage: int
    description: str
    fuelVolume: Optional[float] = None
    fuelPricePerLiter: Optional[float] = None

class FullDump(BaseModel):
    users: List[UserModel]
    cars: List[CarModel]
    user_cars: List[UserCarModel]
    expenses: List[ExpenseModel]

SERVER_USERS: List[UserModel] = []
SERVER_CARS: List[CarModel] = []
SERVER_USER_CARS: List[UserCarModel] = []
SERVER_EXPENSES: List[ExpenseModel] = []

USER_SESSIONS: Dict[int, Dict] = {}

EXPENSE_TYPES = [
    "Топливо",
    "Ремонт",
    "Парковки и дороги",
    "Штрафы и налоги",
    "Мойка",
    "Страховка",
    "Прочее",
]

def main_keyboard() -> ReplyKeyboardMarkup:
    rows = [[KeyboardButton(text=t)] for t in EXPENSE_TYPES]
    return ReplyKeyboardMarkup(keyboard=rows, resize_keyboard=True)

app = FastAPI()

@app.post("/sync/upload")
async def upload_db(dump: FullDump):

    global SERVER_USERS, SERVER_CARS, SERVER_USER_CARS, SERVER_EXPENSES


    SERVER_USERS = dump.users
    SERVER_CARS = dump.cars
    SERVER_USER_CARS = dump.user_cars


    def expense_key(e: ExpenseModel):
        return (e.userId, e.userCarId, e.expenseType, e.amount, e.date, e.description)

    merged: Dict[tuple, ExpenseModel] = {}
    for e in SERVER_EXPENSES:
        merged[expense_key(e)] = e
    for e in dump.expenses:
        merged[expense_key(e)] = e

    SERVER_EXPENSES = list(merged.values())

    print(
        f"[SYNC] upload_db: users={len(SERVER_USERS)}, cars={len(SERVER_CARS)}, "
        f"user_cars={len(SERVER_USER_CARS)}, expenses={len(SERVER_EXPENSES)}"
    )
    return {"status": "ok"}

@app.get("/sync/download", response_model=FullDump)
async def download_db():

    print(
        f"[SYNC] download_db: users={len(SERVER_USERS)}, cars={len(SERVER_CARS)}, "
        f"user_cars={len(SERVER_USER_CARS)}, expenses={len(SERVER_EXPENSES)}"
    )
    return FullDump(
        users=SERVER_USERS,
        cars=SERVER_CARS,
        user_cars=SERVER_USER_CARS,
        expenses=SERVER_EXPENSES,
    )

@app.get("/debug/users", response_model=List[UserModel])
async def debug_users():

    return SERVER_USERS

bot = Bot(BOT_TOKEN)
dp = Dispatcher()

@dp.message(F.text == "/start")
async def cmd_start(message: Message):
    USER_SESSIONS.pop(message.chat.id, None)
    await message.answer(
        "Бот запущен.\n"
        "Выберите категорию расхода с помощью кнопок ниже.\n"
        "Важно: в приложении в поле tgID должен быть ваш @username, "
        "чтобы бот нашёл ваш профиль.",
        reply_markup=main_keyboard(),
    )

def find_or_create_expense_id() -> int:
    if not SERVER_EXPENSES:
        return 1
    return max(e.id or 0 for e in SERVER_EXPENSES) + 1

def get_current_iso_datetime() -> str:
    return datetime.utcnow().strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3] + "Z"

@dp.message(F.text)
async def handle_text(message: Message):
    chat_id = message.chat.id
    text = (message.text or "").strip()

    session = USER_SESSIONS.get(chat_id)

    if session and session.get("stage") == "await_amount":
        amount_str = text.replace(",", ".")
        try:
            amount = float(amount_str)
            if amount <= 0:
                raise ValueError()
        except ValueError:
            await message.answer("Введите сумму числом, например: 1500.50")
            return

        session["amount"] = amount
        session["stage"] = "await_description"
        await message.answer("Введите описание расхода (или '-' если без описания):")
        return

    if session and session.get("stage") == "await_description":
        description = text if text != "-" else ""

        tg_username = message.from_user.username
        if not tg_username:
            await message.answer(
                "У вас нет username в Telegram. "
                "Задайте его в настройках Telegram и укажите такой же @username в приложении в поле tgID."
            )
            USER_SESSIONS.pop(chat_id, None)
            return

        tg_id_value = f"@{tg_username}"


        user = next((u for u in SERVER_USERS if u.tgID == tg_id_value), None)
        if not user:
            await message.answer(
                "Пользователь с таким tgID не найден на сервере.\n"
                "Убедитесь, что в приложении в профиле указан ваш tgID: "
                f"{tg_id_value}, и выполнена синхронизация."
            )
            USER_SESSIONS.pop(chat_id, None)
            return


        user_cars = [uc for uc in SERVER_USER_CARS if uc.userId == user.id]
        if not user_cars:
            await message.answer(
                "У вас нет автомобилей на сервере.\n"
                "Сначала добавьте автомобиль в приложении и синхронизируйте данные."
            )
            USER_SESSIONS.pop(chat_id, None)
            return

        user_car = user_cars[0]


        expense = ExpenseModel(
            id=find_or_create_expense_id(),
            userId=user.id,
            userCarId=user_car.carId,
            expenseType=session["expense_type"],
            amount=session["amount"],
            date=get_current_iso_datetime(),
            mileage=0,
            description=description or session["expense_type"],
            fuelVolume=None,
            fuelPricePerLiter=None,
        )
        SERVER_EXPENSES.append(expense)

        await message.answer(
            f"Расход \"{expense.expenseType}\" на сумму {expense.amount} ₽ добавлен.\n"
            "Он будет доступен в приложении после следующей синхронизации.",
            reply_markup=main_keyboard(),
        )

        USER_SESSIONS.pop(chat_id, None)
        return

    if text in EXPENSE_TYPES:
        USER_SESSIONS[chat_id] = {
            "stage": "await_amount",
            "expense_type": text,
        }
        await message.answer(f"Вы выбрали категорию: {text}.\nВведите сумму расхода в рублях:")
        return

    await message.answer(
        "Чтобы добавить расход, выберите категорию с помощью кнопок ниже.",
        reply_markup=main_keyboard(),
    )

async def run_bot():
    await bot.delete_webhook(drop_pending_updates=True)
    await dp.start_polling(bot)

if __name__ == "__main__":
    import threading

    def run_api():

        uvicorn.run(app, host="0.0.0.0", port=8001)

    api_thread = threading.Thread(target=run_api, daemon=True)
    api_thread.start()

    asyncio.run(run_bot())