package com.example.opticcalculator.ui.theme

import android.widget.Toast
import androidx.compose.runtime.MutableState

fun format(value: String): String {
    return try {
        val x = value.replace(",", ".", true).toDouble()
        value.replace(",", ".", true)

    }
    catch (_: Exception){
        ""
    }
}

