package com.example.myfinance.screens

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinance.R
import com.example.myfinance.db.Category
import com.example.myfinance.db.DBHelper
import com.example.myfinance.db.FileClass
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@Composable
fun MainWindow() {
    val context = LocalContext.current
    val db = DBHelper(context)
    val userLogin = FileClass().readLoginFromFile(context)
    var selectedPeriod by remember { mutableStateOf("Месяц") }
    var selectedCategory by remember { mutableStateOf("Расходы") }
    val category = listOf("Расходы", "Доходы")
    val periods = listOf("День", "Неделя", "Месяц", "Год")
    val firstDayOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val lastDayOfWeek = firstDayOfWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    val income = db.getIncome(userLogin, selectedPeriod)
    val expense = db.getExpense(userLogin, selectedPeriod)
    val totalIncome = income.sumOf { it.amountMoney.toIntOrNull() ?: 0 }
    val totalExpense = expense.sumOf { it.amountMoney.toIntOrNull() ?: 0 }
    val totalIncomeString = "$totalIncome ₽"
    val totalExpenseString = "$totalExpense ₽"
    val selectedCategoryForInformation= remember { mutableStateOf<Category?>(null) }
    var goToMenuWindow by remember { mutableStateOf(false) }
    var goToAddOperation by remember { mutableStateOf(false) }
    var goToInformationWindow by remember { mutableStateOf(false) }

    if (goToMenuWindow) {
        Intent(context, MenuWindow()::class.java).action
    } else if (goToAddOperation) {
        Intent(context, AddOperationWindow()::class.java).action
    } else if (goToInformationWindow) {
        Intent(context, InformationCategoryWindow(selectedCategoryForInformation.value)::class.java).action
    } else {
        Column(
            Modifier.background(colorResource(R.color.backgroundColor)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .height(200.dp)
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
                            Icons.Default.Menu,
                            "Меню",
                            Modifier.clickable {

                                goToMenuWindow = true
                            },
                            tint = Color.White
                        )
                        Text(
                            "Итого",
                            Modifier.padding(start = 125.dp),
                            fontSize = 30.sp,
                            color = colorResource(R.color.textColor)
                        )
                    }
                    Text(
                        text = if (selectedCategory == "Расходы") totalExpenseString else totalIncomeString,
                        fontSize = 30.sp,
                        color = colorResource(R.color.textColor)
                    )
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
        }
        Column(
            Modifier
                .padding(top = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                Modifier
                    .width(340.dp)
                    .height(120.dp)
            )
            {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.cardLightColor)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        periods.forEach { period ->
                            Text(
                                period,
                                Modifier
                                    .clickable { selectedPeriod = period }
                                    .width(60.dp),
                                color = if (selectedPeriod == period) colorResource(R.color.tryTextColor) else colorResource(
                                    R.color.textColor
                                ),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        periods.forEach { period ->
                            Box(
                                Modifier
                                    .height(3.dp)
                                    .background(
                                        if (selectedPeriod == period) colorResource(R.color.boxColor) else colorResource(
                                            R.color.cardLightColor
                                        )
                                    )
                                    .width(60.dp)
                            )
                        }
                    }
                    Row(
                        Modifier
                            .height(40.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            when (selectedPeriod) {
                                "День" -> "Сегодня, " + LocalDate.now()
                                    .format(DateTimeFormatter.ofPattern("d MMMM"))

                                "Неделя" -> "${
                                    firstDayOfWeek.format(
                                        DateTimeFormatter.ofPattern(
                                            "d MMMM"
                                        )
                                    )
                                } - ${lastDayOfWeek.format(DateTimeFormatter.ofPattern("d MMMM"))}"

                                "Месяц" -> LocalDate.now()
                                    .format(DateTimeFormatter.ofPattern("LLLL yyyy"))
                                    .replaceFirstChar { it.uppercaseChar() }

                                "Год" -> LocalDate.now()
                                    .format(DateTimeFormatter.ofPattern("yyyy"))
                                else -> ""
                            },
                            color = colorResource(R.color.textColor)
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = { goToAddOperation = true },
                            Modifier
                                .background(
                                    colorResource(R.color.iconButtonColor),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                Icons.Default.Add,
                                "Добавить операцию",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }

        }
        Column(
            Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(top = 300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (income.isNotEmpty() || expense.isNotEmpty()) {
                when (selectedCategory) {
                    "Расходы" -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            contentPadding = PaddingValues(5.dp),
                        ) {
                            items(expense) { category ->
                                StyleExpenses(category, selectedCategoryForInformation){
                                    goToInformationWindow = true
                                }
                            }
                        }
                    }
                    "Доходы" -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            contentPadding = PaddingValues(5.dp),
                        ) {
                            items(income) { category ->
                                StyleIncome(category, selectedCategoryForInformation){
                                    goToInformationWindow = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StyleCategoryMainWindow(category: Category, selectedCategoryForInformation: MutableState<Category?>, backgroundColor: Int, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(50.dp)
            .clickable {
                selectedCategoryForInformation.value = category
                onClick()
            }
            .clip(
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(backgroundColor),
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.category,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.textColor)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(start = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.amountMoney + "₽",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.textColor)
                )
            }
        }
    }
}

@Composable
fun StyleExpenses(category: Category, selectedCategoryForInformation: MutableState<Category?>, onClick: () -> Unit) {
    val backgroundColor = getBackgroundColor(category.colorCategory)
    StyleCategoryMainWindow(category, selectedCategoryForInformation, backgroundColor, onClick)
}

@Composable
fun StyleIncome(category: Category, selectedCategoryForInformation: MutableState<Category?>, onClick: () -> Unit) {
    val backgroundColor = getBackgroundColor(category.colorCategory)
    StyleCategoryMainWindow(category, selectedCategoryForInformation, backgroundColor, onClick)
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