package com.ptit.feature.screen.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.feature.component.ManageAdminItem
import com.ptit.feature.permission.Permission
import com.ptit.feature.viewmodel.AdminViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    modifier: Modifier = Modifier,
    navigateBack:()->Unit,
    navigateToManageUser:()->Unit,
    navigateToManageRole:()->Unit,
    navigateToManageCategory:()->Unit,
    navigateToManageAuthor:()->Unit,
    navigateToManageBook:()->Unit,
    navigateToManageCart:()->Unit,
    navigateToManagePermission:()->Unit,
    navigateToManagePublisher:()->Unit
){
    val adminViewModel = koinViewModel<AdminViewModel>()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Admin Panel",
                        fontSize = FontSize.MEDIUM
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "back",
                            tint = IconSecondary
                        )
                    }
                }
            )
        }
    ) { padding->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            adminViewModel.rolePermission.value.forEach { permission->
                ManageAdminItem(
                    permission = permission,
                    onClick = {module->
                        when(module){
                            Permission.Module.User ->{
                                navigateToManageUser()
                            }
                            Permission.Module.Role -> {
                                navigateToManageRole()
                            }
                            Permission.Module.Category -> {
                                navigateToManageCategory()

                            }
                            Permission.Module.Author -> {
                                navigateToManageAuthor()

                            }
                            Permission.Module.Publisher -> {
                                navigateToManagePublisher()

                            }
                            Permission.Module.Book -> {
                                navigateToManageBook()
                            }
                            Permission.Module.Cart -> {
                                navigateToManageCart()

                            }
                            Permission.Module.Permission -> {
                                navigateToManagePermission()
                            }
                        }
                    }
                )
            }
        }
    }
}