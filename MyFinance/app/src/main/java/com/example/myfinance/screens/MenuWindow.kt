package com.example.myfinance.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinance.R
import com.example.myfinance.db.FileClass

@Composable
fun MenuWindow(){

    val context = LocalContext.current
    var goToRegistrationWindow by remember { mutableStateOf(false) }
    var goToSetPinCOdeWindow by remember { mutableStateOf(false) }
    var goToMainWindow by remember { mutableStateOf(false) }

    if (goToMainWindow) {
        Intent(LocalContext.current, MainWindow()::class.java).action
    } else if (goToRegistrationWindow) {
        Intent(LocalContext.current, RegistrationWindow()::class.java).action
    } else if (goToSetPinCOdeWindow) {
        Intent(LocalContext.current, SetPinCodeWindow()::class.java).action
    } else {
        Column(
            Modifier.background(colorResource(R.color.backgroundColor)),
            horizontalAlignment = Alignment.CenterHorizontally,
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

                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top)
                    {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            "Вернуться назад",
                            Modifier.clickable { goToMainWindow = true },
                            tint = Color.White
                        )
                    }
                    Column(
                        Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val loginUser = FileClass().readLoginFromFile(context)
                        if (loginUser != null) {
                            Text(
                                loginUser,
                                color = colorResource(R.color.textColor),
                                fontSize = 40.sp
                            )
                        }
                    }

                }
            }
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { goToSetPinCOdeWindow = true },
                    Modifier.padding(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(
                            R.color.cardLightColor
                        )
                    )
                ) {
                    Text(
                        "Установить/сменить пинкод",
                        color = colorResource(R.color.textColor),
                        fontSize = 20.sp
                    )
                }
                Button(
                    onClick = { goToRegistrationWindow = true },
                    Modifier.padding(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(
                            R.color.cardLightColor
                        )
                    )
                ) {
                    Text("Выход", color = colorResource(R.color.textColor), fontSize = 20.sp)
                }
            }
        }
    }
}
