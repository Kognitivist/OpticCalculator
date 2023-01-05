package com.example.opticcalculator

import android.content.Context
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opticcalculator.ui.theme.*



@Composable
fun Ui(context: Context) {
    val focusManager = LocalFocusManager.current
    val checkedState = remember { mutableStateOf(false) }

    val refraction = remember{mutableStateOf("")}
    val calculatedDiameter = remember{mutableStateOf("")}
    val index = remember{mutableStateOf("")}
    val basicCurved = remember{mutableStateOf("")}
    val nominalThickness = remember{mutableStateOf("")}
    val diameter = remember{mutableStateOf("")}

    val thicknessCenter = remember{mutableStateOf("")}
    val thicknessEdge = remember{mutableStateOf("")}

    val argsForCalc = mapOf<String, MutableState<String>>(
        "refraction" to refraction, "index" to index,
        "basicCurved" to basicCurved,
        "nominalThickness" to nominalThickness,
        "diameter" to diameter, "calculatedDiameter" to calculatedDiameter,
        "thicknessCenter" to thicknessCenter, "thicknessEdge" to thicknessEdge)

    val isErrorRefraction: Boolean = refraction.value.isEmpty()
    val isErrorCalculatedDiameter: Boolean = isErrorCalculatedDiameter(calculatedDiameter.value)
    val isErrorIndex: Boolean = isErrorIndex(index.value)
    val isErrorBasicCurved: Boolean = isErrorBasicCurved(basicCurved.value, refraction.value)
    val isErrorNominalThickness: Boolean = isErrorNominalThickness(nominalThickness.value)
    val isErrorDiameter: Boolean = isErrorDiameter(diameter.value, calculatedDiameter.value)



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
            Text(text = "Рассчетные параметры", fontWeight = FontWeight.Bold)
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
                textStyle = TextStyle(fontSize=13.sp),
                onValueChange = { refraction.value = format(it)},
                modifier = Modifier.onFocusChanged {
                    isNumber(refraction)
                },
                label = { Text ( text = "Рефракция" ) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") },
                isError = isErrorRefraction)
        }
        //Рассчетный диаметр
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 0.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = calculatedDiameter.value,
                textStyle = TextStyle(fontSize = 13.sp),
                onValueChange = { calculatedDiameter.value = format(it)},
                modifier = Modifier.onFocusChanged {
                    isNumber(calculatedDiameter)
                },
                label = { Text(text = "Рассчетный диаметр") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") },
                isError = isErrorCalculatedDiameter)
        }
        //Параметры заготовки заголовок
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 20.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Параметры заготовки", fontWeight = FontWeight.Bold)
            /*Switch(checked = checkedState.value, onCheckedChange = { checkedState.value = it })
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_question_mark_24),
                contentDescription = "",
                modifier = Modifier.clickable {})*/
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
                textStyle = TextStyle(fontSize = 13.sp),
                onValueChange = { index.value = format(it) },
                modifier = Modifier.onFocusChanged {
                    isNumber(index)
                },
                label = { Text(text = "Индекс") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") },
                isError = isErrorIndex)
        }
        //Диаметр
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 0.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = diameter.value,
                textStyle = TextStyle(fontSize = 13.sp),
                onValueChange = { diameter.value = format(it) },
                modifier = Modifier.onFocusChanged {
                    isNumber(diameter)
                },
                label = { Text(text = "Диаметр") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") },
                isError = isErrorDiameter
            )
        }
        //БК
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 0.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = basicCurved.value,
                textStyle = TextStyle(fontSize = 13.sp),
                onValueChange = { basicCurved.value = format(it) },
                modifier = Modifier.onFocusChanged {
                    isNumber(basicCurved)
                },
                label = { Text(text = "Базовая кривизна") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") },
                isError = isErrorBasicCurved)
            /*Switch(checked = checkedState.value, onCheckedChange = { checkedState.value = it })*/
            /*Icon(
                painter = painterResource(id = R.drawable.ic_baseline_question_mark_24),
                contentDescription = "",
                modifier = Modifier.clickable {})*/

        }
        //Номинальная толщина
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 0.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = nominalThickness.value,
                textStyle = TextStyle(fontSize = 13.sp),
                onValueChange = { nominalThickness.value = format(it)},
                modifier = Modifier.onFocusChanged {
                    isNumber(nominalThickness)
                },
                label = { Text(text = "Номинальная толщина") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done),
                singleLine = true,
                placeholder = { Text(text = "Введите значение") },
                isError = isErrorNominalThickness)
        }
        //Кнопка "результат"
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 10.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Button(onClick = {
                calculate(argsForCalc, context) },
                shape = RoundedCornerShape(100),
                enabled = !isErrorBasicCurved && !isErrorDiameter && !isErrorIndex
                        && !isErrorCalculatedDiameter && !isErrorRefraction
                        && !isErrorNominalThickness) {
                Text(text = "Результат")
            }
        }
        //Толщина по центру
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 30.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = thicknessCenter.value, fontSize = 20.sp)
        }
        //Толщина по краю
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 20.dp)
            .background(colorResource(id = R.color.white)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = thicknessEdge.value, fontSize = 20.sp)
        }
    }
}