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
    "РўРѕРїР»РёРІРѕ",
    "Р РµРјРѕРЅС‚",
    "РџР°СЂРєРѕРІРєРё Рё РґРѕСЂРѕРіРё",
    "РЁС‚СЂР°С„С‹ Рё РЅР°Р»РѕРіРё",
    "РњРѕР№РєР°",
    "РЎС‚СЂР°С…РѕРІРєР°",
    "РџСЂРѕС‡РµРµ",
]

def expense_type_keyboard() -> ReplyKeyboardMarkup:
    rows = [[KeyboardButton(text=t)] for t in EXPENSE_TYPES]
    return ReplyKeyboardMarkup(keyboard=rows, resize_keyboard=True)

def main_menu_keyboard() -> ReplyKeyboardMarkup:
    rows = [
        [KeyboardButton(text="Р”РѕР±Р°РІРёС‚СЊ СЂР°СЃС…РѕРґ")],
        [KeyboardButton(text="РџРѕСЃРјРѕС‚СЂРµС‚СЊ СЂР°СЃС…РѕРґС‹")],
        [KeyboardButton(text="РћС‚С‡С‘С‚ РІ Excel")],
    ]
    return ReplyKeyboardMarkup(keyboard=rows, resize_keyboard=True)


def confirm_keyboard() -> ReplyKeyboardMarkup:
    rows = [
        [
            KeyboardButton(text="вњ… РџРѕРґС‚РІРµСЂРґРёС‚СЊ Р·Р°РїРёСЃСЊ"),
            KeyboardButton(text="вќЊ РћС‚РјРµРЅРёС‚СЊ"),
        ]
    ]
    return ReplyKeyboardMarkup(keyboard=rows, resize_keyboard=True)


