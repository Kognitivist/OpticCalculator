package com.example.opticcalculator

import android.content.Context
import androidx.compose.runtime.MutableState
import kotlin.math.roundToInt

fun format(value: String): String {
    return try {
        value.replace(",", ".", true)

    }
    catch (_: Exception){
        ""
    }
}

fun round(value: Double): Double {
    return (value * 100.0).roundToInt() / 100.0
}

fun isNumber(mutableState: MutableState<String>){
    try {
        mutableState.value = mutableState.value.toDouble().toString()
    }
    catch (_: Exception){
        if (mutableState.value.isNotEmpty()){mutableState.value = "error"}
    }
}
fun isErrorCalculatedDiameter(value: String):Boolean{
    return try {
        value.isEmpty() || value.toDouble() <= 0
    }
    catch (_: Exception){
        true
    }
}
fun isErrorIndex(value: String):Boolean{
    return try {
        value.isEmpty() || value.toDouble() <= 1 || value.toDouble() > 2
    }
    catch (_: Exception){
        true
    }
}
fun isErrorBasicCurved(value1: String, value2:String):Boolean{
    return try {
        value1.isEmpty() || value1.toDouble() <= value2.toDouble() || value1.toDouble() <= 0
    }
    catch (_: Exception){
        true
    }
}
fun isErrorNominalThickness(value: String):Boolean{
    return try {
        value.isEmpty() || value.toDouble() <= 0
    }
    catch (_: Exception){
        true
    }
}
fun isErrorDiameter(value1: String, value2:String):Boolean{
    return try {
        value1.isEmpty() || value1.toDouble() < value2.toDouble() || value1.toDouble() <= 0
    }
    catch (_: Exception){
        true
    }
}
fun convertPixelsToDp(context: Context, pixels: Float) =
    pixels / context.resources.displayMetrics.density
