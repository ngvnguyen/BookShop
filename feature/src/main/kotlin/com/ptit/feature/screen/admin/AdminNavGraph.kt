package com.ptit.feature.screen.admin

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ptit.feature.navigation.Screen

fun NavGraphBuilder.adminNavGraph(
    navController: NavController
){
    composable<Screen.Admin> {
        AdminScreen(
            navigateBack = {
                navController.navigateUp()
            },
            navigateToManageUser = {
                navController.navigate(Screen.ManageUser)
            },
            navigateToManageRole = {
                navController.navigate(Screen.ManageRole)
            },
            navigateToManageCategory = {
                navController.navigate(Screen.ManageCategory)
            },
            navigateToManageAuthor = {
                navController.navigate(Screen.ManageAuthor)
            },
            navigateToManageBook = {
                navController.navigate(Screen.ManageBook)
            },
            navigateToManageCart = {
                navController.navigate(Screen.ManageCart)
            },
            navigateToManagePermission = {
                navController.navigate(Screen.ManagePermission)
            },
            navigateToManagePublisher = {
                navController.navigate(Screen.ManagePublisher)
            },
        )
    }
    composable<Screen.ManageUser>{
        ManageUserScreen(
            navigateBack = {
                navController.navigateUp()
            },
            navigateToCreateUser = {
                navController.navigate(Screen.ManageUser.CreateUser)
            }
        )
    }
    composable<Screen.ManageUser.CreateUser> {
        UserCreateScreen(
            navigateBack = {
                navController.navigateUp()
            }
        )
    }

    composable<Screen.ManageRole> {
        ManageRoleScreen(
            navigateBack = {
                navController.navigateUp()
            }
        )
    }
    composable<Screen.ManageAuthor> {

    }
    composable<Screen.ManageBook> {
        ManageBookScreen(
            navigateBack = {navController.navigateUp()}
        )
    }
    composable<Screen.ManageCategory> {

    }
    composable<Screen.ManagePermission> {
        ManagePermissionScreen(
            navigateBack = {navController.navigateUp()}
        )
    }
    composable<Screen.ManageCart> {

    }
    composable<Screen.ManagePublisher> {
        
    }
}