package com.ptit.feature.screen.admin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ptit.feature.navigation.Screen
import com.ptit.feature.viewmodel.AdminViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.adminNavGraph(
    navController: NavController,
    adminViewModel: AdminViewModel
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
            },
            adminViewModel = adminViewModel
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
            },
            adminViewModel = adminViewModel
        )
    }
    composable<Screen.ManageAuthor> {
        ManageAuthorScreen(
            navigateBack = {navController.navigateUp()},
            adminViewModel = adminViewModel
        )
    }
    composable<Screen.ManageBook> {
        ManageBookScreen(
            navigateBack = {navController.navigateUp()},
            adminViewModel = adminViewModel
        )
    }
    composable<Screen.ManageCategory> {
        ManageCategoryScreen(
            navigateBack = {navController.navigateUp()},
            adminViewModel = adminViewModel
        )
    }
    composable<Screen.ManagePermission> {
        ManagePermissionScreen(
            navigateBack = {navController.navigateUp()},
            adminViewModel = adminViewModel
        )
    }
    composable<Screen.ManageCart> {

    }
    composable<Screen.ManagePublisher> {
        ManagePublisherScreen(
            navigateBack = {navController.navigateUp()},
            adminViewModel = adminViewModel
        )
    }
}