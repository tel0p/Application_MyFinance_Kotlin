package com.example.myfinance.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myfinance.R
import com.example.myfinance.db.Category
import com.example.myfinance.db.DBHelper
import com.example.myfinance.db.Date
import com.example.myfinance.db.FileClass
import kotlin.math.ceil

@Composable
fun MyFullScreenDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .height(350.dp)
                .width(300.dp)
                .clip(
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOperationWindow() {
    val context = LocalContext.current
    val db = DBHelper(context)
    val userLogin = FileClass().readLoginFromFile(context)
    var selectedCategory by remember { mutableStateOf("Расходы") }
    val selectedCategories = remember { mutableStateOf<Category?>(null) }
    var amountMoney by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    val category = listOf("Расходы", "Доходы")
    val calendar = java.util.Calendar.getInstance()
    val selectedYear = remember { mutableStateOf(calendar.get(java.util.Calendar.YEAR)) }
    val selectedMonth = remember { mutableStateOf(calendar.get(java.util.Calendar.MONTH)) }
    val daysInMonth = getDaysInMonth(selectedYear.value, selectedMonth.value)
    calendar.set(selectedYear.value, selectedMonth.value, 1)
    val firstDayOfWeek = (calendar.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7
    val rows = ceil((daysInMonth + firstDayOfWeek) / 7f).toInt()
    val selectedDay = remember { mutableStateOf(0) }
    var goToMainWindow by remember { mutableStateOf(false) }
    var goToAddCategoryWindow by remember { mutableStateOf(false) }
    var goToCalendarWindow by remember { mutableStateOf(false) }

    if (goToMainWindow) {
        Intent(LocalContext.current, MainWindow()::class.java).action
    } else if (goToAddCategoryWindow) {
        Intent(LocalContext.current, AddCategoryWindow()::class.java).action
    } else if (goToCalendarWindow ) {

        MyFullScreenDialog(
            onDismissRequest = { goToCalendarWindow = false },
            content = {
                Column(
                    Modifier.background(colorResource(R.color.backgroundColor)).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .height(80.dp)
                            .background(colorResource(R.color.cardDarkColor))
                    ) {
                        Row(
                            Modifier
                                .fillMaxSize()
                                .background(colorResource(R.color.cardDarkColor)),
                            horizontalArrangement = Arrangement.Start
                        ) {

                            Column(
                                Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${getMonthName(selectedMonth.value)} ${selectedYear.value}",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = colorResource(R.color.textColor),
                                    fontSize = 30.sp
                                )
                            }
                        }
                    }
                    Column(
                        Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row {
                            listOf("пн", "вт", "ср", "чт", "пт", "сб", "вс").forEach { day ->
                                Text(
                                    text = day,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    color = colorResource(R.color.textColor)
                                )
                            }
                        }
                        for (row in 0 until rows) {
                            Row {
                                for (column in 0 until 7) {
                                    val day = row * 7 + column + 1 - firstDayOfWeek
                                    if (day in 1..daysInMonth) {
                                        Text(
                                            text = day.toString(),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedDay.value = day },
                                            textAlign = TextAlign.Center,
                                            color = if (selectedDay.value == day) colorResource(R.color.tryTextColor) else colorResource(
                                                R.color.textColor
                                            )
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.padding(top = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    selectedMonth.value -= 1
                                    if (selectedMonth.value < 0) {
                                        selectedMonth.value = 11
                                        selectedYear.value -= 1
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(5.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(
                                        R.color.cardLightColor
                                    )
                                )
                            ) {
                                Text("Предыдущий месяц", color = colorResource(R.color.textColor))
                            }

                            Button(
                                onClick = {
                                    selectedMonth.value += 1
                                    if (selectedMonth.value > 11) {
                                        selectedMonth.value = 0
                                        selectedYear.value += 1
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(5.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(
                                        R.color.cardLightColor
                                    )
                                )
                            ) {
                                Text("Следующий месяц", color = colorResource(R.color.textColor))
                            }
                        }
                        Column() {
                            Button(
                                onClick = {
                                    if (selectedDay.value != 0) {
                                        Date.saveDate(
                                            selectedDay.value.toString().padStart(2, '0'),
                                            (selectedMonth.value + 1).toString().padStart(2, '0'),
                                            selectedYear.value.toString()
                                        )
                                        goToCalendarWindow = false
                                        Toast.makeText(context, "Дата: ${selectedDay.value.toString().padStart(2, '0')}.${(selectedMonth.value + 1).toString().padStart(2, '0')}.${selectedYear.value} успешно выбрана", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context, "Выберите день", Toast.LENGTH_LONG).show()
                                    }
                                },
                                modifier = Modifier
                                    .padding(5.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(
                                        R.color.cardLightColor
                                    )
                                )
                            ) {
                                Text("Подтвердить", color = colorResource(R.color.textColor))
                            }
                        }
                    }
                }
            }
        )
    } else {
        Column(
            Modifier.background(colorResource(R.color.backgroundColor)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                Modifier
                    .height(80.dp)
                    .background(colorResource(R.color.cardDarkColor))
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.cardDarkColor)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            "Вернуться назад",
                            Modifier.clickable {
                                Date.clearDate()
                                goToMainWindow = true },
                            tint = Color.White
                        )
                        Text(
                            "Добавление операции",
                            Modifier.padding(start = 90.dp),
                            fontSize = 15.sp,
                            color = colorResource(R.color.textColor)
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        category.forEach { category ->
                            Text(
                                category,
                                Modifier
                                    .clickable { selectedCategory = category }
                                    .width(130.dp),
                                fontSize = 30.sp,
                                textAlign = TextAlign.Center,
                                color = if (selectedCategory == category) colorResource(R.color.textColor) else colorResource(
                                    R.color.tryTextColor
                                )
                            )
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        category.forEach { category ->
                            Box(
                                Modifier
                                    .height(3.dp)
                                    .background(
                                        if (selectedCategory == category) colorResource(R.color.boxColor) else colorResource(
                                            R.color.cardDarkColor
                                        )
                                    )
                                    .width(130.dp)
                            )
                        }
                    }

                }
            }

            Row {
                TextField(
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "0",
                            Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            color = colorResource(R.color.textColor)
                        )
                    },
                    value = amountMoney,
                    onValueChange = { text ->
                        if (!(text == "0" || text.startsWith("0"))) {
                            amountMoney = text.filter { it.isDigit() }
                        }
                    },
                    modifier = Modifier.width(150.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedIndicatorColor = colorResource(R.color.textColor),
                        focusedIndicatorColor = colorResource(R.color.boxColor),
                    ),
                    textStyle = TextStyle(
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.textColor)
                    )
                )
            }
            Card(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 30.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomEnd = 20.dp,
                            bottomStart = 20.dp
                        )
                    )
            )
            {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.cardLightColor)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Категории:", color = colorResource(R.color.textColor))
                    val expenseCategory = db.getExpenseCategories(userLogin)
                    val incomeCategory = db.getIncomeCategories(userLogin)

                    when (selectedCategory) {
                        "Расходы" -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                contentPadding = PaddingValues(5.dp),
                            ) {
                                items(expenseCategory) { category ->
                                    StyleExpensesCategory(category, selectedCategories)
                                }
                            }
                        }

                        "Доходы" -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                contentPadding = PaddingValues(5.dp),
                            ) {
                                items(incomeCategory) { category ->
                                    StyleIncomeCategory(category, selectedCategories)
                                }
                            }
                        }
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { goToCalendarWindow = true },
                        Modifier
                            .background(
                                colorResource(R.color.iconButtonColor),
                                shape = CircleShape
                            )
                    ) {
                        Icon(Icons.Default.DateRange, "Дата", tint = Color.Black)
                    }

                    Text("Дата: ${Date.getDate()}", color = colorResource(R.color.textColor))
                    IconButton(
                        onClick = { goToAddCategoryWindow = true },
                        Modifier
                            .background(
                                colorResource(R.color.iconButtonColor),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            "Добавить категорию",
                            tint = Color.Black
                        )
                    }
                }

                Text("Комментарий:", color = colorResource(R.color.textColor))
                TextField(
                    placeholder = {
                        Text(text = "Комментарий",
                            Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            color = colorResource(R.color.textColor)) },
                    value = comment,
                    onValueChange = { text -> comment = text },
                    modifier = Modifier
                        .width(350.dp)
                        .fillMaxHeight(0.4f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedIndicatorColor = colorResource(R.color.textColor),
                        focusedIndicatorColor = colorResource(R.color.boxColor),
                    ),
                    textStyle = TextStyle(
                        fontSize = 30.sp,
                        color = colorResource(R.color.textColor)
                    )
                )
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (amountMoney.isEmpty() || selectedCategories.value == null) {
                                Toast.makeText(context, "Не указана сумма или не выбрана категория", Toast.LENGTH_LONG).show()
                            } else {
                                if (userLogin != null) {
                                    when (selectedCategory) {
                                        "Расходы" -> {
                                            db.addExpense(
                                                selectedCategories.value!!.category,
                                                amountMoney,
                                                selectedCategories.value!!.colorCategory,
                                                comment,
                                                Date.getDate(),
                                                userLogin
                                            )
                                        }
                                        "Доходы" -> {
                                            db.addIncome(
                                                selectedCategories.value!!.category,
                                                amountMoney,
                                                selectedCategories.value!!.colorCategory,
                                                comment,
                                                Date.getDate(),
                                                userLogin
                                            )

                                        }
                                    }
                                    Toast.makeText(context, "Категория ${selectedCategories.value!!.category} добавлена", Toast.LENGTH_LONG).show()
                                    Date.clearDate()
                                    goToMainWindow = true
                                }
                            }
                        },
                        Modifier.width(300.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.cardLightColor)
                        )
                    ) {
                        Text(text = "Добавить", color = colorResource(R.color.textColor))
                    }
                }
            }
        }
    }
}

