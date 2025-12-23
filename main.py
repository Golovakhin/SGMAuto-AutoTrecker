from fastapi import FastAPI
from pydantic import BaseModel
from typing import List, Optional, Dict
from aiogram import Bot, Dispatcher, F
from aiogram.types import Message, ReplyKeyboardMarkup, KeyboardButton, BufferedInputFile
from datetime import datetime, timedelta
import asyncio
import uvicorn
import io
import csv

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

def expense_type_keyboard() -> ReplyKeyboardMarkup:
    rows = [[KeyboardButton(text=t)] for t in EXPENSE_TYPES]
    return ReplyKeyboardMarkup(keyboard=rows, resize_keyboard=True)

def main_menu_keyboard() -> ReplyKeyboardMarkup:
    rows = [
        [KeyboardButton(text="Добавить расход")],
        [KeyboardButton(text="Посмотреть расходы")],
        [KeyboardButton(text="Отчёт в Excel")],
    ]
    return ReplyKeyboardMarkup(keyboard=rows, resize_keyboard=True)


def confirm_keyboard() -> ReplyKeyboardMarkup:
    rows = [
        [
            KeyboardButton(text="✅ Подтвердить запись"),
            KeyboardButton(text="❌ Отменить"),
        ]
    ]
    return ReplyKeyboardMarkup(keyboard=rows, resize_keyboard=True)


def date_choice_keyboard() -> ReplyKeyboardMarkup:
    rows = [
        [
            KeyboardButton(text="Сегодня"),
            KeyboardButton(text="Вчера"),
        ],
        [KeyboardButton(text="Другая дата")],
    ]
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
        "Выберите действие с помощью кнопок ниже.\n"
        "Важно: в приложении в поле tgID должен быть ваш @username, "
        "чтобы бот нашёл ваш профиль.",
        reply_markup=main_menu_keyboard(),
    )

def find_or_create_expense_id() -> int:
    if not SERVER_EXPENSES:
        return 1
    return max(e.id or 0 for e in SERVER_EXPENSES) + 1

def iso_from_utc(dt: datetime) -> str:
    return dt.strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3] + "Z"


def get_current_iso_datetime() -> str:
    return iso_from_utc(datetime.utcnow())


BOT_TIME_OFFSET_HOURS = 3  # сдвиг относительно UTC для отображения пользователю (по умолчанию МСК)

def format_expense_datetime(dt_str: Optional[str]) -> str:
    """Форматирует дату расхода в удобный вид с учётом местного времени: 23.12.2025 17:05."""
    if not dt_str:
        return "дата неизвестна"
    try:
        value = dt_str.replace("Z", "+00:00") if dt_str.endswith("Z") else dt_str
        dt = datetime.fromisoformat(value)
        # dt хранится в UTC, сдвигаем к локальному времени пользователя
        dt_local = dt + timedelta(hours=BOT_TIME_OFFSET_HOURS)
        return dt_local.strftime("%d.%m.%Y %H:%M")
    except ValueError:
        return dt_str


