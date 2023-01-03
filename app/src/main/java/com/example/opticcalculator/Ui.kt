package com.example.opticcalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Ui() {
    val refraction = remember{mutableStateOf("")}
    val calculatedDiameter = remember{mutableStateOf("")}
    val index = remember{mutableStateOf("")}
    val basicCurved = remember{mutableStateOf("")}
    val nominalThickness = remember{mutableStateOf("")}
    val diameter = remember{mutableStateOf("")}
    val thicknessCenter = remember{mutableStateOf("Толщина по центру ...")}
    val thicknessEdge = remember{mutableStateOf("Толщина по краю ...")}
    val argsForCalc = mapOf<String, MutableState<String>>(
        "refraction" to refraction, "index" to index,
        "basicCurved" to basicCurved,
        "nominalThickness" to nominalThickness,
        "diameter" to diameter, "calculatedDiameter" to calculatedDiameter,
        "thicknessCenter" to thicknessCenter, "thicknessEdge" to thicknessEdge)

    Column(modifier = Modifier
        .fillMaxSize(1f)
        .background(Color.White)) {
        //Рассчетные параметры заголовок
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 20.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Рассчетные параметры")
        }
        //Рассчетные параметры значения:
        //Рефракция
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 10.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = refraction.value,
                textStyle = TextStyle(fontSize=15.sp),
                onValueChange = { refraction.value = it},
                label = { Text ( text = "Рефракция" ) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") }
            )
        }
        //Рассчетный диаметр
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 0.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = calculatedDiameter.value,
                textStyle = TextStyle(fontSize = 15.sp),
                onValueChange = { calculatedDiameter.value = it },
                label = { Text(text = "Рассчетный диаметр") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") })
        }
        //Параметры заготовки заголовок
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 20.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Параметры заготовки")
        }
        //Параметры заготовки значения:
        //Индекс
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 10.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = index.value,
                textStyle = TextStyle(fontSize = 15.sp),
                onValueChange = { index.value = it },
                label = { Text(text = "Индекс") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") })
        }
        //БК
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 0.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = basicCurved.value,
                textStyle = TextStyle(fontSize = 15.sp),
                onValueChange = { basicCurved.value = it },
                label = { Text(text = "Базовая кривизна") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") })
        }
        //Номинальная толщина
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 0.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = nominalThickness.value,
                textStyle = TextStyle(fontSize = 15.sp),
                onValueChange = { nominalThickness.value = it },
                label = { Text(text = "Номинальная толщина") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") })
        }
        //Диаметр
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 0.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = diameter.value,
                textStyle = TextStyle(fontSize = 15.sp),
                onValueChange = { diameter.value = it },
                label = { Text(text = "Диаметр") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") })
        }
        //Кнопка "результат"
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 10.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Button(onClick = {
                calculateET(argsForCalc) },
                shape = RoundedCornerShape(100),
                enabled = refraction.value.isNotEmpty()) {
                Text(text = "Результат")
            }
        }
        //Толщина по центру
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 10.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = thicknessCenter.value, fontSize = 20.sp)
        }
        //Толщина по краю
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 10.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = thicknessEdge.value, fontSize = 20.sp)
        }



    }
}