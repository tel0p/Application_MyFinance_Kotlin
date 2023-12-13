package com.example.myfinance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myfinance.ui.theme.MyFinanceTheme
import com.example.myfinance.db.DBHelper
import com.example.myfinance.db.FileClass
import com.example.myfinance.screens.CheckPinCodeWindow
import com.example.myfinance.screens.MainWindow
import com.example.myfinance.screens.RegistrationWindow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFinanceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val db = DBHelper(LocalContext.current)
                    val file = FileClass().readLoginFromFile(LocalContext.current)
                    if (file == null) {
                        RegistrationWindow()
                    } else if (db.checkPinCodeExists(file) != null){
                        CheckPinCodeWindow()
                    } else {
                        MainWindow()
                    }
                }
            }
        }
    }
}