@dp.message(F.text)
async def handle_text(message: Message):
    chat_id = message.chat.id
    text = (message.text or "").strip()

    session = USER_SESSIONS.get(chat_id)

    # Шаг подтверждения перед сохранением расхода
    if session and session.get("stage") == "confirm_expense":
        if text == "✅ Подтвердить запись":
            try:
                tg_id_value = session["tg_id_value"]
                user_id = session["user_id"]
                user_car_id = session["user_car_id"]
                expense_type = session["expense_type"]
                amount = session["amount"]
                description = session["description"]
                date_iso = session["date_iso"]
            except KeyError:
                USER_SESSIONS.pop(chat_id, None)
                await message.answer(
                    "Произошла ошибка при подтверждении записи. Попробуйте ввести расход заново.",
                    reply_markup=main_menu_keyboard(),
                )
                return

            expense = ExpenseModel(
                id=find_or_create_expense_id(),
                userId=user_id,
                userCarId=user_car_id,
                expenseType=expense_type,
                amount=amount,
                date=date_iso,
                mileage=0,
                description=description or expense_type,
                fuelVolume=None,
                fuelPricePerLiter=None,
            )
            SERVER_EXPENSES.append(expense)

            expense_time = format_expense_datetime(expense.date)
            await message.answer(
                "Запись о расходе сохранена.\n\n"
                f"Категория: {expense.expenseType}\n"
                f"Сумма: {expense.amount:.2f} ₽\n"
                f"Дата и время: {expense_time}\n"
                f"Описание: {expense.description or 'Без описания'}\n\n"
                "Этот расход появится в приложении после следующей синхронизации.",
                reply_markup=main_menu_keyboard(),
            )

            USER_SESSIONS.pop(chat_id, None)
            return

        if text == "❌ Отменить":
            USER_SESSIONS.pop(chat_id, None)
            await message.answer(
                "Ввод расхода отменён.\nВы можете начать заново, выбрав действие в главном меню.",
                reply_markup=main_menu_keyboard(),
            )
            return

        # Если пользователь ввёл что-то другое, просим воспользоваться кнопками подтверждения
        await message.answer(
            "Проверьте данные расхода и подтвердите или отмените запись с помощью кнопок ниже.",
            reply_markup=confirm_keyboard(),
        )
        return

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

    # Выбор даты расхода (после ввода описания)
    if session and session.get("stage") == "choose_date":
        if text == "Сегодня":
            date_iso = get_current_iso_datetime()
        elif text == "Вчера":
            date_iso = iso_from_utc(datetime.utcnow() - timedelta(days=1))
        elif text == "Другая дата":
            session["stage"] = "await_custom_date"
            await message.answer(
                "Введите дату расхода в формате ДД.ММ.ГГГГ или ДД.ММ.ГГГГ ЧЧ:ММ.\n"
                "Например: 23.12.2025 или 23.12.2025 14:30.",
            )
            return
        else:
            await message.answer(
                "Пожалуйста, выберите дату расхода с помощью кнопок или введите корректный вариант.",
                reply_markup=date_choice_keyboard(),
            )
            return

        # У нас есть date_iso, переходим к шагу подтверждения
        expense_time = format_expense_datetime(date_iso)
        session.update(
            {
                "stage": "confirm_expense",
                "date_iso": date_iso,
            }
        )

        await message.answer(
            "Проверьте данные расхода:\n\n"
            f"Категория: {session['expense_type']}\n"
            f"Сумма: {session['amount']:.2f} ₽\n"
            f"Дата и время: {expense_time}\n"
            f"Описание: {session.get('description') or 'Без описания'}\n\n"
            "Если всё верно, нажмите \"✅ Подтвердить запись\".\n"
            "Если допустили ошибку, нажмите \"❌ Отменить\" и введите данные заново.",
            reply_markup=confirm_keyboard(),
        )
        return

    # Ввод произвольной даты
    if session and session.get("stage") == "await_custom_date":
        text_clean = text.strip()
        dt_local = None
        for fmt in ("%d.%m.%Y %H:%M", "%d.%m.%Y"):
            try:
                dt_local = datetime.strptime(text_clean, fmt)
                break
            except ValueError:
                continue

        if dt_local is None:
            await message.answer(
                "Не удалось распознать дату. Введите дату в формате ДД.ММ.ГГГГ или ДД.ММ.ГГГГ ЧЧ:ММ.\n"
                "Например: 23.12.2025 или 23.12.2025 14:30.",
            )
            return

        # Переводим локальное время пользователя в UTC
        dt_utc = dt_local - timedelta(hours=BOT_TIME_OFFSET_HOURS)
        date_iso = iso_from_utc(dt_utc)

        expense_time = format_expense_datetime(date_iso)
        session.update(
            {
                "stage": "confirm_expense",
                "date_iso": date_iso,
            }
        )

        await message.answer(
            "Проверьте данные расхода:\n\n"
            f"Категория: {session['expense_type']}\n"
            f"Сумма: {session['amount']:.2f} ₽\n"
            f"Дата и время: {expense_time}\n"
            f"Описание: {session.get('description') or 'Без описания'}\n\n"
            "Если всё верно, нажмите \"✅ Подтвердить запись\".\n"
            "Если допустили ошибку, нажмите \"❌ Отменить\" и введите данные заново.",
            reply_markup=confirm_keyboard(),
        )
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

        # Переходим к выбору даты расхода
        session.update(
            {
                "stage": "choose_date",
                "tg_id_value": tg_id_value,
                "user_id": user.id,
                "user_car_id": user_car.carId,
                "description": description,
            }
        )

        await message.answer(
            "Выберите дату расхода:",
            reply_markup=date_choice_keyboard(),
        )
        return

    # Главное меню
    if text == "Добавить расход":
        USER_SESSIONS.pop(chat_id, None)
        await message.answer(
            "Выберите категорию расхода с помощью кнопок ниже.",
            reply_markup=expense_type_keyboard(),
        )
        return

    if text == "Посмотреть расходы":
        tg_username = message.from_user.username
        if not tg_username:
            await message.answer(
                "У вас нет username в Telegram. "
                "Задайте его в настройках Telegram и укажите такой же @username в приложении в поле tgID."
            )
            return

        tg_id_value = f"@{tg_username}"
        user = next((u for u in SERVER_USERS if u.tgID == tg_id_value), None)
        if not user:
            await message.answer(
                "Пользователь с таким tgID не найден на сервере.\n"
                "Убедитесь, что в приложении в профиле указан ваш tgID: "
                f"{tg_id_value}, и выполнена синхронизация."
            )
            return

        user_expenses = [e for e in SERVER_EXPENSES if e.userId == user.id]
        if not user_expenses:
            await message.answer(
                "У вас ещё нет расходов.\n"
                "Добавьте первый расход через кнопку \"Добавить расход\".",
                reply_markup=main_menu_keyboard(),
            )
            return

        # Показываем последние 10 расходов пользователя
        user_expenses_sorted = sorted(user_expenses, key=lambda e: e.date or "")
        last_expenses = user_expenses_sorted[-10:]

        lines = []
        for e in last_expenses:
            date_str = format_expense_datetime(e.date)
            desc = f" ({e.description})" if e.description else ""
            lines.append(f"• {date_str} — {e.expenseType}: {e.amount:.2f} ₽{desc}")

        text_response = (
            "Ваши последние расходы:\n\n" + "\n".join(lines) +
            f"\n\nПоказано записей: {len(last_expenses)}"
        )
        await message.answer(text_response, reply_markup=main_menu_keyboard())
        return

    if text == "Отчёт в Excel":
        tg_username = message.from_user.username
        if not tg_username:
            await message.answer(
                "У вас нет username в Telegram. "
                "Задайте его в настройках Telegram и укажите такой же @username в приложении в поле tgID."
            )
            return

        tg_id_value = f"@{tg_username}"
        user = next((u for u in SERVER_USERS if u.tgID == tg_id_value), None)
        if not user:
            await message.answer(
                "Пользователь с таким tgID не найден на сервере.\n"
                "Убедитесь, что в приложении в профиле указан ваш tgID: "
                f"{tg_id_value}, и выполнена синхронизация."
            )
            return

        user_expenses = [e for e in SERVER_EXPENSES if e.userId == user.id]
        if not user_expenses:
            await message.answer(
                "У вас ещё нет расходов.\n"
                "Добавьте первый расход через кнопку \"Добавить расход\".",
                reply_markup=main_menu_keyboard(),
            )
            return

        # Формируем полный отчёт по всем расходам пользователя
        user_expenses_sorted = sorted(user_expenses, key=lambda e: e.date or "")

        output = io.StringIO()
        writer = csv.writer(output, delimiter=';')
        writer.writerow(["Дата и время", "Категория", "Сумма, ₽", "Описание"])

        for e in user_expenses_sorted:
            date_str = format_expense_datetime(e.date)
            writer.writerow([
                date_str,
                e.expenseType,
                f"{e.amount:.2f}",
                e.description or "",
            ])

        data = output.getvalue().encode("utf-8-sig")  # BOM, чтобы Excel корректно открыл русский текст
        file = BufferedInputFile(data, "expenses_report.csv")

        await message.answer_document(
            document=file,
            caption="Отчёт по всем вашим расходам (CSV-файл для Excel).",
            reply_markup=main_menu_keyboard(),
        )
        return

    if text in EXPENSE_TYPES:
        USER_SESSIONS[chat_id] = {
            "stage": "await_amount",
            "expense_type": text,
        }
        await message.answer(f"Вы выбрали категорию: {text}.\nВведите сумму расхода в рублях:")
        return

    await message.answer(
        "Чтобы начать, используйте кнопки главного меню ниже.",
        reply_markup=main_menu_keyboard(),
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