def date_choice_keyboard() -> ReplyKeyboardMarkup:
    rows = [
        [
            KeyboardButton(text="РЎРµРіРѕРґРЅСЏ"),
            KeyboardButton(text="Р’С‡РµСЂР°"),
        ],
        [KeyboardButton(text="Р”СЂСѓРіР°СЏ РґР°С‚Р°")],
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
        "Р‘РѕС‚ Р·Р°РїСѓС‰РµРЅ.\n"
        "Р’С‹Р±РµСЂРёС‚Рµ РґРµР№СЃС‚РІРёРµ СЃ РїРѕРјРѕС‰СЊСЋ РєРЅРѕРїРѕРє РЅРёР¶Рµ.\n"
        "Р’Р°Р¶РЅРѕ: РІ РїСЂРёР»РѕР¶РµРЅРёРё РІ РїРѕР»Рµ tgID РґРѕР»Р¶РµРЅ Р±С‹С‚СЊ РІР°С€ @username, "
        "С‡С‚РѕР±С‹ Р±РѕС‚ РЅР°С€С‘Р» РІР°С€ РїСЂРѕС„РёР»СЊ.",
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


BOT_TIME_OFFSET_HOURS = 3

def format_expense_datetime(dt_str: Optional[str]) -> str:
    """Р¤РѕСЂРјР°С‚РёСЂСѓРµС‚ РґР°С‚Сѓ СЂР°СЃС…РѕРґР° РІ СѓРґРѕР±РЅС‹Р№ РІРёРґ СЃ СѓС‡С‘С‚РѕРј РјРµСЃС‚РЅРѕРіРѕ РІСЂРµРјРµРЅРё: 23.12.2025 17:05."""
    if not dt_str:
        return "РґР°С‚Р° РЅРµРёР·РІРµСЃС‚РЅР°"
    try:
        value = dt_str.replace("Z", "+00:00") if dt_str.endswith("Z") else dt_str
        dt = datetime.fromisoformat(value)
        dt_local = dt + timedelta(hours=BOT_TIME_OFFSET_HOURS)
        return dt_local.strftime("%d.%m.%Y %H:%M")
    except ValueError:
        return dt_str


@dp.message(F.text)
async def handle_text(message: Message):
    chat_id = message.chat.id
    text = (message.text or "").strip()

    session = USER_SESSIONS.get(chat_id)

    if session and session.get("stage") == "confirm_expense":
        if text == "вњ… РџРѕРґС‚РІРµСЂРґРёС‚СЊ Р·Р°РїРёСЃСЊ":
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
                    "РџСЂРѕРёР·РѕС€Р»Р° РѕС€РёР±РєР° РїСЂРё РїРѕРґС‚РІРµСЂР¶РґРµРЅРёРё Р·Р°РїРёСЃРё. РџРѕРїСЂРѕР±СѓР№С‚Рµ РІРІРµСЃС‚Рё СЂР°СЃС…РѕРґ Р·Р°РЅРѕРІРѕ.",
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
                "Р—Р°РїРёСЃСЊ Рѕ СЂР°СЃС…РѕРґРµ СЃРѕС…СЂР°РЅРµРЅР°.\n\n"
                f"РљР°С‚РµРіРѕСЂРёСЏ: {expense.expenseType}\n"
                f"РЎСѓРјРјР°: {expense.amount:.2f} в‚Ѕ\n"
                f"Р”Р°С‚Р° Рё РІСЂРµРјСЏ: {expense_time}\n"
                f"РћРїРёСЃР°РЅРёРµ: {expense.description or 'Р‘РµР· РѕРїРёСЃР°РЅРёСЏ'}\n\n"
                "Р­С‚РѕС‚ СЂР°СЃС…РѕРґ РїРѕСЏРІРёС‚СЃСЏ РІ РїСЂРёР»РѕР¶РµРЅРёРё РїРѕСЃР»Рµ СЃР»РµРґСѓСЋС‰РµР№ СЃРёРЅС…СЂРѕРЅРёР·Р°С†РёРё.",
                reply_markup=main_menu_keyboard(),
            )

            USER_SESSIONS.pop(chat_id, None)
            return

        if text == "вќЊ РћС‚РјРµРЅРёС‚СЊ":
            USER_SESSIONS.pop(chat_id, None)
            await message.answer(
                "Р’РІРѕРґ СЂР°СЃС…РѕРґР° РѕС‚РјРµРЅС‘РЅ.\nР’С‹ РјРѕР¶РµС‚Рµ РЅР°С‡Р°С‚СЊ Р·Р°РЅРѕРІРѕ, РІС‹Р±СЂР°РІ РґРµР№СЃС‚РІРёРµ РІ РіР»Р°РІРЅРѕРј РјРµРЅСЋ.",
                reply_markup=main_menu_keyboard(),
            )
            return

        await message.answer(
            "РџСЂРѕРІРµСЂСЊС‚Рµ РґР°РЅРЅС‹Рµ СЂР°СЃС…РѕРґР° Рё РїРѕРґС‚РІРµСЂРґРёС‚Рµ РёР»Рё РѕС‚РјРµРЅРёС‚Рµ Р·Р°РїРёСЃСЊ СЃ РїРѕРјРѕС‰СЊСЋ РєРЅРѕРїРѕРє РЅРёР¶Рµ.",
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
            await message.answer("Р’РІРµРґРёС‚Рµ СЃСѓРјРјСѓ С‡РёСЃР»РѕРј, РЅР°РїСЂРёРјРµСЂ: 1500.50")
            return

        session["amount"] = amount
        session["stage"] = "await_description"
        await message.answer("Р’РІРµРґРёС‚Рµ РѕРїРёСЃР°РЅРёРµ СЂР°СЃС…РѕРґР° (РёР»Рё '-' РµСЃР»Рё Р±РµР· РѕРїРёСЃР°РЅРёСЏ):")
        return

    if session and session.get("stage") == "choose_date":
        if text == "РЎРµРіРѕРґРЅСЏ":
            date_iso = get_current_iso_datetime()
        elif text == "Р’С‡РµСЂР°":
            date_iso = iso_from_utc(datetime.utcnow() - timedelta(days=1))
        elif text == "Р”СЂСѓРіР°СЏ РґР°С‚Р°":
            session["stage"] = "await_custom_date"
            await message.answer(
                "Р’РІРµРґРёС‚Рµ РґР°С‚Сѓ СЂР°СЃС…РѕРґР° РІ С„РѕСЂРјР°С‚Рµ Р”Р”.РњРњ.Р“Р“Р“Р“ РёР»Рё Р”Р”.РњРњ.Р“Р“Р“Р“ Р§Р§:РњРњ.\n"
                "РќР°РїСЂРёРјРµСЂ: 23.12.2025 РёР»Рё 23.12.2025 14:30.",
            )
            return
        else:
            await message.answer(
                "РџРѕР¶Р°Р»СѓР№СЃС‚Р°, РІС‹Р±РµСЂРёС‚Рµ РґР°С‚Сѓ СЂР°СЃС…РѕРґР° СЃ РїРѕРјРѕС‰СЊСЋ РєРЅРѕРїРѕРє РёР»Рё РІРІРµРґРёС‚Рµ РєРѕСЂСЂРµРєС‚РЅС‹Р№ РІР°СЂРёР°РЅС‚.",
                reply_markup=date_choice_keyboard(),
            )
            return

        expense_time = format_expense_datetime(date_iso)
        session.update(
            {
                "stage": "confirm_expense",
                "date_iso": date_iso,
            }
        )

        await message.answer(
            "РџСЂРѕРІРµСЂСЊС‚Рµ РґР°РЅРЅС‹Рµ СЂР°СЃС…РѕРґР°:\n\n"
            f"РљР°С‚РµРіРѕСЂРёСЏ: {session['expense_type']}\n"
            f"РЎСѓРјРјР°: {session['amount']:.2f} в‚Ѕ\n"
            f"Р”Р°С‚Р° Рё РІСЂРµРјСЏ: {expense_time}\n"
            f"РћРїРёСЃР°РЅРёРµ: {session.get('description') or 'Р‘РµР· РѕРїРёСЃР°РЅРёСЏ'}\n\n"
            "Р•СЃР»Рё РІСЃС‘ РІРµСЂРЅРѕ, РЅР°Р¶РјРёС‚Рµ \"вњ… РџРѕРґС‚РІРµСЂРґРёС‚СЊ Р·Р°РїРёСЃСЊ\".\n"
            "Р•СЃР»Рё РґРѕРїСѓСЃС‚РёР»Рё РѕС€РёР±РєСѓ, РЅР°Р¶РјРёС‚Рµ \"вќЊ РћС‚РјРµРЅРёС‚СЊ\" Рё РІРІРµРґРёС‚Рµ РґР°РЅРЅС‹Рµ Р·Р°РЅРѕРІРѕ.",
            reply_markup=confirm_keyboard(),
        )
        return

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
                "РќРµ СѓРґР°Р»РѕСЃСЊ СЂР°СЃРїРѕР·РЅР°С‚СЊ РґР°С‚Сѓ. Р’РІРµРґРёС‚Рµ РґР°С‚Сѓ РІ С„РѕСЂРјР°С‚Рµ Р”Р”.РњРњ.Р“Р“Р“Р“ РёР»Рё Р”Р”.РњРњ.Р“Р“Р“Р“ Р§Р§:РњРњ.\n"
                "РќР°РїСЂРёРјРµСЂ: 23.12.2025 РёР»Рё 23.12.2025 14:30.",
            )
            return

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
            "РџСЂРѕРІРµСЂСЊС‚Рµ РґР°РЅРЅС‹Рµ СЂР°СЃС…РѕРґР°:\n\n"
            f"РљР°С‚РµРіРѕСЂРёСЏ: {session['expense_type']}\n"
            f"РЎСѓРјРјР°: {session['amount']:.2f} в‚Ѕ\n"
            f"Р”Р°С‚Р° Рё РІСЂРµРјСЏ: {expense_time}\n"
            f"РћРїРёСЃР°РЅРёРµ: {session.get('description') or 'Р‘РµР· РѕРїРёСЃР°РЅРёСЏ'}\n\n"
            "Р•СЃР»Рё РІСЃС‘ РІРµСЂРЅРѕ, РЅР°Р¶РјРёС‚Рµ \"вњ… РџРѕРґС‚РІРµСЂРґРёС‚СЊ Р·Р°РїРёСЃСЊ\".\n"
            "Р•СЃР»Рё РґРѕРїСѓСЃС‚РёР»Рё РѕС€РёР±РєСѓ, РЅР°Р¶РјРёС‚Рµ \"вќЊ РћС‚РјРµРЅРёС‚СЊ\" Рё РІРІРµРґРёС‚Рµ РґР°РЅРЅС‹Рµ Р·Р°РЅРѕРІРѕ.",
            reply_markup=confirm_keyboard(),
        )
        return

    if session and session.get("stage") == "await_description":
        description = text if text != "-" else ""

        tg_username = message.from_user.username
        if not tg_username:
            await message.answer(
                "РЈ РІР°СЃ РЅРµС‚ username РІ Telegram. "
                "Р—Р°РґР°Р№С‚Рµ РµРіРѕ РІ РЅР°СЃС‚СЂРѕР№РєР°С… Telegram Рё СѓРєР°Р¶РёС‚Рµ С‚Р°РєРѕР№ Р¶Рµ @username РІ РїСЂРёР»РѕР¶РµРЅРёРё РІ РїРѕР»Рµ tgID."
            )
            USER_SESSIONS.pop(chat_id, None)
            return

        tg_id_value = f"@{tg_username}"


        user = next((u for u in SERVER_USERS if u.tgID == tg_id_value), None)
        if not user:
            await message.answer(
                "РџРѕР»СЊР·РѕРІР°С‚РµР»СЊ СЃ С‚Р°РєРёРј tgID РЅРµ РЅР°Р№РґРµРЅ РЅР° СЃРµСЂРІРµСЂРµ.\n"
                "РЈР±РµРґРёС‚РµСЃСЊ, С‡С‚Рѕ РІ РїСЂРёР»РѕР¶РµРЅРёРё РІ РїСЂРѕС„РёР»Рµ СѓРєР°Р·Р°РЅ РІР°С€ tgID: "
                f"{tg_id_value}, Рё РІС‹РїРѕР»РЅРµРЅР° СЃРёРЅС…СЂРѕРЅРёР·Р°С†РёСЏ."
            )
            USER_SESSIONS.pop(chat_id, None)
            return


        user_cars = [uc for uc in SERVER_USER_CARS if uc.userId == user.id]
        if not user_cars:
            await message.answer(
                "РЈ РІР°СЃ РЅРµС‚ Р°РІС‚РѕРјРѕР±РёР»РµР№ РЅР° СЃРµСЂРІРµСЂРµ.\n"
                "РЎРЅР°С‡Р°Р»Р° РґРѕР±Р°РІСЊС‚Рµ Р°РІС‚РѕРјРѕР±РёР»СЊ РІ РїСЂРёР»РѕР¶РµРЅРёРё Рё СЃРёРЅС…СЂРѕРЅРёР·РёСЂСѓР№С‚Рµ РґР°РЅРЅС‹Рµ."
            )
            USER_SESSIONS.pop(chat_id, None)
            return

        user_car = user_cars[0]

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
            "Р’С‹Р±РµСЂРёС‚Рµ РґР°С‚Сѓ СЂР°СЃС…РѕРґР°:",
            reply_markup=date_choice_keyboard(),
        )
        return

    if text == "Р”РѕР±Р°РІРёС‚СЊ СЂР°СЃС…РѕРґ":
        USER_SESSIONS.pop(chat_id, None)
        await message.answer(
            "Р’С‹Р±РµСЂРёС‚Рµ РєР°С‚РµРіРѕСЂРёСЋ СЂР°СЃС…РѕРґР° СЃ РїРѕРјРѕС‰СЊСЋ РєРЅРѕРїРѕРє РЅРёР¶Рµ.",
            reply_markup=expense_type_keyboard(),
        )
        return

    if text == "РџРѕСЃРјРѕС‚СЂРµС‚СЊ СЂР°СЃС…РѕРґС‹":
        tg_username = message.from_user.username
        if not tg_username:
            await message.answer(
                "РЈ РІР°СЃ РЅРµС‚ username РІ Telegram. "
                "Р—Р°РґР°Р№С‚Рµ РµРіРѕ РІ РЅР°СЃС‚СЂРѕР№РєР°С… Telegram Рё СѓРєР°Р¶РёС‚Рµ С‚Р°РєРѕР№ Р¶Рµ @username РІ РїСЂРёР»РѕР¶РµРЅРёРё РІ РїРѕР»Рµ tgID."
            )
            return

        tg_id_value = f"@{tg_username}"
        user = next((u for u in SERVER_USERS if u.tgID == tg_id_value), None)
        if not user:
            await message.answer(
                "РџРѕР»СЊР·РѕРІР°С‚РµР»СЊ СЃ С‚Р°РєРёРј tgID РЅРµ РЅР°Р№РґРµРЅ РЅР° СЃРµСЂРІРµСЂРµ.\n"
                "РЈР±РµРґРёС‚РµСЃСЊ, С‡С‚Рѕ РІ РїСЂРёР»РѕР¶РµРЅРёРё РІ РїСЂРѕС„РёР»Рµ СѓРєР°Р·Р°РЅ РІР°С€ tgID: "
                f"{tg_id_value}, Рё РІС‹РїРѕР»РЅРµРЅР° СЃРёРЅС…СЂРѕРЅРёР·Р°С†РёСЏ."
            )
            return

        user_expenses = [e for e in SERVER_EXPENSES if e.userId == user.id]
        if not user_expenses:
            await message.answer(
                "РЈ РІР°СЃ РµС‰С‘ РЅРµС‚ СЂР°СЃС…РѕРґРѕРІ.\n"
                "Р”РѕР±Р°РІСЊС‚Рµ РїРµСЂРІС‹Р№ СЂР°СЃС…РѕРґ С‡РµСЂРµР· РєРЅРѕРїРєСѓ \"Р”РѕР±Р°РІРёС‚СЊ СЂР°СЃС…РѕРґ\".",
                reply_markup=main_menu_keyboard(),
            )
            return

        user_expenses_sorted = sorted(user_expenses, key=lambda e: e.date or "")
        last_expenses = user_expenses_sorted[-10:]

        lines = []
        for e in last_expenses:
            date_str = format_expense_datetime(e.date)
            desc = f" ({e.description})" if e.description else ""
            lines.append(f"вЂў {date_str} вЂ” {e.expenseType}: {e.amount:.2f} в‚Ѕ{desc}")

        text_response = (
            "Р’Р°С€Рё РїРѕСЃР»РµРґРЅРёРµ СЂР°СЃС…РѕРґС‹:\n\n" + "\n".join(lines) +
            f"\n\nРџРѕРєР°Р·Р°РЅРѕ Р·Р°РїРёСЃРµР№: {len(last_expenses)}"
        )
        await message.answer(text_response, reply_markup=main_menu_keyboard())
        return

    if text == "РћС‚С‡С‘С‚ РІ Excel":
        tg_username = message.from_user.username
        if not tg_username:
            await message.answer(
                "РЈ РІР°СЃ РЅРµС‚ username РІ Telegram. "
                "Р—Р°РґР°Р№С‚Рµ РµРіРѕ РІ РЅР°СЃС‚СЂРѕР№РєР°С… Telegram Рё СѓРєР°Р¶РёС‚Рµ С‚Р°РєРѕР№ Р¶Рµ @username РІ РїСЂРёР»РѕР¶РµРЅРёРё РІ РїРѕР»Рµ tgID."
            )
            return

        tg_id_value = f"@{tg_username}"
        user = next((u for u in SERVER_USERS if u.tgID == tg_id_value), None)
        if not user:
            await message.answer(
                "РџРѕР»СЊР·РѕРІР°С‚РµР»СЊ СЃ С‚Р°РєРёРј tgID РЅРµ РЅР°Р№РґРµРЅ РЅР° СЃРµСЂРІРµСЂРµ.\n"
                "РЈР±РµРґРёС‚РµСЃСЊ, С‡С‚Рѕ РІ РїСЂРёР»РѕР¶РµРЅРёРё РІ РїСЂРѕС„РёР»Рµ СѓРєР°Р·Р°РЅ РІР°С€ tgID: "
                f"{tg_id_value}, Рё РІС‹РїРѕР»РЅРµРЅР° СЃРёРЅС…СЂРѕРЅРёР·Р°С†РёСЏ."
            )
            return

        user_expenses = [e for e in SERVER_EXPENSES if e.userId == user.id]
        if not user_expenses:
            await message.answer(
                "РЈ РІР°СЃ РµС‰С‘ РЅРµС‚ СЂР°СЃС…РѕРґРѕРІ.\n"
                "Р”РѕР±Р°РІСЊС‚Рµ РїРµСЂРІС‹Р№ СЂР°СЃС…РѕРґ С‡РµСЂРµР· РєРЅРѕРїРєСѓ \"Р”РѕР±Р°РІРёС‚СЊ СЂР°СЃС…РѕРґ\".",
                reply_markup=main_menu_keyboard(),
            )
            return

        user_expenses_sorted = sorted(user_expenses, key=lambda e: e.date or "")

        output = io.StringIO()
        writer = csv.writer(output, delimiter=';')
        writer.writerow(["Р”Р°С‚Р° Рё РІСЂРµРјСЏ", "РљР°С‚РµРіРѕСЂРёСЏ", "РЎСѓРјРјР°, в‚Ѕ", "РћРїРёСЃР°РЅРёРµ"])

        for e in user_expenses_sorted:
            date_str = format_expense_datetime(e.date)
            writer.writerow([
                date_str,
                e.expenseType,
                f"{e.amount:.2f}",
                e.description or "",
            ])

        data = output.getvalue().encode("utf-8-sig")
        file = BufferedInputFile(data, "expenses_report.csv")

        await message.answer_document(
            document=file,
            caption="РћС‚С‡С‘С‚ РїРѕ РІСЃРµРј РІР°С€РёРј СЂР°СЃС…РѕРґР°Рј (CSV-С„Р°Р№Р» РґР»СЏ Excel).",
            reply_markup=main_menu_keyboard(),
        )
        return

    if text in EXPENSE_TYPES:
        USER_SESSIONS[chat_id] = {
            "stage": "await_amount",
            "expense_type": text,
        }
        await message.answer(f"Р’С‹ РІС‹Р±СЂР°Р»Рё РєР°С‚РµРіРѕСЂРёСЋ: {text}.\nР’РІРµРґРёС‚Рµ СЃСѓРјРјСѓ СЂР°СЃС…РѕРґР° РІ СЂСѓР±Р»СЏС…:")
        return

    await message.answer(
        "Р§С‚РѕР±С‹ РЅР°С‡Р°С‚СЊ, РёСЃРїРѕР»СЊР·СѓР№С‚Рµ РєРЅРѕРїРєРё РіР»Р°РІРЅРѕРіРѕ РјРµРЅСЋ РЅРёР¶Рµ.",
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
