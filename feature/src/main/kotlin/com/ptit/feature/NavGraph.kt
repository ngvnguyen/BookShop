package com.ptit.feature

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ptit.feature.screen.AuthScreen
import com.ptit.feature.screen.ForgotPasswordScreen
import com.ptit.feature.screen.HomeGraphScreen
import com.ptit.feature.screen.ResetPasswordScreen
import com.ptit.feature.navigation.Screen
import com.ptit.feature.screen.ProfileScreen
import com.ptit.feature.screen.admin.adminNavGraph

@Composable
fun NavGraph(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Auth,
        modifier = modifier.fillMaxSize()
    ){
        composable<Screen.Auth>{
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph){
                        popUpTo(Screen.Auth){
                            inclusive = true
                        }
                    }
                },
                navigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword)
                }
            )
        }
        composable<Screen.HomeGraph>{
            HomeGraphScreen(
                navigateToAuthScreen = {
                    navController.navigate(Screen.Auth){
                        popUpTo(Screen.HomeGraph) {
                            inclusive = true
                        }
                    }
                },
                navigateToAdminScreen = {
                    navController.navigate(Screen.Admin)
                },
                navigateToProfileScreen = {
                    navController.navigate(Screen.Profile)
                }
            )
        }
        composable<Screen.ForgotPassword> {
            ForgotPasswordScreen(
                navigateToAuth = {
                    navController.navigateUp()
                }  ,
                navigateToOTP = {email->
                    navController.navigate(Screen.ResetPassword(email))
                }
            )
        }
        composable<Screen.ResetPassword> {
            ResetPasswordScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToAuth = {
                    navController.navigate(Screen.Auth){
                        popUpTo(Screen.Auth)
                    }
                }
            )
        }
        composable<Screen.Profile> {
            ProfileScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
        adminNavGraph(navController)
    }
}