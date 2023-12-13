package com.example.myfinance.db

import android.content.Context
import java.io.File
import java.io.IOException

class FileClass {
    fun saveLoginToFile(context: Context, login: String) {
        try {
            val fileOutputStream = context.openFileOutput("loginCurrentUser.txt", Context.MODE_PRIVATE)
            fileOutputStream.write(login.toByteArray())
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readLoginFromFile(context: Context): String? {
        try {
            val fileInputStream = context.openFileInput("loginCurrentUser.txt")
            val stringBuilder = StringBuilder()
            val buffer = ByteArray(1024)
            var bytesRead: Int

            while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                val str = String(buffer, 0, bytesRead)
                stringBuilder.append(str)
            }

            fileInputStream.close()
            return stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun deleteFile(context: Context) {
        val file = File(context.filesDir, "loginCurrentUser.txt")
        file.delete()
    }
}