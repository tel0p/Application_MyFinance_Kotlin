package com.example.myfinance.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinance.R
import com.example.myfinance.db.DBHelper
import com.example.myfinance.db.FileClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryWindow() {
    val context = LocalContext.current
    val db = DBHelper(context)
    val file = FileClass().readLoginFromFile(context)
    var error by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Расходы") }
    var nameCategory by remember { mutableStateOf("") }
    val category = listOf("Расходы", "Доходы")
    var goToAddOperationWindow by remember { mutableStateOf(false) }
    val colors = listOf(
        R.color.red,
        R.color.yellow,
        R.color.blue,
        R.color.green,
        R.color.pink,
        R.color.purple,
        R.color.orange
    )
    var currentColor by remember { mutableStateOf("transparent") }
    var selectedColor by remember { mutableStateOf("") }

    if (goToAddOperationWindow) {
        Intent(LocalContext.current, AddOperationWindow()::class.java).action
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
                            Modifier.clickable { goToAddOperationWindow = true },
                            tint = Color.White
                        )
                        Text(
                            "Добавление категории",
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
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Введите название категории",
                            Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = colorResource(R.color.textColor)
                        )
                    },
                    value = nameCategory.trimStart(),
                    onValueChange = { text ->
                        nameCategory = text
                        error =
                            if (text.trimStart().length > 20) "Название не должно превышать 20 символов" else ""
                    },
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .width(350.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = colorResource(R.color.cardLightColor),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.textColor)
                    ),
                    shape = CircleShape
                )
                Text(
                    text = error,
                    color = if (error.isNotEmpty()) Color.Red else Color.Transparent,
                    fontSize = 10.sp,
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colors.forEach { color ->
                        val isSelected = selectedColor == color.toString()

                        Box(
                            Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .clip(shape = RoundedCornerShape(32.dp))
                                .background(color = colorResource(color))

                                .clickable {
                                    selectedColor = color.toString()
                                    when (color) {
                                        R.color.red -> currentColor = "red"
                                        R.color.yellow -> currentColor = "yellow"
                                        R.color.blue -> currentColor = "blue"
                                        R.color.green -> currentColor = "green"
                                        R.color.pink -> currentColor = "pink"
                                        R.color.purple -> currentColor = "purple"
                                        R.color.orange -> currentColor = "orange"
                                    }
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isSelected) Color.Black else Color.Transparent,
                                    shape = RoundedCornerShape(32.dp)
                                )
                        )
                    }
                }
                Button(
                    onClick = {
                        if (nameCategory == "" || currentColor == "transparent") {
                            Toast.makeText(
                                context,
                                "Название категории не введено или не выбран цвет",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            if (error.isEmpty()) {
                                when (selectedCategory) {
                                    "Расходы" -> {
                                        db.addExpenseCategory(nameCategory, currentColor, file)
                                    }

                                    "Доходы" -> {
                                        db.addIncomeCategory(nameCategory, currentColor, file)
                                    }
                                }
                                Toast.makeText(
                                    context,
                                    "Категория ${nameCategory} создана",
                                    Toast.LENGTH_LONG
                                ).show()
                                goToAddOperationWindow = true
                            } else {
                                Toast.makeText(
                                    context,
                                    "Исправьте ошибки в форме",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    Modifier.padding(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(
                            R.color.cardLightColor
                        )
                    )
                ) {
                    Text(
                        "Создать категорию",
                        fontSize = 30.sp,
                        color = colorResource(R.color.textColor)
                    )
                }
            }
        }
    }
}