@Composable
fun StyleCategory(category: Category, selectedCategories: MutableState<Category?>, backgroundColor: Int) {
    val isSelected = selectedCategories.value == category

    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(50.dp)
            .clickable { selectedCategories.value = category }
            .clip(
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 2.dp,
                color = if (isSelected) Color.Black else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(backgroundColor),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.category,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = colorResource(R.color.textColor)
            )
        }
    }
}

@Composable
fun StyleExpensesCategory(category: Category, selectedCategories: MutableState<Category?>) {
    val backgroundColor = getBackgroundColor(category.colorCategory)
    StyleCategory(category, selectedCategories, backgroundColor)
}

@Composable
fun StyleIncomeCategory(category: Category, selectedCategories: MutableState<Category?>) {
    val backgroundColor = getBackgroundColor(category.colorCategory)
    StyleCategory(category, selectedCategories, backgroundColor)
}

private fun getBackgroundColor(color: String): Int {
    return when (color) {
        "red" -> R.color.red
        "yellow" -> R.color.yellow
        "blue" -> R.color.blue
        "green" -> R.color.green
        "pink" -> R.color.pink
        "purple" -> R.color.purple
        "orange" -> R.color.orange
        else -> R.color.transparent
    }
}

fun getDaysInMonth(year: Int, month: Int): Int {
    val calendar = java.util.Calendar.getInstance()
    calendar.set(year, month, 1)
    calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
    return calendar.get(java.util.Calendar.DAY_OF_MONTH)
}

fun getMonthName(month: Int): String {
    val monthNames = arrayOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")
    return monthNames[month]
}