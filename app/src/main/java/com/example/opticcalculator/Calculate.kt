package com.example.opticcalculator

import androidx.compose.runtime.MutableState
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

fun calculate(args:Map<String, MutableState<String>>) {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.UP

    val index = args["index"]!!.value.toDouble()
    val basicCurved = args["basicCurved"]!!.value.toDouble()
    val refraction = args["refraction"]!!.value.toDouble()
    val diameter = args["diameter"]!!.value.toDouble()
    val nominalThickness = args["nominalThickness"]!!.value.toDouble()
    val calculatedDiameter = args["calculatedDiameter"]!!.value.toDouble()
    val thicknessCenter = args["thicknessCenter"]!!
    val thicknessEdge = args["thicknessEdge"]!!

    val fBC = df.format((index-1)*1000/basicCurved).toDouble()
    val sBC = df.format((index-1)*1000/(basicCurved-refraction)).toDouble()


    val firstCurvature =
        fBC - df.format(((df.format(fBC.pow(2)).toDouble())-(df.format(0.25*(diameter.pow(2))).toDouble())).pow(1/2.toDouble())).toDouble()

    val secondCurvature =
        sBC - df.format(((df.format(sBC.pow(2)).toDouble())-(df.format(0.25*(diameter.pow(2))).toDouble())).pow(1/2.toDouble())).toDouble()

    val altFirstCurvature =
        fBC - df.format(((df.format(fBC.pow(2)).toDouble())-(df.format(0.25*(calculatedDiameter.pow(2))).toDouble())).pow(1/2.toDouble())).toDouble()
    val altSecondCurvature =
        sBC - df.format(((df.format(sBC.pow(2)).toDouble())-(df.format(0.25*(calculatedDiameter.pow(2))).toDouble())).pow(1/2.toDouble())).toDouble()

    if (refraction >= 0){
        thicknessCenter.value = "Толщина по центру = " + df.format(nominalThickness + firstCurvature - secondCurvature) +" мм"
        thicknessEdge.value = "Толщина по краю = " + df.format(nominalThickness + firstCurvature - secondCurvature - altFirstCurvature + altSecondCurvature) +" мм"

    }
    else{
        thicknessCenter.value = "Толщина по центру = " + df.format(nominalThickness) +" мм"
        thicknessEdge.value = "Толщина по краю = " + df.format(nominalThickness - firstCurvature + secondCurvature) +" мм"


    }
}