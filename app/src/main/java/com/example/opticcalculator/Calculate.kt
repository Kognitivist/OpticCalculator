package com.example.opticcalculator

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.example.opticcalculator.ui.theme.round
import kotlin.math.pow

fun calculate(args:Map<String, MutableState<String>>, context: Context) {



    val index = args["index"]!!.value.toDouble()
    val basicCurved = args["basicCurved"]!!.value.toDouble()
    val refraction = args["refraction"]!!.value.toDouble()
    var diameter = args["diameter"]!!.value.toDouble()
    val nominalThickness = args["nominalThickness"]!!.value.toDouble()
    var calculatedDiameter = args["calculatedDiameter"]!!.value.toDouble()
    val thicknessCenter = args["thicknessCenter"]!!
    val thicknessEdge = args["thicknessEdge"]!!

    val fBC = round((index-1)*1000/basicCurved)
    val sBC = round((index-1)*1000/(basicCurved-refraction))

    if (fBC < diameter/2 || sBC < diameter/2){
        if (fBC >= sBC){
            args["diameter"]!!.value = (sBC*2).toString()
            diameter = sBC*2 }
        else {args["diameter"]!!.value = (fBC*2).toString()
            diameter = fBC*2}
        Toast.makeText(context, "При данных параметрах диаметр уменьшен до максимально возможного", Toast.LENGTH_LONG)
            .show()
    }
    if (fBC < calculatedDiameter/2 || sBC < calculatedDiameter/2){
        if (fBC >= sBC){
            args["calculatedDiameter"]!!.value = (sBC*2).toString()
            calculatedDiameter = sBC*2}
        else {args["calculatedDiameter"]!!.value = (fBC*2).toString()
            calculatedDiameter = fBC*2}
    }



    val firstCurvature =
        fBC - round(((round(fBC.pow(2)))-(round(0.25*(diameter.pow(2))))).pow(1/2.toDouble()))

    val secondCurvature =
        sBC - round(((round(sBC.pow(2)))-(round(0.25*(diameter.pow(2))))).pow(1/2.toDouble()))

    val altFirstCurvature =
        fBC - round(((round(fBC.pow(2)))-(round(0.25*(calculatedDiameter.pow(2))))).pow(1/2.toDouble()))
    val altSecondCurvature =
        sBC - round(((round(sBC.pow(2)))-(round(0.25*(calculatedDiameter.pow(2))))).pow(1/2.toDouble()))

    if (refraction >= 0){
        thicknessCenter.value = "Толщина по центру = " + round(nominalThickness + firstCurvature - secondCurvature) +" мм"
        thicknessEdge.value = "Толщина по краю = " + round(nominalThickness + firstCurvature - secondCurvature - altFirstCurvature + altSecondCurvature) +" мм"

    }
    else{
        thicknessCenter.value = "Толщина по центру = " + round(nominalThickness) +" мм"
        thicknessEdge.value = "Толщина по краю = " + round(nominalThickness - firstCurvature + secondCurvature) +" мм"


    }
}