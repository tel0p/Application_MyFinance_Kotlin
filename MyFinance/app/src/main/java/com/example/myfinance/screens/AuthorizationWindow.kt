package com.example.myfinance.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinance.R
import com.example.myfinance.db.DBHelper
import com.example.myfinance.db.FileClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorizationWindow() {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var goToMainWindow by remember { mutableStateOf(false) }
    var goToRegistrationWindow by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (goToMainWindow) {
        Intent(context, MainWindow()::class.java).action
    } else if (goToRegistrationWindow) {
        Intent(context, RegistrationWindow()::class.java).action
    } else {
        Column(
            Modifier.background(colorResource(R.color.backgroundColor)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                Modifier
                    .height(100.dp)
                    .background(colorResource(R.color.cardDarkColor))
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.cardDarkColor)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Авторизация",
                        fontSize = 40.sp,
                        color = colorResource(R.color.textColor)
                    )
                }
            }
            TextField(
                singleLine = true,
                label = { Text(text = "Логин", color = colorResource(R.color.textColor)) },
                placeholder = { Text(text = "Введите логин", color = colorResource(R.color.textColor)) },
                value = login.trim(),
                onValueChange = { text -> login = text },
                modifier = Modifier.padding(bottom = 10.dp, top = 150.dp).width(350.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colorResource(R.color.cardLightColor),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    color = colorResource(R.color.textColor)
                ),
                shape = CircleShape
            )
            TextField(
                singleLine = true,
                label = { Text(text = "Пароль", color = colorResource(R.color.textColor)) },
                placeholder = { Text(text = "Введите пароль", color = colorResource(R.color.textColor)) },
                value = password.trim(),
                onValueChange = { text -> password = text },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.padding(10.dp).width(350.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colorResource(R.color.cardLightColor),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    color = colorResource(R.color.textColor)
                ),
                shape = CircleShape
            )
            Button(
                onClick = {
                    checkUser(
                        context = context,
                        login = login,
                        pass = password,
                        onSuccess = { goToMainWindow = true },
                        onError = { message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(
                        R.color.cardLightColor
                    )
                )
            ) {
                Text(
                    "Авторизоваться",
                    fontSize = 30.sp,
                    color = colorResource(R.color.textColor)
                )
            }
            Text(
                "Зарегистрироваться",
                Modifier
                    .padding(top = 100.dp)
                    .clickable { goToRegistrationWindow = true },
                fontSize = 20.sp,
                color = colorResource(R.color.textColor)
            )
        }
    }
}

fun checkUser(context: Context, login: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
    if (login == "" || pass == "")
        onError("Не все поля заполнены")
    else {
        val db = DBHelper(context)
        val isAuth = db.getUser(login, pass)
        if (isAuth){
            FileClass().saveLoginToFile(context, login)
            onSuccess()
            Toast.makeText(context, "Пользователь $login авторизован", Toast.LENGTH_LONG).show()
        } else {
            onError("Логин или пароль введены не верно")
        }
    }
}
