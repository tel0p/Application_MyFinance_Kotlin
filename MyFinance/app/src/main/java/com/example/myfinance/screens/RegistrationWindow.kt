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
import com.example.myfinance.db.User
import com.example.myfinance.db.FileClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationWindow() {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var truePassword by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var truePasswordError by remember { mutableStateOf("") }
    var goToMainWindow by remember { mutableStateOf(false) }
    var goToAuthorizationWindow by remember { mutableStateOf(false) }
    val context = LocalContext.current
    if (goToMainWindow) {
        Intent(context, MainWindow()::class.java).action
    } else if (goToAuthorizationWindow) {
        Intent(context, AuthorizationWindow()::class.java).action
    } else {
        FileClass().deleteFile(context)
        Column(
            Modifier.background(colorResource(R.color.backgroundColor)),
            horizontalAlignment = Alignment.CenterHorizontally
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
                        "Регистрация",
                        fontSize = 40.sp,
                        color = colorResource(R.color.textColor)
                    )
                }
            }
            TextField(
                singleLine = true,
                label = { Text(text = "Логин", color = colorResource(R.color.textColor)) },
                placeholder = {
                    Text(
                        text = "Введите логин",
                        color = colorResource(R.color.textColor)
                    )
                },
                value = login.trim(),
                onValueChange = { text ->
                    login = text
                    loginError =
                        if (text.length > 20) "Логин не должен превышать 20 символов" else ""
                },
                modifier = Modifier
                    .padding(bottom = 10.dp, top = 150.dp)
                    .width(350.dp),
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

            Text(
                text = loginError,
                color = if (loginError.isNotEmpty()) Color.Red else Color.Transparent,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 20.dp)
            )

            TextField(
                singleLine = true,
                label = { Text(text = "Пароль", color = colorResource(R.color.textColor)) },
                placeholder = {
                    Text(
                        text = "Введите пароль",
                        color = colorResource(R.color.textColor)
                    )
                },
                value = password.trim(),
                onValueChange = { text ->
                    password = text
                    passwordError =
                        if (text.length < 5) "Пароль должен содержать не менее 5 символов" else ""
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .padding(10.dp)
                    .width(350.dp),
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

            Text(
                text = passwordError,
                color = if (passwordError.isNotEmpty()) Color.Red else Color.Transparent,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 20.dp)
            )

            TextField(
                singleLine = true,
                label = {
                    Text(
                        text = "Подтверждение пароля",
                        color = colorResource(R.color.textColor)
                    )
                },
                placeholder = {
                    Text(
                        text = "Подтвердите пароль",
                        color = colorResource(R.color.textColor)
                    )
                },
                value = truePassword.trim(),
                onValueChange = { text ->
                    truePassword = text
                    truePasswordError =
                        if (text.length < 5) "Пароль должен содержать не менее 5 символов" else ""
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .padding(10.dp)
                    .width(350.dp),
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

            Text(
                text = truePasswordError,
                color = if (truePasswordError.isNotEmpty()) Color.Red else Color.Transparent,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 20.dp)
            )

            Button(
                onClick = {
                    if (loginError.isEmpty() && passwordError.isEmpty() && truePasswordError.isEmpty()) {
                        saveUser(
                            context = context,
                            login = login,
                            pass = password,
                            truePass = truePassword,
                            pin = null,
                            onSuccess = { goToMainWindow = true }
                        ) { message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(context, "Исправьте ошибки в форме", Toast.LENGTH_SHORT)
                            .show()
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
                    "Зарегистрироваться",
                    fontSize = 30.sp,
                    color = colorResource(R.color.textColor)
                )
            }
            Text(
                "Авторизоваться",
                Modifier
                    .padding(top = 100.dp)
                    .clickable { goToAuthorizationWindow = true },
                fontSize = 20.sp,
                color = colorResource(R.color.textColor)
            )
        }
    }
}

fun saveUser(context: Context, login: String, pass: String, truePass: String, pin: String?, onSuccess: () -> Unit, onError: (String) -> Unit) {
    if (login == "" || pass == "" || truePass == "")
        onError("Не все поля заполнены")
    else {
        val db = DBHelper(context)

        // Проверка наличия пользователя с таким логином
        if (db.isLoginExists(login)) {
            onError("Пользователь с логином $login уже существует")
        } else {
            if (pass == truePass) {
                val user = User(login, pass, pin)

                FileClass().saveLoginToFile(context, login)
                db.addUser(user)
                onSuccess()
                Toast.makeText(context, "Пользователь $login добавлен", Toast.LENGTH_LONG).show()
            } else {
                onError("Пароли не совпадают")
            }
        }
    }
}

