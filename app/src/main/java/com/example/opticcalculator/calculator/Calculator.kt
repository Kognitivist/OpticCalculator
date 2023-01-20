package com.example.opticcalculator.calculator

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import com.example.opticcalculator.round
import kotlin.math.pow


class Calculator {


    fun thicknessCenter(context: Context,
                        Refraction: MutableLiveData<String>,
                        CalculatedDiameter: MutableLiveData<String>,
                        Index: MutableLiveData<String>,
                        Diameter: MutableLiveData<String>,
                        BasicCurved: MutableLiveData<String>,
                        NominalThickness: MutableLiveData<String>,
                        openDialog: MutableState<Boolean>): String {

        val refraction = Refraction.value!!.toDouble()
        var calculatedDiameter = CalculatedDiameter.value!!.toDouble()
        val index = Index.value!!.toDouble()
        var diameter = Diameter.value!!.toDouble()
        val basicCurved = BasicCurved.value!!.toDouble()
        val nominalThickness = NominalThickness.value!!.toDouble()


        val fBC = round((index-1)*1000/basicCurved)
        val sBC = round((index-1)*1000/(basicCurved-refraction))

        if (fBC < diameter/2 || sBC < diameter/2){
            if (fBC >= sBC){
                Diameter.value = (sBC*2).toString()
                diameter = sBC*2 }
            else {Diameter.value = (fBC*2).toString()
                diameter = fBC*2}
            Toast.makeText(context, "При данных параметрах диаметр уменьшен до максимально возможного", Toast.LENGTH_LONG)
                .show()
        }
        if (fBC < calculatedDiameter/2 || sBC < calculatedDiameter/2){
            if (fBC >= sBC){
                CalculatedDiameter.value = (sBC*2).toString()
                calculatedDiameter = sBC*2}
            else {CalculatedDiameter.value = (fBC*2).toString()
                calculatedDiameter = fBC*2}
            openDialog.value = true
        }

        val firstCurvature =
            fBC - round(((round(fBC.pow(2)))-(round(0.25*(diameter.pow(2))))).pow(1/2.toDouble()))

        val secondCurvature =
            sBC - round(((round(sBC.pow(2)))-(round(0.25*(diameter.pow(2))))).pow(1/2.toDouble()))

        val altFirstCurvature =
            fBC - round(((round(fBC.pow(2)))-(round(0.25*(calculatedDiameter.pow(2))))).pow(1/2.toDouble()))
        val altSecondCurvature =
            sBC - round(((round(sBC.pow(2)))-(round(0.25*(calculatedDiameter.pow(2))))).pow(1/2.toDouble()))

        return if (refraction >= 0){
            round(nominalThickness + firstCurvature - secondCurvature).toString()
        } else{
            round(nominalThickness).toString()
        }
    }

    fun thicknessEdge(context: Context,
                        Refraction: MutableLiveData<String>,
                        CalculatedDiameter: MutableLiveData<String>,
                        Index: MutableLiveData<String>,
                        Diameter: MutableLiveData<String>,
                        BasicCurved: MutableLiveData<String>,
                        NominalThickness: MutableLiveData<String>,
                        openDialog: MutableState<Boolean>): String{

        val refraction = Refraction.value!!.toDouble()
        var calculatedDiameter = CalculatedDiameter.value!!.toDouble()
        val index = Index.value!!.toDouble()
        var diameter = Diameter.value!!.toDouble()
        val basicCurved = BasicCurved.value!!.toDouble()
        val nominalThickness = NominalThickness.value!!.toDouble()


        val fBC = round((index-1)*1000/basicCurved)
        val sBC = round((index-1)*1000/(basicCurved-refraction))

        if (fBC < diameter/2 || sBC < diameter/2){
            if (fBC >= sBC){
                Diameter.value = (sBC*2).toString()
                diameter = sBC*2 }
            else {Diameter.value = (fBC*2).toString()
                diameter = fBC*2}
            Toast.makeText(context, "При данных параметрах диаметр уменьшен до максимально возможного", Toast.LENGTH_LONG)
                .show()
        }
        if (fBC < calculatedDiameter/2 || sBC < calculatedDiameter/2){
            if (fBC >= sBC){
                CalculatedDiameter.value = (sBC*2).toString()
                calculatedDiameter = sBC*2}
            else {CalculatedDiameter.value = (fBC*2).toString()
                calculatedDiameter = fBC*2}
            openDialog.value = true
        }

        val firstCurvature =
            fBC - round(((round(fBC.pow(2)))-(round(0.25*(diameter.pow(2))))).pow(1/2.toDouble()))

        val secondCurvature =
            sBC - round(((round(sBC.pow(2)))-(round(0.25*(diameter.pow(2))))).pow(1/2.toDouble()))

        val altFirstCurvature =
            fBC - round(((round(fBC.pow(2)))-(round(0.25*(calculatedDiameter.pow(2))))).pow(1/2.toDouble()))
        val altSecondCurvature =
            sBC - round(((round(sBC.pow(2)))-(round(0.25*(calculatedDiameter.pow(2))))).pow(1/2.toDouble()))

        return if (refraction >= 0){
            round(nominalThickness + firstCurvature - secondCurvature - altFirstCurvature + altSecondCurvature).toString()
        } else{
            round(nominalThickness - firstCurvature + secondCurvature).toString()
        }
    }
}