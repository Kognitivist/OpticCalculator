package com.example.opticcalculator.screens

import android.content.Context
import android.util.Log
import android.util.TypedValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.opticcalculator.MainViewModel
import com.example.opticcalculator.navigation.NavRoute

@Composable
fun CanvasScreen(navController: NavHostController, viewModel: MainViewModel){

    val context = LocalContext.current

    val basicCurved = remember{ mutableStateOf("300") }
    val refraction = remember{ mutableStateOf("300") }
    val pxMM = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_MM, 1f,
        context.resources.displayMetrics
    )
    val pxDP = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 1f,
        context.resources.displayMetrics
    )
    val mm:Float = pxMM/pxDP
    val diameter = remember{ mutableStateOf("${65*mm}") }
    val rad1 = 166.67f*pxMM
    val rad2 = 55.56f*pxMM


    Log.d("Mylog_pxMM", "${pxMM}")
    Log.d("Mylog_pxDP", "${pxDP}")
    Log.d("Mylog_width", "${context.resources.displayMetrics.widthPixels}")
    Log.d("Mylog_height", "${context.resources.displayMetrics.heightPixels}")



    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(diameter.value.toDouble().dp)
                .background(Color.Red)
        ) {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)){
                drawCircle(color = Color.Blue,
                    radius = rad1,
                    center = Offset(-rad1+200.dp.toPx(),(diameter.value.toDouble()/2).dp.toPx()),
                    style = Fill,
                    //blendMode = BlendMode.Plus,
                    alpha = 1f)
                drawCircle(
                    color = Color.White,
                    radius = rad2,
                    center = Offset(-rad2+196.7.dp.toPx(),(diameter.value.toDouble()/2).dp.toPx()),
                    style = Fill,
                    //blendMode = BlendMode.Plus,
                    alpha = 1f
                )
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 10.dp)
            .background(color = Color.White),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Button(onClick = { navController.navigate(route = NavRoute.StartScreen.route) }) {
                Text(text = "Back")
            }
        }
    }
}