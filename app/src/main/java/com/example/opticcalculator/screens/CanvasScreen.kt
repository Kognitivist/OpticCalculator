package com.example.opticcalculator.screens

import android.annotation.SuppressLint
import android.util.Log
import android.util.TypedValue
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.opticcalculator.MainViewModel
import com.example.opticcalculator.round
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

    val diameter = remember{
        mutableStateOf("${viewModel.arguments.value!!["calculatedDiameter"]!!.value.toDouble()*convertMMtoDP}")}
    val basicCurved = viewModel.arguments.value!!["basicCurved"]!!.value.toDouble()
    val refraction = viewModel.arguments.value!!["refraction"]!!.value.toDouble()
    val index = viewModel.arguments.value!!["index"]!!.value.toDouble()
    val thicknessCenter = (viewModel.arguments.value!!["thicknessCenter"]!!.value.toFloat())*convertMMtoPX
    val fBC:Float = round((index-1)*1000/basicCurved).toFloat()
    val sBC:Float = round((index-1)*1000/(basicCurved-refraction)).toFloat()

    val rad1 = fBC*convertMMtoPX
    val rad2 = sBC*convertMMtoPX

    val checkedState = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()




    Log.d("Mylog_pxMM", "${convertMMtoPX}")
    Log.d("Mylog_pxDP", "${convertDPtoPX}")
    Log.d("Mylog_width", "${context.resources.displayMetrics.widthPixels}")
    Log.d("Mylog_height", "${context.resources.displayMetrics.heightPixels}")


    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {
                items(viewModel.indexList){
                        n -> Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clickable {
                    }) {
                    Text(text = n, textAlign = TextAlign.Center, fontSize = 20.sp)
                }
                }
            }
        },
        drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
        modifier = Modifier.padding(1.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {
            /** добавить линзу для сравнения switch*/
            Row(modifier = Modifier
                .fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it }
                )
                Text(text = "Добавить линзу для сравнения", fontSize = 18.sp)
            }
            if (checkedState.value){
                /**выбор материала сравнивваемой ОЛ*/
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "дополнительный функционал)))")
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                /** Рассчетная линза*/
                Column(modifier = Modifier
                    .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        modifier = Modifier
                            .width((widthToDP / 2).dp)
                            .height(diameter.value.toDouble().dp)
                    ) {
                        Canvas(modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)){
                            /** окружность передней кривизны */
                            drawCircle(color = Color.Blue,
                                radius = rad1,
                                center = Offset(-rad1+(widthToPX/2)-30f,(diameter.value.toDouble()/2).dp.toPx()),
                                style = Fill,
                            )
                            /** окружность задней кривизны */
                            drawCircle(
                                color = Color.White,
                                radius = rad2,
                                center = Offset(-rad2+(widthToPX/2)-(thicknessCenter)-30f,(diameter.value.toDouble()/2).dp.toPx()),
                                style = Fill,
                            )
                        }
                    }
                    Text(text = "$refraction")
                    Text(text = "Индекс: $index")
                }
                /** Сравниваемая линза*/
                if (checkedState.value){
                    Column(modifier = Modifier
                        .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(
                            modifier = Modifier
                                .width((widthToDP / 2).dp)
                                .height(diameter.value.toDouble().dp)
                        ) {
                            Canvas(modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.White)){
                                /** окружность передней кривизны */
                                drawCircle(color = Color.Blue,
                                    radius = rad1,
                                    center = Offset(-rad1+(widthToPX/2)-30f,(diameter.value.toDouble()/2).dp.toPx()),
                                    style = Fill,
                                )
                                /** окружность задней кривизны */
                                drawCircle(
                                    color = Color.White,
                                    radius = rad2,
                                    center = Offset(-rad2+(widthToPX/2)-(thicknessCenter)-30f,(diameter.value.toDouble()/2).dp.toPx()),
                                    style = Fill,
                                )
                            }
                        }
                        Text(text = "$refraction")
                        Text(text = "Индекс: $index")
                    }
                }
            }
        }
    }

}