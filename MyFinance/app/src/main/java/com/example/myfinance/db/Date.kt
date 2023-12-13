package com.example.myfinance.db

import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Date {
    companion object {
        private var isSaved: Boolean = false
        private lateinit var saveDay: String
        private lateinit var saveMonth: String
        private lateinit var saveYear: String

        fun saveDate(day: String, month: String, year: String) {
            saveDay = day
            saveMonth = month
            saveYear = year
            isSaved = true
        }

        fun getDate(): String {
            return if (isSaved){
                "$saveDay.$saveMonth.$saveYear"
            } else {
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            }
        }

        fun clearDate() {
            saveDay = ""
            saveMonth = ""
            saveYear = ""
            isSaved = false
        }
    }
}