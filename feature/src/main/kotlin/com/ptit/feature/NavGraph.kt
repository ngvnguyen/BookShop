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
import com.ptit.feature.screen.BookDetailsScreen
import com.ptit.feature.screen.ChangePasswordScreen
import com.ptit.feature.screen.CheckoutScreen
import com.ptit.feature.screen.OrderScreen
import com.ptit.feature.screen.OrderSuccessScreen
import com.ptit.feature.screen.PickAddressScreen
import com.ptit.feature.screen.PickCouponScreen
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
                },
                navigateToBookDetails = {id->
                    navController.navigate(Screen.BookDetails(id))
                },
                navigateToChangePassword = {
                    navController.navigate(Screen.ChangePassword)
                },
                navigateToPickAddress = {
                    navController.navigate(Screen.PickAddress)
                },
                navigateToPickCoupon = {
                    navController.navigate(Screen.PickCoupon)
                },
                navigateToCheckout = {
                    navController.navigate(Screen.Checkout)
                },
                navigateToOrder = {
                    navController.navigate(Screen.Order)
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
        composable<Screen.ChangePassword> {
            ChangePasswordScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Screen.BookDetails> {
            BookDetailsScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToCheckOut = {

                }
            )
        }
        composable<Screen.PickAddress> {
            PickAddressScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<Screen.PickCoupon> {
            PickCouponScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<Screen.Checkout> {
            CheckoutScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToSuccess = {
                    navController.navigate(Screen.OrderSuccess)
                }
            )
        }
        composable<Screen.OrderSuccess> {
            OrderSuccessScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph){
                        popUpTo(Screen.HomeGraph){
                            inclusive = true
                        }
                    }
                },
                navigateToOrder = {
                    navController.navigate(Screen.Order)
                }
            )
        }
        composable<Screen.Order> {
            OrderScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToOrderDetails = {id->
                    navController.navigate(Screen.OrderDetails(id))
                }
            )
        }
        composable<Screen.OrderDetails> {

        }
        adminNavGraph(navController)
    }
}