package com.ptit.feature.screen.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ptit.feature.viewmodel.AdminViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePublisherScreen(
    navigateBack:()->Unit,
    adminViewModel: AdminViewModel
){
    val rolePermission by adminViewModel.rolePermission
    val managePermission by remember {
        derivedStateOf {
            rolePermission.firstOrNull{it.isPublisher()}
        }
    }
    if (managePermission == null) navigateBack()


    val getMethod by remember {
        derivedStateOf {
            managePermission?.data?.any { it.isGet() && it.status.isActive() } ?:false
        }
    }
    val postMethod by remember {
        derivedStateOf {
            managePermission?.data?.any { it.isPost() && it.status.isActive() } ?:false
        }
    }
    val putMethod by remember {
        derivedStateOf {
            managePermission?.data?.any { it.isPut() && it.status.isActive() } ?:false
        }
    }
    val deleteMethod by remember {
        derivedStateOf {
            managePermission?.data?.any { it.isDelete() && it.status.isActive() } ?:false
        }
    }

    Scaffold(
        containerColor = SurfaceDarker,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Publisher",
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
        },
        modifier = Modifier.fillMaxSize()
    ){padding->
        Column(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "This feature is under development!",
                fontSize = FontSize.MEDIUM
            )
        }
    }
}