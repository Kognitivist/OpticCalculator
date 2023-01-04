package com.example.opticcalculator.ui.theme

fun format(value: String): String {
    return try {
        val x = value.replace(",", ".", true).toDouble()
        value.replace(",", ".", true)

    }
    catch (_: Exception){
        ""
    }

}