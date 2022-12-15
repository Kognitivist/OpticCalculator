package com.example.opticcalculator

import kotlin.math.pow

fun calculateCT(refraction:Double, calculatedDiameter:Double,
              index:Double, basicCurved:Double,
              nominalThickness:Double, diameter:Double): Double {
    val firstBasicCurved_mm = (index-1)*1000/basicCurved
    val secondBasicCurved_mm = (index-1)*1000/(basicCurved-refraction)
    val firstCurvature = firstBasicCurved_mm - (firstBasicCurved_mm.pow(2)-0.25*diameter.pow(2)).pow(1/2)
    val secondCurvature = secondBasicCurved_mm - (secondBasicCurved_mm.pow(2)-0.25*diameter.pow(2)).pow(1/2)
    val altFirstCurvature = firstBasicCurved_mm - (firstBasicCurved_mm.pow(2)-0.25*calculatedDiameter.pow(2)).pow(1/2)
    val altSecondCurvature = secondBasicCurved_mm - (secondBasicCurved_mm.pow(2)-0.25*calculatedDiameter.pow(2)).pow(1/2)

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
    val firstBasicCurved_mm = (index-1)*1000/basicCurved
    val secondBasicCurved_mm = (index-1)*1000/(basicCurved-refraction)
    val firstCurvature = firstBasicCurved_mm - (firstBasicCurved_mm.pow(2)-0.25*diameter.pow(2)).pow(1/2)
    val secondCurvature = secondBasicCurved_mm - (secondBasicCurved_mm.pow(2)-0.25*diameter.pow(2)).pow(1/2)
    val altFirstCurvature = firstBasicCurved_mm - (firstBasicCurved_mm.pow(2)-0.25*calculatedDiameter.pow(2)).pow(1/2)
    val altSecondCurvature = secondBasicCurved_mm - (secondBasicCurved_mm.pow(2)-0.25*calculatedDiameter.pow(2)).pow(1/2)

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