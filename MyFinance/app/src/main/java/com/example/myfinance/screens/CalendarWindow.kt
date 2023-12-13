package com.example.myfinance.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import java.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinance.R
import com.example.myfinance.db.Date
import kotlin.math.ceil


@SuppressLint("UnrememberedMutableState")
@Composable
fun CalendarWindow() {
    val calendar = Calendar.getInstance()
    val selectedYear = remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    val selectedMonth = remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    val daysInMonth = getDaysInMonth(selectedYear.value, selectedMonth.value)
    calendar.set(selectedYear.value, selectedMonth.value, 1)
    val firstDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
    val rows = ceil((daysInMonth + firstDayOfWeek) / 7f).toInt()
    val selectedDay = remember { mutableStateOf(0) }
    var goToAddOperationWindow by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (goToAddOperationWindow) {
        Intent(LocalContext.current, AddOperationWindow()::class.java).action
    } else {
        Column(
            Modifier.background(colorResource(R.color.backgroundColor)),
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
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        "Вернуться назад",
                        Modifier.clickable { goToAddOperationWindow = true },
                        tint = Color.White
                    )
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
                                Date.saveDate(selectedDay.value.toString(), (selectedMonth.value + 1).toString(), selectedYear.value.toString())
                                goToAddOperationWindow = true
                                Toast.makeText(context, "Дата: ${selectedDay.value}.${selectedMonth.value + 1}.${selectedYear.value} успешно выбрана", Toast.LENGTH_LONG).show()
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
}

/*fun getDaysInMonth(year: Int, month: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1)
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun getMonthName(month: Int): String {
    val monthNames = arrayOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")
    return monthNames[month]
}*/