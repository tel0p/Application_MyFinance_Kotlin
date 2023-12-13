package com.example.myfinance.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinance.R
import com.example.myfinance.db.Category
import com.example.myfinance.db.DBHelper
import com.example.myfinance.db.FileClass

@Composable
fun InformationCategoryWindow(selectedCategory: Category?) {
    val context = LocalContext.current
    val db = DBHelper(context)
    val userLogin = FileClass().readLoginFromFile(context)
    var goToMainWindow by remember { mutableStateOf(false) }

    if (goToMainWindow) {
        Intent(LocalContext.current, MainWindow()::class.java).action
    } else {
        Column(
            Modifier.background(colorResource(R.color.backgroundColor)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
            )
            {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    "Вернуться назад",
                    Modifier.clickable { goToMainWindow = true },
                    tint = Color.White
                )
            }

            val categoryDetailsExpense =
                selectedCategory?.let {
                    db.getExpenseInformationForCategory(
                        userLogin,
                        it.category
                    )
                } ?: emptyList()
            val categoryDetailsIncome =
                selectedCategory?.let { db.getIncomeInformationForCategory(userLogin, it.category) }
                    ?: emptyList()

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(5.dp),
            ) {
                items(categoryDetailsExpense) { category ->
                    StyleExpensesInformation(category)
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(5.dp),
            ) {
                items(categoryDetailsIncome) { category ->
                    StyleIncomeInformation(category)
                }
            }
        }
    }
}

@Composable
fun StyleCategoryInformationWindow(category: Category, backgroundColor: Int) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(IntrinsicSize.Min)
            .clip(
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(backgroundColor),
        )
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
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
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.date,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.textColor)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.amountMoney + "₽",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.textColor)
                    )

                }
            }
            if (category.comment.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(5.dp)
                ) {
                    Text(
                        text = category.comment,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.textColor),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun StyleExpensesInformation(category: Category) {
    val backgroundColor = getBackgroundColor(category.colorCategory)
    StyleCategoryInformationWindow(category, backgroundColor)
}

@Composable
fun StyleIncomeInformation(category: Category) {
    val backgroundColor = getBackgroundColor(category.colorCategory)
    StyleCategoryInformationWindow(category, backgroundColor)
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