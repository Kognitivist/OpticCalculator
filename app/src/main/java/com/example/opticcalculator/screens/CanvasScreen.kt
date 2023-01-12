package com.example.opticcalculator.screens

import android.annotation.SuppressLint
import android.util.TypedValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.opticcalculator.MainViewModel
import com.example.opticcalculator.round
import com.example.opticcalculator.ui.theme.*
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CanvasScreen(navController: NavHostController, viewModel: MainViewModel, lifecycleOwner: LifecycleOwner){

    val context = LocalContext.current

    val convertMMtoPX = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_MM, 1f,
        context.resources.displayMetrics
    )
    val convertDPtoPX = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 1f,
        context.resources.displayMetrics
    )
    val widthToPX = context.resources.displayMetrics.widthPixels
    val widthToDP = widthToPX*(1/convertDPtoPX)
    val convertMMtoDP:Float = convertMMtoPX/convertDPtoPX

    val calculatedDiameter = remember{
        mutableStateOf("${viewModel.arguments.value!!["calculatedDiameter"]!!.value.toDouble()*convertMMtoDP}")}
    val basicCurved = viewModel.arguments.value!!["basicCurved"]!!.value.toDouble()
    val refraction = viewModel.arguments.value!!["refraction"]!!.value.toDouble()
    val index = viewModel.arguments.value!!["index"]!!.value.toDouble()
    val thicknessCenter = (viewModel.arguments.value!!["thicknessCenter"]!!.value.toFloat())*convertMMtoPX
    val fBC: Float = round((index-1)*1000/basicCurved).toFloat()
    val sBC: Float = round((index-1)*1000/(basicCurved-refraction)).toFloat()

    val compareIndex = remember { mutableStateOf(viewModel.arguments.value!!["index"]!!.value) }
    val compareThicknessCenter = remember { mutableStateOf(viewModel.arguments.value!!["thicknessCenter"]!!.value) }
    val compareThicknessEdge = remember { mutableStateOf(viewModel.arguments.value!!["thicknessEdge"]!!.value) }
    val compareFBC: Float = round((compareIndex.value.toDouble()-1)*1000/basicCurved).toFloat()
    val compareSBC: Float = round((compareIndex.value.toDouble()-1)*1000/(basicCurved-refraction)).toFloat()
    val compareCalculatedDiameter = remember { mutableStateOf(viewModel.arguments.value!!["calculatedDiameter"]!!.value) }
    val compareDiameter = remember { mutableStateOf(viewModel.arguments.value!!["diameter"]!!.value) }

    val compareRad1 = compareFBC*convertMMtoPX
    val compareRad2 = compareSBC*convertMMtoPX

    val rad1 = fBC*convertMMtoPX
    val rad2 = sBC*convertMMtoPX

    val checkedState = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val openDialog = remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor_1),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {
                items(viewModel.indexList){
                        n -> Card(modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(top = 24.dp)
                    .clickable {
                        compareIndex.value = n
                        compareCalculatedDiameter.value =
                            viewModel.arguments.value!!["calculatedDiameter"]!!.value
                        compareDiameter.value = viewModel.arguments.value!!["diameter"]!!.value
                        viewModel.compareCalculate(
                            viewModel,
                            n,
                            compareThicknessCenter,
                            compareThicknessEdge,
                            compareCalculatedDiameter,
                            compareDiameter
                        )
                        coroutineScope.launch { scaffoldState.drawerState.close() }
                        if (compareCalculatedDiameter.value.toDouble() != viewModel.arguments.value!!["calculatedDiameter"]!!.value.toDouble()) {
                            openDialog.value = true
                        }
                    },
                    backgroundColor = BackgroundColor_Card,
                    shape = RoundedCornerShape(20),
                ) {
                    Text(text = n, textAlign = TextAlign.Center,
                        fontSize = 22.sp, color = BackgroundColor_1,
                        modifier = Modifier.padding(5.dp))
                }
                }
            }
        },
        drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
        modifier = Modifier
            .background(BackgroundColor_1)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor_1),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {
            /** добавить линзу для сравнения switch*/
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ButtonColor,
                        checkedTrackColor = TrackColor,)
                )
                Text(text = "Добавить линзу для сравнения", fontSize = 18.sp)
            }
            /**выбор материала сравнивваемой ОЛ*/
            if (checkedState.value){
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    },
                        colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor,
                            contentColor = BackgroundColor_1),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "Выбрать индекс")
                    }
                }
                /*Row(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    RadioButton(
                        selected = state.value,
                        onClick = { state.value = true }
                    )
                    Text(text = "Сферическая ОЛ")
                    RadioButton(
                        selected = !state.value,
                        onClick = { state.value = false }
                    )
                    Text(text = "Асферическая ОЛ")
                }*/
            }
            /** canvas OL*/
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                /** Рассчетная линза*/
                Column(modifier = Modifier
                    .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        modifier = Modifier
                            .width((widthToDP / 2).dp)
                            .height(calculatedDiameter.value.toDouble().dp),
                        elevation = 0.dp
                    ) {
                        Canvas(modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundColor_1)
                        ){
                            /** окружность передней кривизны */
                            drawCircle(color = ColorOfLens,
                                radius = rad1,
                                center = Offset(-rad1+(widthToPX/2)-100f,(calculatedDiameter.value.toDouble()/2).dp.toPx()),
                                style = Fill,
                            )
                            /** окружность задней кривизны */
                            drawCircle(
                                color = BackgroundColor_1,
                                radius = rad2,
                                center = Offset(-rad2+(widthToPX/2)-(thicknessCenter)-100f,(calculatedDiameter.value.toDouble()/2).dp.toPx()),
                                style = Fill,
                            )
                            if (rad2 < (widthToPX/2)-(thicknessCenter)-100f){
                                drawRect(
                                    topLeft = Offset(0f,0f),
                                    size = Size(-rad2+(widthToPX/2)-(thicknessCenter)-100f,calculatedDiameter.value.toDouble().dp.toPx()),
                                    color = BackgroundColor_1
                                )
                            }
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$refraction D", fontWeight = FontWeight.Bold)
                        Text(text = "Индекс: $index", fontWeight = FontWeight.Bold)
                        Text(text = "Рассчетный диаметр: ${viewModel.arguments.value!!["calculatedDiameter"]!!.value} мм", fontWeight = FontWeight.Bold)
                        Text(text = "Толщина центра ${viewModel.arguments.value!!["thicknessCenter"]!!.value} мм", fontWeight = FontWeight.Bold)
                        Text(text = "Толщина края ${viewModel.arguments.value!!["thicknessEdge"]!!.value} мм", fontWeight = FontWeight.Bold)
                    }

                }
                /** Сравниваемая линза*/
                if (checkedState.value){
                    Column(modifier = Modifier
                        .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(
                            modifier = Modifier
                                .width((widthToDP / 2).dp)
                                .height((compareCalculatedDiameter.value.toDouble() * convertMMtoDP).dp),
                            elevation = 0.dp
                        ) {
                            Canvas(modifier = Modifier
                                .fillMaxSize()
                                .background(BackgroundColor_1)){
                                /** окружность передней кривизны */
                                drawCircle(color = ColorOfLens,
                                    radius = compareRad1,
                                    center = Offset(-compareRad1+(widthToPX/2)-100f,
                                        (compareCalculatedDiameter.value.toDouble() * convertMMtoDP/2).dp.toPx()),
                                    style = Fill,
                                )
                                /** окружность задней кривизны */
                                drawCircle(
                                    color = BackgroundColor_1,
                                    radius = compareRad2,
                                    center = Offset( -compareRad2+(widthToPX/2)-(compareThicknessCenter.value.toFloat()*convertMMtoPX)-100f,
                                        (compareCalculatedDiameter.value.toDouble() * convertMMtoDP/2).dp.toPx()),
                                    style = Fill,
                                )
                                if (compareRad2 < (widthToPX/2)-(compareThicknessCenter.value.toFloat()*convertMMtoPX)-100f){
                                    drawRect(
                                        topLeft = Offset(0f,0f),
                                        size = Size((widthToPX/2)-(compareThicknessCenter.value.toFloat()*convertMMtoPX)-100f-compareRad2,calculatedDiameter.value.toDouble().dp.toPx()),
                                        color = BackgroundColor_1
                                    )
                                }

                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$refraction D", fontWeight = FontWeight.Bold)
                            Text(text = "Индекс: ${compareIndex.value}", fontWeight = FontWeight.Bold)
                            Text(text = "Рассчетный диаметр: ${compareCalculatedDiameter.value} мм", fontWeight = FontWeight.Bold)
                            Text(text = "Толщина центра ${compareThicknessCenter.value} мм",
                                fontWeight = FontWeight.Bold,
                                color =
                                if (compareThicknessCenter.value.toDouble() < viewModel.arguments.value!!["thicknessCenter"]!!.value.toDouble()){
                                    TextColorGreen}
                                else if (compareThicknessCenter.value.toDouble() > viewModel.arguments.value!!["thicknessCenter"]!!.value.toDouble()){
                                    TextColorRed}
                                else{ Color.Black}
                            )

                            Text(text = "Толщина края ${compareThicknessEdge.value} мм",
                                fontWeight = FontWeight.Bold,
                                color = if (compareThicknessEdge.value.toDouble() < viewModel.arguments.value!!["thicknessEdge"]!!.value.toDouble()){
                                    TextColorGreen}
                                else if (compareThicknessEdge.value.toDouble() > viewModel.arguments.value!!["thicknessEdge"]!!.value.toDouble()){
                                    TextColorRed}
                                else{ Color.Black}
                            )
                        }

                    }
                }
            }
        }
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = { Text(text = "Внимание!") },
                text = { Text("""При данных параметрах невозможно изготовить линзу в расчетном диаметре.
                    |Линза не пройдет в оправу.
                """.trimMargin()) },
                buttons = {
                    Button(
                        onClick = { openDialog.value = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor,
                            contentColor = BackgroundColor_1),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text("OK", fontSize = 22.sp)
                    }
                }
            )
        }
    }

}