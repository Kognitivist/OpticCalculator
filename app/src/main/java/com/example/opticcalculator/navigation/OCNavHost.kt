package com.example.opticcalculator.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.opticcalculator.MainViewModel
import com.example.opticcalculator.screens.StartScreen
import com.example.opticcalculator.screens.CanvasScreen

sealed class NavRoute (val route: String){
    object StartScreen: NavRoute ("START_SCREEN")
    object CanvasScreen: NavRoute ("CANVAS_SCREEN")
}

@Composable
fun OCNavHost(mViewModel: MainViewModel, navController: NavHostController){
    NavHost(navController = navController, startDestination = NavRoute.StartScreen.route){
        composable(NavRoute.StartScreen.route){ StartScreen(navController, viewModel = mViewModel) }
        composable(NavRoute.CanvasScreen.route){ CanvasScreen(navController, viewModel = mViewModel) }
    }

}