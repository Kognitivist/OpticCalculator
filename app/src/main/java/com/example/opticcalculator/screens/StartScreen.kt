package com.example.opticcalculator.screens

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.opticcalculator.*
import com.example.opticcalculator.R
import com.example.opticcalculator.navigation.NavRoute
import com.example.opticcalculator.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun StartScreen(navController: NavHostController, viewModel: MainViewModel, lifecycleOwner: LifecycleOwner) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val openDialog = remember { mutableStateOf(false) }

    val checkedStateBC = remember { mutableStateOf(false) }
    val checkedStateNT = remember { mutableStateOf(false) }

    val refraction = remember{ mutableStateOf("") }
    val calculatedDiameter = remember{ mutableStateOf("") }
    val index = remember{ mutableStateOf("") }
    val basicCurved = remember{ mutableStateOf("") }
    /** Дефолтное значение БК*/
    if (isNumberBool(refraction) &&checkedStateBC.value && !isErrorRefraction(refraction.value)){
        when(refraction.value.toDouble()){
            in 0.0..3.0 -> viewModel.arguments.value!!["basicCurved"]!!.value = "6"
            in 3.01..5.0 -> viewModel.arguments.value!!["basicCurved"]!!.value = "7"
            in 5.01..7.0 -> viewModel.arguments.value!!["basicCurved"]!!.value = "8"
            in 7.01..30.0 -> viewModel.arguments.value!!["basicCurved"]!!.value = "${refraction.value.toDouble()+1}"
            in -3.0..-0.01 -> viewModel.arguments.value!!["basicCurved"]!!.value = "4"
            in -5.0..-3.01 -> viewModel.arguments.value!!["basicCurved"]!!.value = "3"
            in -30.0..-5.01 -> viewModel.arguments.value!!["basicCurved"]!!.value = "2"
            else -> viewModel.arguments.value!!["basicCurved"]!!.value = "error"
        }
    }
    val nominalThickness = remember{ mutableStateOf("") }
    /** Дефолтное значение НТ*/
    if (isNumberBool(refraction) &&checkedStateNT.value && !isErrorRefraction(refraction.value)){
        when(refraction.value.toDouble()){
            in -1.5..1.5 -> viewModel.arguments.value!!["nominalThickness"]!!.value = "1.5"
            else -> viewModel.arguments.value!!["nominalThickness"]!!.value = "1.2"
        }
    }

    val diameter = remember{ mutableStateOf("") }
    val thicknessCenter = remember{ mutableStateOf("") }
    val thicknessEdge = remember{ mutableStateOf("") }

    val argsForCalc = mapOf<String, MutableState<String>>(
        "refraction" to refraction, "index" to index,
        "basicCurved" to basicCurved,
        "nominalThickness" to nominalThickness,
        "diameter" to diameter, "calculatedDiameter" to calculatedDiameter,
        "thicknessCenter" to thicknessCenter, "thicknessEdge" to thicknessEdge)

    viewModel.arguments.observe(lifecycleOwner){
        if (viewModel.arguments.value != null){
            refraction.value = viewModel.arguments.value!!["refraction"]!!.value
            calculatedDiameter.value = viewModel.arguments.value!!["calculatedDiameter"]!!.value
            index.value = viewModel.arguments.value!!["index"]!!.value
            basicCurved.value = viewModel.arguments.value!!["basicCurved"]!!.value
            nominalThickness.value = viewModel.arguments.value!!["nominalThickness"]!!.value
            diameter.value = viewModel.arguments.value!!["diameter"]!!.value
            thicknessCenter.value = viewModel.arguments.value!!["thicknessCenter"]!!.value
            thicknessEdge.value = viewModel.arguments.value!!["thicknessEdge"]!!.value
        }
    }

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
                        viewModel.arguments.value = argsForCalc
                        viewModel.arguments.value!!["index"]!!.value = n
                        viewModel.arguments.value!!["thicknessCenter"]!!.value = ""
                        viewModel.arguments.value!!["thicknessEdge"]!!.value = ""
                        coroutineScope.launch { scaffoldState.drawerState.close() }
                        focusManager.moveFocus(FocusDirection.Down)
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
            .fillMaxSize()
            .background(com.example.opticcalculator.ui.theme.BackgroundColor_1)
    ) {
        Column(modifier = Modifier
            .fillMaxSize(1f)
            .background(com.example.opticcalculator.ui.theme.BackgroundColor_1)) {
            /**Рассчетные параметры заголовок*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Рассчетные параметры", fontWeight = FontWeight.Bold)
            }
            /**Рассчетные параметры значения:*/
            /**Рефракция*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.width(40.dp)){}
                OutlinedTextField(
                    value = refraction.value,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FocusedBorderColor,
                        focusedLabelColor = FocusedLabelColor
                    ),
                    textStyle = TextStyle(fontSize=13.sp),
                    onValueChange = {
                        refraction.value = format(it)
                        viewModel.arguments.value = argsForCalc
                        viewModel.arguments.value!!["thicknessCenter"]!!.value = ""
                        viewModel.arguments.value!!["thicknessEdge"]!!.value = ""},
                    modifier = Modifier.onFocusChanged {
                        isNumber(refraction)
                    },
                    label = { Text ( text = "Рефракция" ) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }),
                    singleLine = true,
                    placeholder = { Text(text = "Введите значение") },
                    isError = isErrorRefraction(refraction.value))
                IconButton(onClick = {
                    Toast.makeText(context, "Преломляющая сила линзы от -15 до +15", Toast.LENGTH_LONG)
                        .show()
                }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_question_mark_24),
                        contentDescription = "")
                }
            }
            /**Рассчетный диаметр*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.width(40.dp)){}
                OutlinedTextField(value = calculatedDiameter.value,
                    textStyle = TextStyle(fontSize = 13.sp),
                    onValueChange = { calculatedDiameter.value = format(it)
                        viewModel.arguments.value = argsForCalc
                        viewModel.arguments.value!!["thicknessCenter"]!!.value = ""
                        viewModel.arguments.value!!["thicknessEdge"]!!.value = ""},
                    modifier = Modifier.onFocusChanged {
                        isNumber(calculatedDiameter)
                    },
                    label = { Text(text = "Рассчетный диаметр") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }),
                    singleLine = true,
                    placeholder = { Text(text = "Введите значение") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FocusedBorderColor,
                        focusedLabelColor = FocusedLabelColor
                    ),
                    isError = isErrorCalculatedDiameter(calculatedDiameter.value))
                IconButton(onClick = {
                    Toast.makeText(context, """Минимальный диаметр линзы проходящей в оправу,
                        |не может быть больше диаметра заготовки (до 90мм)
                    """.trimMargin(),
                        Toast.LENGTH_LONG)
                        .show()
                }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_question_mark_24),
                        contentDescription = "")
                }
            }
            /**Параметры заготовки заголовок*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Параметры заготовки", fontWeight = FontWeight.Bold)

            }
            /**Параметры заготовки значения:*/
            /**Индекс*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.width(40.dp)){}
                OutlinedTextField(value = index.value,
                    textStyle = TextStyle(fontSize = 13.sp),
                    onValueChange = { index.value = format(it)
                        viewModel.arguments.value = argsForCalc
                        viewModel.arguments.value!!["thicknessCenter"]!!.value = ""
                        viewModel.arguments.value!!["thicknessEdge"]!!.value = ""},
                    modifier = Modifier.onFocusChanged {
                        if (it.isFocused){
                            coroutineScope.launch { scaffoldState.drawerState.open() }
                        }
                    },
                    readOnly = true,
                    label = { Text(text = "Индекс") },
                    singleLine = true,
                    placeholder = { Text(text = "Выберите значение") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FocusedBorderColor,
                        focusedLabelColor = FocusedLabelColor
                    ),
                    isError = isErrorIndex(index.value))
                IconButton(onClick = {
                    Toast.makeText(context, "коэффициент преломления материала",
                        Toast.LENGTH_LONG)
                        .show()
                }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_question_mark_24),
                        contentDescription = "")
                }
            }

            /**Диаметр*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.width(40.dp)){}
                OutlinedTextField(
                    value = diameter.value,
                    textStyle = TextStyle(fontSize = 13.sp),
                    onValueChange = { diameter.value = format(it)
                        viewModel.arguments.value = argsForCalc
                        viewModel.arguments.value!!["thicknessCenter"]!!.value = ""
                        viewModel.arguments.value!!["thicknessEdge"]!!.value = ""},
                    modifier = Modifier.onFocusChanged {
                        isNumber(diameter)
                    },
                    label = { Text(text = "Диаметр") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }),
                    singleLine = true,
                    placeholder = { Text(text = "Введите значение") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FocusedBorderColor,
                        focusedLabelColor = FocusedLabelColor
                    ),
                    isError = isErrorDiameter(diameter.value, calculatedDiameter.value)
                )
                IconButton(onClick = {
                    Toast.makeText(context, "Диаметр заказанной заготовки, до 90мм", Toast.LENGTH_LONG)
                        .show()
                }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_question_mark_24),
                        contentDescription = "")
                }
            }
            /**БК*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.width(40.dp)) {
                    Checkbox(
                        checked = checkedStateBC.value,
                        onCheckedChange = { checkedStateBC.value = it },
                        colors  = CheckboxDefaults.colors(
                            checkedColor = TrackColor,
                            checkmarkColor = ButtonColor)
                    )
                }
                OutlinedTextField(
                    value = basicCurved.value,
                    textStyle = TextStyle(fontSize = 13.sp),
                    onValueChange = { basicCurved.value = format(it)
                        viewModel.arguments.value = argsForCalc
                        viewModel.arguments.value!!["thicknessCenter"]!!.value = ""
                        viewModel.arguments.value!!["thicknessEdge"]!!.value = ""},
                    modifier = Modifier.onFocusChanged {
                        isNumber(basicCurved)
                    },
                    label = { Text(text = "Базовая кривизна") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }),
                    singleLine = true,
                    placeholder = { Text(text = "Введите значение") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FocusedBorderColor,
                        focusedLabelColor = FocusedLabelColor
                    ),
                    isError = isErrorBasicCurved(basicCurved.value, refraction.value) )
                IconButton(onClick = {
                    Toast.makeText(context, """Кривизна передней поверхности ОЛ.
                    |Не более 16 диоптрий
                    |Если неизвестно, отметь галочку "значение по умолчанию"
                """.trimMargin(),
                        Toast.LENGTH_LONG)
                        .show()
                }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_question_mark_24),
                        contentDescription = "")
                }

            }
            /**Номинальная толщина*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.width(40.dp)){
                    Checkbox(
                        checked = checkedStateNT.value,
                        onCheckedChange = { checkedStateNT.value = it },
                        colors  = CheckboxDefaults.colors(
                            checkedColor = TrackColor,
                            checkmarkColor = ButtonColor)
                    )
                }
                OutlinedTextField(value = nominalThickness.value,
                    textStyle = TextStyle(fontSize = 13.sp),
                    onValueChange = { nominalThickness.value = format(it)
                        viewModel.arguments.value = argsForCalc
                        viewModel.arguments.value!!["thicknessCenter"]!!.value = ""
                        viewModel.arguments.value!!["thicknessEdge"]!!.value = ""},
                    modifier = Modifier.onFocusChanged {
                        isNumber(nominalThickness)
                    },
                    label = { Text(text = "Номинальная толщина") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {keyboardController?.hide()}),
                    singleLine = true,
                    placeholder = { Text(text = "Введите значение") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FocusedBorderColor,
                        focusedLabelColor = FocusedLabelColor
                    ),
                    isError = isErrorNominalThickness(nominalThickness.value))
                IconButton(onClick = {
                    Toast.makeText(context, """Минимальная толщина заготовки.
                    |От 0 до 3мм
                    |Если неизвестно, ставьте галочку "значение по умолчанию"
                """.trimMargin(),
                        Toast.LENGTH_LONG)
                        .show()
                }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_question_mark_24),
                        contentDescription = "")
                }
            }
            /**Кнопка "результат"*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                Button(onClick = {
                    viewModel.calculate(context ,argsForCalc, openDialog)
                    viewModel.arguments.value = argsForCalc
                    keyboardController?.hide()
                },
                    shape = RoundedCornerShape(20),
                    enabled = !isErrorBasicCurved(basicCurved.value, refraction.value)
                            && !isErrorDiameter(diameter.value, calculatedDiameter.value)
                            && !isErrorIndex(index.value)
                            && !isErrorCalculatedDiameter(calculatedDiameter.value)
                            && !isErrorRefraction(refraction.value)
                            && !isErrorNominalThickness(nominalThickness.value),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor,
                        contentColor = BackgroundColor_1)) {
                    Text(text = "Результат")
                }
            }
            /**Кнопка перехода на экран Canvas*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                Button(onClick = {
                    navController.navigate(route = NavRoute.CanvasScreen.route)
                },
                    shape = RoundedCornerShape(100),
                    enabled = viewModel.arguments.value != null
                            && viewModel.arguments.value!!["thicknessCenter"]!!.value.isNotEmpty()
                            && !isErrorBasicCurved(basicCurved.value, refraction.value)
                            && !isErrorDiameter(diameter.value, calculatedDiameter.value)
                            && !isErrorIndex(index.value)
                            && !isErrorCalculatedDiameter(calculatedDiameter.value)
                            && !isErrorRefraction(refraction.value)
                            && !isErrorNominalThickness(nominalThickness.value)) {
                    Text(text = "Canvas")
                }
            }
            /**Толщина по центру*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Толщина по центру: ")
                Text(text = thicknessCenter.value, fontSize = 20.sp)
            }
            /**Толщина по краю*/
            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Толщина по краю: ")
                Text(text = thicknessEdge.value, fontSize = 20.sp)
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

