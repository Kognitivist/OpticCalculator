package com.example.opticcalculator

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.roundToInt

fun calculateCT(refraction:Double,
              index:Double, basicCurved:Double,
              nominalThickness:Double, diameter:Double): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.DOWN
    val firstBasicCurved_mm = (index-1)*1000/basicCurved
    val secondBasicCurved_mm = (index-1)*1000/(basicCurved-refraction)
    val firstCurvature = firstBasicCurved_mm - (df.format(firstBasicCurved_mm.pow(2)).toDouble()-(df.format(0.25*diameter.pow(2)).toDouble())).pow(1/2)
    val secondCurvature = secondBasicCurved_mm - (df.format(secondBasicCurved_mm.pow(2)).toDouble()-(df.format(0.25*diameter.pow(2)).toDouble())).pow(1/2)

    if (refraction >= 0){
        val thicknessCenter = nominalThickness + firstCurvature - secondCurvature
        return thicknessCenter
    }
    else{
        val thicknessCenter = nominalThickness
        return thicknessCenter
    }
}
fun calculateET(refraction:Double, calculatedDiameter:Double,
                index:Double, basicCurved:Double,
                nominalThickness:Double, diameter:Double): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.DOWN
    val firstBasicCurved_mm = (index-1)*1000/basicCurved
    val secondBasicCurved_mm = (index-1)*1000/(basicCurved-refraction)
    val firstCurvature = firstBasicCurved_mm - (df.format(firstBasicCurved_mm.pow(2)).toDouble()-(df.format(0.25*diameter.pow(2)).toDouble())).pow(1/2)
    val secondCurvature = secondBasicCurved_mm - (df.format(secondBasicCurved_mm.pow(2)).toDouble()-(df.format(0.25*diameter.pow(2)).toDouble())).pow(1/2)
    val altFirstCurvature = firstBasicCurved_mm - (df.format(firstBasicCurved_mm.pow(2)).toDouble()-(df.format(0.25*calculatedDiameter.pow(2)).toDouble())).pow(1/2)
    val altSecondCurvature = secondBasicCurved_mm - (df.format(secondBasicCurved_mm.pow(2)).toDouble()-(df.format(0.25*calculatedDiameter.pow(2)).toDouble())).pow(1/2)

    if (refraction >= 0){
        val thicknessCenter = nominalThickness + firstCurvature - secondCurvature
        val thicknessEdge = thicknessCenter + altFirstCurvature - altSecondCurvature
        return thicknessEdge
    }
    else{
        val thicknessEdge = nominalThickness - firstCurvature + secondCurvature
        return thicknessEdge
    }
}