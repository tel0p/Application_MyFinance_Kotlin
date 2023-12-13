package com.example.myfinance.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale


class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Определение структуры базы данных и ее версии
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 6

        // Таблица пользователей
        private const val TABLE_USER = "Users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_LOGIN = "login"
        private const val COLUMN_PASS = "pass"
        private const val COLUMN_PIN = "pin"

        // Таблица категорий расходов
        private const val TABLE_EXPENSE_CATEGORY = "Expenses_Category"
        private const val COLUMN_EXPENSE_CATEGORY_ID = "expense_category_id"
        private const val COLUMN_EXPENSE_CATEGORY = "expense_category"
        private const val COLUMN_EXPENSE_CATEGORY_USER_LOGIN = "expense_category_user_login"
        private const val COLUMN_EXPENSE_CATEGORY_COLOR = "expense_category_color"

        // Таблица категорий доходов
        private const val TABLE_INCOME_CATEGORY = "Income_Category"
        private const val COLUMN_INCOME_CATEGORY_ID = "income_category_id"
        private const val COLUMN_INCOME_CATEGORY = "income_category"
        private const val COLUMN_INCOME_CATEGORY_USER_LOGIN = "income_category_user_login"
        private const val COLUMN_INCOME_CATEGORY_COLOR = "income_category_color"

        // Таблица расходов
        private const val TABLE_EXPENSE = "expenses"
        private const val COLUMN_EXPENSE_ID = "expenses_id"
        private const val COLUMN_EXPENSE_NAME = "expense_name"
        private const val COLUMN_EXPENSE_AMOUNT = "expense_amount"
        private const val COLUMN_EXPENSE_USER_LOGIN = "user_login"
        private const val COLUMN_EXPENSE_COLOR = "expense_color"
        private const val COLUMN_EXPENSE_DATE = "expense_date"
        private const val COLUMN_EXPENSE_COMMENT = "expense_comment"

        // Таблица доходов
        private const val TABLE_INCOME = "incomes"
        private const val COLUMN_INCOME_ID = "income_id"
        private const val COLUMN_INCOME_NAME = "income_name"
        private const val COLUMN_INCOME_AMOUNT = "income_amount"
        private const val COLUMN_INCOME_USER_LOGIN = "user_login"
        private const val COLUMN_INCOME_COLOR = "income_color"
        private const val COLUMN_INCOME_DATE = "income_date"
        private const val COLUMN_INCOME_COMMENT = "income_comment"
    }

    // Метод, вызываемый при создании базы данных
    override fun onCreate(db: SQLiteDatabase?) {
        // Создание таблиц
        val query = "CREATE TABLE $TABLE_USER ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_LOGIN TEXT, $COLUMN_PASS TEXT, $COLUMN_PIN TEXT)"
        val queryExpensesCategory = "CREATE TABLE $TABLE_EXPENSE_CATEGORY " +
                "($COLUMN_EXPENSE_CATEGORY_ID INTEGER PRIMARY KEY," +
                " $COLUMN_EXPENSE_CATEGORY TEXT," +
                " $COLUMN_EXPENSE_CATEGORY_COLOR TEXT," +
                " $COLUMN_EXPENSE_CATEGORY_USER_LOGIN TEXT," +
                " FOREIGN KEY($COLUMN_EXPENSE_CATEGORY_USER_LOGIN) REFERENCES $TABLE_USER($COLUMN_LOGIN))"
        val queryIncomeCategory = "CREATE TABLE $TABLE_INCOME_CATEGORY " +
                "($COLUMN_INCOME_CATEGORY_ID INTEGER PRIMARY KEY," +
                " $COLUMN_INCOME_CATEGORY TEXT," +
                " $COLUMN_INCOME_CATEGORY_COLOR TEXT," +
                " $COLUMN_INCOME_CATEGORY_USER_LOGIN TEXT," +
                " FOREIGN KEY($COLUMN_INCOME_CATEGORY_USER_LOGIN) REFERENCES $TABLE_USER($COLUMN_LOGIN))"
        val queryExpenses = "CREATE TABLE $TABLE_EXPENSE " +
                "($COLUMN_EXPENSE_ID INTEGER PRIMARY KEY, " +
                " $COLUMN_EXPENSE_NAME TEXT, " +
                " $COLUMN_EXPENSE_AMOUNT TEXT," +
                " $COLUMN_EXPENSE_COLOR TEXT, " +
                " $COLUMN_EXPENSE_DATE TEXT, " +
                " $COLUMN_EXPENSE_COMMENT TEXT, " +
                " $COLUMN_EXPENSE_USER_LOGIN TEXT, " +
                " FOREIGN KEY($COLUMN_EXPENSE_USER_LOGIN) REFERENCES $TABLE_USER($COLUMN_LOGIN))"
        val queryIncomes = "CREATE TABLE $TABLE_INCOME " +
                "($COLUMN_INCOME_ID INTEGER PRIMARY KEY, " +
                " $COLUMN_INCOME_NAME TEXT, " +
                " $COLUMN_INCOME_AMOUNT TEXT," +
                " $COLUMN_INCOME_COLOR TEXT, " +
                " $COLUMN_INCOME_DATE TEXT, " +
                " $COLUMN_INCOME_COMMENT TEXT, " +
                " $COLUMN_INCOME_USER_LOGIN TEXT, " +
                " FOREIGN KEY($COLUMN_INCOME_USER_LOGIN) REFERENCES $TABLE_USER($COLUMN_LOGIN))"

        // Выполнение SQL-запросов для создания таблиц
        db!!.run {
            execSQL(query)
            execSQL(queryExpensesCategory)
            execSQL(queryIncomeCategory)
            execSQL(queryExpenses)
            execSQL(queryIncomes)

            // Заполнение таблицы категорий расходов значениями по умолчанию
            val defaultCategoriesExpenses = arrayOf(
                Triple("Здоровье", "red", null),
                Triple("Досуг", "blue", null),
                Triple("Дом", "green", null),
            )
            for (categoryExpenses in defaultCategoriesExpenses) {
                val insertCategoryExpenses = "INSERT INTO $TABLE_EXPENSE_CATEGORY ($COLUMN_EXPENSE_CATEGORY, $COLUMN_EXPENSE_CATEGORY_COLOR, $COLUMN_EXPENSE_CATEGORY_USER_LOGIN) VALUES ('${categoryExpenses.first}', '${categoryExpenses.second}', null)"
                execSQL(insertCategoryExpenses)
            }

            // Заполнение таблицы категорий доходов значениями по умолчанию
            val defaultCategoriesIncomes = arrayOf(
                Triple("Зарплата", "pink", null),
                Triple("Подарок", "yellow", null),
                Triple("Проценты по вкладам", "purple", null),
            )
            for (categoryIncomes in defaultCategoriesIncomes) {
                val insertCategoryExpenses = "INSERT INTO $TABLE_INCOME_CATEGORY ($COLUMN_INCOME_CATEGORY, $COLUMN_INCOME_CATEGORY_COLOR, $COLUMN_INCOME_CATEGORY_USER_LOGIN) VALUES ('${categoryIncomes.first}', '${categoryIncomes.second}', null)"
                execSQL(insertCategoryExpenses)
            }
        }
    }

    // Метод, вызываемый при обновлении базы данных
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Удаление существующих таблиц при обновлении
        db!!.run {
            execSQL("DROP TABLE IF EXISTS $TABLE_USER")
            execSQL("DROP TABLE IF EXISTS $TABLE_EXPENSE_CATEGORY")
            execSQL("DROP TABLE IF EXISTS $TABLE_INCOME_CATEGORY")
            execSQL("DROP TABLE IF EXISTS $TABLE_EXPENSE")
            execSQL("DROP TABLE IF EXISTS $TABLE_INCOME")
        }

        // Повторное создание базы данных
        onCreate(db)
    }

    // Проверка наличия пользователя с указанным логином
    fun isLoginExists(login: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USER WHERE $COLUMN_LOGIN = ?"
        val cursor = db.rawQuery(query, arrayOf(login))
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    // Добавление нового пользователя в базу данных
    fun addUser(user: User){
        val values = ContentValues()
        values.put(COLUMN_LOGIN, user.login)
        values.put(COLUMN_PASS, user.pass)

        val db = this.writableDatabase
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    // Проверка наличия пользователя с указанным логином и паролем
    @SuppressLint("Range")
    fun getUser(login: String, pass: String): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM $TABLE_USER WHERE $COLUMN_LOGIN = '$login' AND $COLUMN_PASS = '$pass'", null)
        return result.moveToFirst()
    }

    // Сохранение пин-кода для пользователя
    fun savePinCode(login: String, pinCode: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PIN, pinCode)
        db.update(TABLE_USER, values, "$COLUMN_LOGIN = ?", arrayOf(login))
        db.close()
    }

    // Получение пин-кода для пользователя
    @SuppressLint("Range")
    fun getPinCode(login: String): String? {
        val db = readableDatabase
        val query = "SELECT $COLUMN_PIN FROM $TABLE_USER WHERE $COLUMN_LOGIN = ?"
        val cursor = db.rawQuery(query, arrayOf(login))
        var pinCode: String? = null
        if (cursor.moveToFirst()) {
            pinCode = cursor.getString(cursor.getColumnIndex(COLUMN_PIN))
        }
        cursor.close()
        db.close()
        return pinCode
    }

    // Проверка наличия пин-кода для пользователя
    @SuppressLint("Range")
    fun checkPinCodeExists(login: String): String? {
        val db = readableDatabase
        val query = "SELECT $COLUMN_PIN FROM $TABLE_USER WHERE $COLUMN_LOGIN = ?"
        val cursor = db.rawQuery(query, arrayOf(login))

        var hasPinCode: String? = null
        if (cursor.moveToFirst()) {
            hasPinCode = cursor.getString(cursor.getColumnIndex(COLUMN_PIN))
        }
        cursor.close()
        db.close()

        return hasPinCode
    }

    // Получение списка категорий расходов для пользователя
    @SuppressLint("Range")
    fun getExpenseCategories(userLogin: String?): List<Category> {
        val db = readableDatabase
        val expenseCategories = mutableListOf<Category>()
        val query = "SELECT $COLUMN_EXPENSE_CATEGORY, $COLUMN_EXPENSE_CATEGORY_COLOR FROM $TABLE_EXPENSE_CATEGORY WHERE $COLUMN_EXPENSE_CATEGORY_USER_LOGIN = ? OR $COLUMN_EXPENSE_CATEGORY_USER_LOGIN IS NULL"
        val cursor = db.rawQuery(query, arrayOf(userLogin))

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY))
            val color = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY_COLOR))

            val category = Category(name, color,"", "", "")
            expenseCategories.add(category)
        }

        cursor.close()
        db.close()
        return expenseCategories
    }

    // Получение списка категорий доходов для пользователя
    @SuppressLint("Range")
    fun getIncomeCategories(userLogin: String?): List<Category> {
        val db = readableDatabase
        val incomeCategories = mutableListOf<Category>()
        val query = "SELECT $COLUMN_INCOME_CATEGORY, $COLUMN_INCOME_CATEGORY_COLOR FROM $TABLE_INCOME_CATEGORY WHERE $COLUMN_INCOME_CATEGORY_USER_LOGIN = ? OR $COLUMN_INCOME_CATEGORY_USER_LOGIN IS NULL"
        val cursor = db.rawQuery(query, arrayOf(userLogin))

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_CATEGORY))
            val color = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_CATEGORY_COLOR))

            val category = Category(name, color,"", "", "")
            incomeCategories.add(category)
        }

        cursor.close()
        db.close()
        return incomeCategories
    }

    // Добавление новой категории расходов
    fun addExpenseCategory(category: String, color: String, userLogin: String?) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EXPENSE_CATEGORY, category)
            put(COLUMN_EXPENSE_CATEGORY_COLOR, color)
            put(COLUMN_EXPENSE_CATEGORY_USER_LOGIN, userLogin)
        }
        db.insert(TABLE_EXPENSE_CATEGORY, null, values)
        db.close()
    }

    // Добавление новой категории доходов
    fun addIncomeCategory(category: String, color: String, userLogin: String?) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_INCOME_CATEGORY, category)
            put(COLUMN_INCOME_CATEGORY_COLOR, color)
            put(COLUMN_INCOME_CATEGORY_USER_LOGIN, userLogin)
        }
        db.insert(TABLE_INCOME_CATEGORY, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun addExpense(expenseName: String, amount: String, color: String, comment: String, date: String, userLogin: String) {
        val db: SQLiteDatabase = writableDatabase

        val expenseId = generateUniqueId()

        val cursor = db.rawQuery("SELECT * FROM $TABLE_EXPENSE WHERE $COLUMN_EXPENSE_ID = ?", arrayOf(expenseId.toString()))

        if (cursor.moveToFirst()) {
            val currentAmount = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT)).toInt()
            val newAmount = currentAmount + amount.toInt()

            val values = ContentValues().apply {
                put(COLUMN_EXPENSE_AMOUNT, newAmount.toString())
                put(COLUMN_EXPENSE_COMMENT, comment)
            }

            db.update(TABLE_EXPENSE, values, "$COLUMN_EXPENSE_ID = ?", arrayOf(expenseId.toString()))
        } else {
            val values = ContentValues().apply {
                put(COLUMN_EXPENSE_ID, expenseId)
                put(COLUMN_EXPENSE_NAME, expenseName)
                put(COLUMN_EXPENSE_AMOUNT, amount)
                put(COLUMN_EXPENSE_COLOR, color)
                put(COLUMN_EXPENSE_COMMENT, comment)
                put(COLUMN_EXPENSE_DATE, date)
                put(COLUMN_EXPENSE_USER_LOGIN, userLogin)
            }

            db.insert(TABLE_EXPENSE, null, values)
        }

        cursor.close()
        db.close()
    }

    // Получение списка расходов пользователя за выбранный период
    @SuppressLint("Range")
    fun getExpense(userLogin: String?, selectedPeriod: String): List<Category> {
        val expense = mutableListOf<Category>()
        val db = readableDatabase

        // Формирование SQL-запроса в зависимости от выбранного периода
        val query = when (selectedPeriod) {
            "День" -> "SELECT $COLUMN_EXPENSE_NAME, $COLUMN_EXPENSE_COLOR, SUM($COLUMN_EXPENSE_AMOUNT) AS total_amount FROM $TABLE_EXPENSE WHERE $COLUMN_EXPENSE_USER_LOGIN = ? AND $COLUMN_EXPENSE_DATE = ? GROUP BY $COLUMN_EXPENSE_NAME, $COLUMN_EXPENSE_COLOR"
            "Неделя" -> "SELECT $COLUMN_EXPENSE_NAME, $COLUMN_EXPENSE_COLOR, SUM($COLUMN_EXPENSE_AMOUNT) AS total_amount FROM $TABLE_EXPENSE WHERE $COLUMN_EXPENSE_USER_LOGIN = ? AND $COLUMN_EXPENSE_DATE BETWEEN ? AND ? GROUP BY $COLUMN_EXPENSE_NAME, $COLUMN_EXPENSE_COLOR"
            "Месяц" -> "SELECT $COLUMN_EXPENSE_NAME, $COLUMN_EXPENSE_COLOR, SUM($COLUMN_EXPENSE_AMOUNT) AS total_amount FROM $TABLE_EXPENSE WHERE $COLUMN_EXPENSE_USER_LOGIN = ? AND strftime('%Y-%m', date(substr($COLUMN_EXPENSE_DATE, 7, 4) || '-' || substr($COLUMN_EXPENSE_DATE, 4, 2) || '-' || substr($COLUMN_EXPENSE_DATE, 1, 2))) = ? GROUP BY $COLUMN_EXPENSE_NAME, $COLUMN_EXPENSE_COLOR"
            "Год" -> "SELECT $COLUMN_EXPENSE_NAME, $COLUMN_EXPENSE_COLOR, SUM($COLUMN_EXPENSE_AMOUNT) AS total_amount FROM $TABLE_EXPENSE WHERE $COLUMN_EXPENSE_USER_LOGIN = ? AND strftime('%Y', date(substr($COLUMN_EXPENSE_DATE, 7, 4) || '-' || substr($COLUMN_EXPENSE_DATE, 4, 2) || '-' || substr($COLUMN_EXPENSE_DATE, 1, 2))) = ? GROUP BY $COLUMN_EXPENSE_NAME, $COLUMN_EXPENSE_COLOR"
            else -> "SELECT $COLUMN_EXPENSE_NAME, $COLUMN_EXPENSE_COLOR, SUM($COLUMN_EXPENSE_AMOUNT) AS total_amount FROM $TABLE_EXPENSE WHERE $COLUMN_EXPENSE_USER_LOGIN = ? GROUP BY $COLUMN_EXPENSE_NAME, $COLUMN_EXPENSE_COLOR"
        }

        val currentDate = LocalDate.now()
        val firstDayOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val lastDayOfWeek = firstDayOfWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val cursor = when (selectedPeriod) {
            "День" -> db.rawQuery(query, arrayOf(userLogin, currentDate.format(DateTimeFormatter.ofPattern("d.MM.yyyy"))))
            "Неделя" -> db.rawQuery(query, arrayOf(userLogin, firstDayOfWeek.format(DateTimeFormatter.ofPattern("d.MM.yyyy")), lastDayOfWeek.format(DateTimeFormatter.ofPattern("d.MM.yyyy"))))
            "Месяц" -> db.rawQuery(query, arrayOf(userLogin, currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"))))
            "Год" -> db.rawQuery(query, arrayOf(userLogin, currentDate.format(DateTimeFormatter.ofPattern("yyyy"))))
            else -> db.rawQuery(query, arrayOf(userLogin))
        }

        while (cursor.moveToNext()) {
            val categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_NAME))
            val categoryColor = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_COLOR))
            val totalAmount = cursor.getString(cursor.getColumnIndex("total_amount"))

            val category = Category(categoryName, categoryColor, totalAmount, "", "")
            expense.add(category)
        }
        cursor.close()
        db.close()
        return expense
    }

    @SuppressLint("Range")
    fun addIncome(incomeName: String, amount: String, color: String, comment: String, date: String, userLogin: String) {
        val db: SQLiteDatabase = writableDatabase

        val incomeId = generateUniqueId()

        val cursor = db.rawQuery("SELECT * FROM $TABLE_INCOME WHERE $COLUMN_INCOME_ID = ?", arrayOf(incomeId.toString()))

        if (cursor.moveToFirst()) {
            val currentAmount = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT)).toInt()
            val newAmount = currentAmount + amount.toInt()

            val values = ContentValues().apply {
                put(COLUMN_INCOME_AMOUNT, newAmount.toString())
                put(COLUMN_INCOME_COMMENT, comment)
            }

            db.update(TABLE_INCOME, values, "$COLUMN_INCOME_ID = ?", arrayOf(incomeId.toString()))
        } else {
            val values = ContentValues().apply {
                put(COLUMN_INCOME_ID, incomeId)
                put(COLUMN_INCOME_NAME, incomeName)
                put(COLUMN_INCOME_AMOUNT, amount)
                put(COLUMN_INCOME_COLOR, color)
                put(COLUMN_INCOME_COMMENT, comment)
                put(COLUMN_INCOME_DATE, date)
                put(COLUMN_INCOME_USER_LOGIN, userLogin)
            }

            db.insert(TABLE_INCOME, null, values)
        }

        cursor.close()
        db.close()
    }

    // Получение списка доходов пользователя за выбранный период
    @SuppressLint("Range")
    fun getIncome(userLogin: String?, selectedPeriod: String): List<Category> {
        val income = mutableListOf<Category>()
        val db = readableDatabase

        // Формирование SQL-запроса в зависимости от выбранного периода
        val query = when (selectedPeriod) {
            "День" -> "SELECT $COLUMN_INCOME_NAME, $COLUMN_INCOME_COLOR, SUM($COLUMN_INCOME_AMOUNT) AS total_amount FROM $TABLE_INCOME WHERE $COLUMN_INCOME_USER_LOGIN = ? AND $COLUMN_INCOME_DATE = ? GROUP BY $COLUMN_INCOME_NAME, $COLUMN_INCOME_COLOR"
            "Неделя" -> "SELECT $COLUMN_INCOME_NAME, $COLUMN_INCOME_COLOR, SUM($COLUMN_INCOME_AMOUNT) AS total_amount FROM $TABLE_INCOME WHERE $COLUMN_INCOME_USER_LOGIN = ? AND $COLUMN_INCOME_DATE BETWEEN ? AND ? GROUP BY $COLUMN_INCOME_NAME, $COLUMN_INCOME_COLOR"
            "Месяц" -> "SELECT $COLUMN_INCOME_NAME, $COLUMN_INCOME_COLOR, SUM($COLUMN_INCOME_AMOUNT) AS total_amount FROM $TABLE_INCOME WHERE $COLUMN_INCOME_USER_LOGIN = ? AND strftime('%Y-%m', date(substr($COLUMN_INCOME_DATE, 7, 4) || '-' || substr($COLUMN_INCOME_DATE, 4, 2) || '-' || substr($COLUMN_INCOME_DATE, 1, 2))) = ? GROUP BY $COLUMN_INCOME_NAME, $COLUMN_INCOME_COLOR"
            "Год" -> "SELECT $COLUMN_INCOME_NAME, $COLUMN_INCOME_COLOR, SUM($COLUMN_INCOME_AMOUNT) AS total_amount FROM $TABLE_INCOME WHERE $COLUMN_INCOME_USER_LOGIN = ? AND strftime('%Y', date(substr($COLUMN_INCOME_DATE, 7, 4) || '-' || substr($COLUMN_INCOME_DATE, 4, 2) || '-' || substr($COLUMN_INCOME_DATE, 1, 2))) = ? GROUP BY $COLUMN_INCOME_NAME, $COLUMN_INCOME_COLOR"
            else -> "SELECT $COLUMN_INCOME_NAME, $COLUMN_INCOME_COLOR, SUM($COLUMN_INCOME_AMOUNT) AS total_amount FROM $TABLE_INCOME WHERE $COLUMN_INCOME_USER_LOGIN = ? GROUP BY $COLUMN_INCOME_NAME, $COLUMN_INCOME_COLOR"
        }

        val currentDate = LocalDate.now()
        val firstDayOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val lastDayOfWeek = firstDayOfWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val cursor = when (selectedPeriod) {
            "День" -> db.rawQuery(query, arrayOf(userLogin, currentDate.format(DateTimeFormatter.ofPattern("d.MM.yyyy"))))
            "Неделя" -> db.rawQuery(query, arrayOf(userLogin, firstDayOfWeek.format(DateTimeFormatter.ofPattern("d.MM.yyyy")), lastDayOfWeek.format(DateTimeFormatter.ofPattern("d.MM.yyyy"))))
            "Месяц" -> db.rawQuery(query, arrayOf(userLogin, currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"))))
            "Год" -> db.rawQuery(query, arrayOf(userLogin, currentDate.format(DateTimeFormatter.ofPattern("yyyy"))))
            else -> db.rawQuery(query, arrayOf(userLogin))
        }

        while (cursor.moveToNext()) {
            val categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_NAME))
            val categoryColor = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_COLOR))
            val totalAmount = cursor.getString(cursor.getColumnIndex("total_amount"))

            val category = Category(categoryName, categoryColor, totalAmount, "", "")
            income.add(category)
        }

        cursor.close()
        db.close()
        return income
    }

    @SuppressLint("Range")
    fun getIncomeInformationForCategory(userLogin: String?, categoryName: String): List<Category> {
        val categoriesList = mutableListOf<Category>()
        val db = readableDatabase

        val query = "SELECT * FROM $TABLE_INCOME WHERE $COLUMN_INCOME_USER_LOGIN = ? AND $COLUMN_INCOME_NAME = ?"

        val cursor = db.rawQuery(query, arrayOf(userLogin, categoryName))

        if (cursor.moveToFirst()) {
            do {
                val categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_NAME))
                val categoryColor = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_COLOR))
                val amount = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT))
                val dateStr = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_DATE))
                val comment = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_COMMENT))

                val category = Category(categoryName, categoryColor, amount, comment, dateStr)
                categoriesList.add(category)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return categoriesList.sortedByDescending { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(it.date) }
    }

    @SuppressLint("Range")
    fun getExpenseInformationForCategory(userLogin: String?, categoryName: String): List<Category> {
        val categoriesList = mutableListOf<Category>()
        val db = readableDatabase

        val query =
            "SELECT * FROM $TABLE_EXPENSE WHERE $COLUMN_EXPENSE_USER_LOGIN = ? AND $COLUMN_EXPENSE_NAME = ?"

        val cursor = db.rawQuery(query, arrayOf(userLogin, categoryName))

        if (cursor.moveToFirst()) {
            do {
                val categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_NAME))
                val categoryColor = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_COLOR))
                val amount = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT))
                val dateStr = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_DATE))
                val comment = cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_COMMENT))

                val category = Category(categoryName, categoryColor, amount, comment, dateStr)
                categoriesList.add(category)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return categoriesList.sortedByDescending { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(it.date) }
    }
}

private fun generateUniqueId(): Long {
    return System.currentTimeMillis()
}