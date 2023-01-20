package com.example.opticcalculator

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.*
import kotlin.math.pow

class MainViewModel (application: Application): AndroidViewModel(application) {
    val context = application

    val indexList = mutableListOf("1.5", "1.53", "1.56", "1.59", "1.6",
        "1.61", "1.67", "1.7", "1.74", "1.76", "1.8", "1.9")

    val arguments: MutableLiveData<Map<String, MutableState<String>>> by lazy {
        MutableLiveData<Map<String, MutableState<String>>>()
    }
    fun calculate(context: Context, args:Map<String, MutableState<String>>,  openDialog: MutableState<Boolean>) {

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

        if (refraction >= 0){
            thicknessCenter.value = "${round(nominalThickness + firstCurvature - secondCurvature)}"
            thicknessEdge.value = "${round(nominalThickness + firstCurvature - secondCurvature - altFirstCurvature + altSecondCurvature)}"
        }
        else{
            thicknessCenter.value = "${round(nominalThickness)}"
            thicknessEdge.value = "${round(nominalThickness - firstCurvature + secondCurvature)}"
        }
    }
    fun compareCalculate(viewModel: MainViewModel, indexOL:String,
                         compareThicknessCenter: MutableState<String>,
                         compareThicknessEdge: MutableState<String>,
                         compareCalculatedDiameter: MutableState<String>,
                         compareDiameter: MutableState<String>,
                         type:Boolean = true){
        val index = indexOL.toDouble()
        val basicCurved = viewModel.arguments.value!!["basicCurved"]!!.value.toDouble()
        val refraction = viewModel.arguments.value!!["refraction"]!!.value.toDouble()
        val diameter = viewModel.arguments.value!!["diameter"]!!.value.toDouble()
        val nominalThickness = viewModel.arguments.value!!["nominalThickness"]!!.value.toDouble()
        val calculatedDiameter = viewModel.arguments.value!!["calculatedDiameter"]!!.value.toDouble()

        val fBC = round((index-1)*1000/basicCurved)
        val sBC = round((index-1)*1000/(basicCurved-refraction))

        if (fBC < diameter/2 || sBC < diameter/2){
            if (fBC >= sBC){ compareDiameter.value = (sBC*2).toString()}
            else {compareDiameter.value = (fBC*2).toString()}
            Toast.makeText(context, "При данных параметрах диаметр уменьшен до максимально возможного", Toast.LENGTH_LONG)
                .show()
        }
        if (fBC < calculatedDiameter/2 || sBC < calculatedDiameter/2){
            if (fBC >= sBC){compareCalculatedDiameter.value = (sBC*2).toString()}
            else {compareCalculatedDiameter.value = (fBC*2).toString()}

        }

        val firstCurvature =
            fBC - round(((round(fBC.pow(2)))-(round(0.25*(compareDiameter.value.toDouble().pow(2))))).pow(1/2.toDouble()))

        val secondCurvature =
            sBC - round(((round(sBC.pow(2)))-(round(0.25*(compareDiameter.value.toDouble().pow(2))))).pow(1/2.toDouble()))

        val altFirstCurvature =
            fBC - round(((round(fBC.pow(2)))-(round(0.25*(compareCalculatedDiameter.value.toDouble().pow(2))))).pow(1/2.toDouble()))
        val altSecondCurvature =
            sBC - round(((round(sBC.pow(2)))-(round(0.25*(compareCalculatedDiameter.value.toDouble().pow(2))))).pow(1/2.toDouble()))

        if (refraction >= 0){
            compareThicknessCenter.value = "${round(nominalThickness + firstCurvature - secondCurvature)}"
            compareThicknessEdge.value = "${round(nominalThickness + firstCurvature - secondCurvature - altFirstCurvature + altSecondCurvature)}"
        }
        else{
            compareThicknessCenter.value = "${round(nominalThickness)}"
            compareThicknessEdge.value = "${round(nominalThickness - firstCurvature + secondCurvature)}"
        }
    }
}

class MainViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(application = application) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }
}