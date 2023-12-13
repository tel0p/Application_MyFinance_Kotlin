package com.example.myfinance.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myfinance.R
import com.example.myfinance.db.DBHelper
import com.example.myfinance.db.FileClass

@Composable
fun SetPinCodeWindow() {
    val pinCode: MutableList<Int> = remember { mutableStateListOf() }
    var goToMainWindow by remember { mutableStateOf(false) }
    val login = FileClass().readLoginFromFile(LocalContext.current)
    if (goToMainWindow) {
        Intent(LocalContext.current, MainWindow()::class.java).action
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(R.color.backgroundColor)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Установка/смена пин-кода",
                Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                softWrap = true,
                color = colorResource(R.color.textColor)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(4) { index ->
                    CircularProgressIndicator(
                        progress = if (pinCode.size >= index + 1) 1.0f else 0.0f,
                        Modifier
                            .size(15.dp),
                        color = colorResource(R.color.tryTextColor),
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp, bottom = 50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(4) {
                    Box(
                        Modifier
                            .size(25.dp, 3.dp)
                            .background(colorResource(R.color.boxColor))
                    )
                }
            }
            val buttons = listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6),
                listOf(7, 8, 9),
                listOf(-1, 0, -2)
            )
            buttons.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    row.forEach { number ->
                        Button(
                            onClick = {
                                if (number >= 0) {
                                    pinCode.add(number)
                                } else if (number == -2 && pinCode.isNotEmpty()) {
                                    pinCode.removeAt(pinCode.lastIndex)
                                } else {
                                    pinCode.clear()
                                }
                            },
                            modifier = if (number == -1) {
                                Modifier.alpha(0f)
                            } else {
                                Modifier
                                    .alpha(1f)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.cardLightColor)
                            )
                        ) {
                            Text(if (number >= 0) number.toString() else if (number == -1) "Х" else "<", color = colorResource(R.color.textColor))
                        }
                    }
                }
            }
            if (pinCode.size == 4) {
                if (login != null) {
                    setPinCode(
                        LocalContext.current,
                        login,
                        pinCode.joinToString(),
                        {goToMainWindow = true}
                    )
                }
            }
        }
    }
}

fun setPinCode(context: Context, login: String, pin: String, onSuccess: () -> Unit){

    val dbHelper = DBHelper(context)
    dbHelper.savePinCode(login, pin)
    onSuccess()
    Toast.makeText(context, "Пинкод успешно установлен, для $login", Toast.LENGTH_LONG).show()

}