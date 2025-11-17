package com.ptit.feature.screen.admin

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ptit.data.DisplayResult
import com.ptit.feature.permission.Permission
import com.ptit.feature.viewmodel.Action
import com.ptit.feature.viewmodel.AdminViewModel
import com.ptit.shared.Alpha
import com.ptit.shared.ButtonDisabled
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Orange
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextPrimary
import com.ptit.shared.White
import com.ptit.shared.component.CustomTextField
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePermissionScreen(
    navigateBack:()->Unit,
    adminViewModel: AdminViewModel
) {
    val permissionState by adminViewModel.allPermission.collectAsState()
    var action = adminViewModel.action
    val snackbarHostState = remember { SnackbarHostState() }
    val rolePermission by adminViewModel.rolePermission
    val managePermission by remember {
        derivedStateOf {
            rolePermission.firstOrNull{it.isPermission()}
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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Permission",
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
        floatingActionButton = {

            if (postMethod)
                FloatingActionButton(
                    onClick = {
                        adminViewModel.resetPermissionForm()
                        adminViewModel.setAct(Action.CREATE)
                    },
                    shape = RoundedCornerShape(50.dp),
                    containerColor = Color.White
                ){
                    Icon(
                        painter = painterResource(Resources.Icon.Plus),
                        contentDescription = "Add",
                        tint = IconSecondary,
                    )
                }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { padding ->
        permissionState.DisplayResult(
            onSuccess = {permissions->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        )
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    items(
                        items = permissions
                    ) {permissionList->
                        Text(
                            text = permissionList.module.name,
                            fontSize = FontSize.MEDIUM,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            permissionList.data.forEach { permissionData->
                                PermissionItem(
                                    permissionData = permissionData,
                                    onClick = {
                                        if (putMethod){
                                            adminViewModel.selectPermission(
                                                permissionData = permissionData,
                                                module = permissionList.module
                                            )
                                            adminViewModel.setAct(Action.UPDATE)
                                        }
                                    },
                                    canDelete = deleteMethod,
                                    onDeleteClick = {
                                        adminViewModel.deletePermission(
                                            id = permissionData.id,
                                            onSuccess = {},
                                            onError = {e->
                                                snackbarHostState.showSnackbar(e)
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }
                    item{
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                AnimatedContent(targetState = action) {act->
                    if (!act.isIdle())
                        PermissionAlertDialog(
                            action = act,
                            onDismiss = {adminViewModel.resetAction()},
                            onConfirm = {
                                if (act.isCreate()) {
                                    adminViewModel.createPermission(
                                        onSuccess = {snackbarHostState.showSnackbar("Successfully")},
                                        onError = {e->snackbarHostState.showSnackbar(e)}
                                    )
                                } else {
                                    adminViewModel.updatePermission(
                                        onSuccess = {snackbarHostState.showSnackbar("Successfully")},
                                        onError = {e->snackbarHostState.showSnackbar(e)}
                                    )
                                }
                                adminViewModel.resetAction()
                            },
                            viewModel = adminViewModel
                        )
                }
            },
            onError = {e->
                ErrorCard(
                    modifier = Modifier.fillMaxSize(),
                    message = e
                )
            },
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            }
        )

    }
}

@Composable
fun PermissionItem(
    permissionData: Permission.PermissionData,
    onClick:()->Unit,
    canDelete: Boolean,
    onDeleteClick:()->Unit={}
){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = TextPrimary.copy(alpha = Alpha.TEN_PERCENT),
                shape = RoundedCornerShape(12.dp)
            )
            .background(SurfaceDarker)
            .clickable(onClick=onClick)

    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
        ) {
            Row {
                Text(
                    text = "id : ${permissionData.id}",
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = permissionData.method.name,
                    color = when(permissionData.method){
                        Permission.Method.GET-> Color.Green
                        Permission.Method.POST-> Color.Yellow
                        Permission.Method.PUT-> Color.Blue
                        Permission.Method.DELETE-> Color.Red
                    },
                    fontWeight = FontWeight.W500
                )
            }
            Text(
                text = "Path : ${permissionData.apiPath}"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = permissionData.name,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = permissionData.status.name,
                    color = when(permissionData.status){
                        Permission.Status.ACTIVE-> Color.Green
                        Permission.Status.INACTIVE-> Color.Red
                    },
                    fontWeight = FontWeight.W400
                )
            }
        }
        if (canDelete)
            IconButton(
                onClick=onDeleteClick,
                modifier = Modifier.align(Alignment.TopEnd)

            ) {
                Icon(
                    painter = painterResource(Resources.Icon.Delete),
                    contentDescription = "Delete"
                )
            }
    }



}

@Composable
fun PermissionAlertDialog(
    action: Action,
    onDismiss:()->Unit,
    onConfirm:()->Unit,
    viewModel: AdminViewModel
){
    val permissionForm = viewModel.permissionForm
    AlertDialog(
        shape = RoundedCornerShape(6.dp),
        containerColor = SurfaceLighter,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    disabledContainerColor = ButtonDisabled
                ),
                enabled = viewModel.isPermissionFormValid
            ) {
                Text(
                    text = "Confirm"
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceDarker,
                )
            ) {
                Text(
                    text = "Cancel"
                )
            }
        },
        title = {
            Text(
                text = if (action.isCreate()) "Create Permission" else "Update Permission",
                fontSize = FontSize.MEDIUM
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                CustomTextFieldWithLabel(
                    value = permissionForm.name,
                    onValueChange = {
                        viewModel.updatePermissionForm(name = it)
                    },
                    label = "Permission name"
                )
                CustomTextFieldWithLabel(
                    value = permissionForm.apiPath,
                    onValueChange = {
                        viewModel.updatePermissionForm(apiPath = it)
                    },
                    label = "Api Path"
                )
                permissionForm.createdBy?.let {
                    CustomTextFieldWithLabel(
                        value = it,
                        onValueChange = {},
                        enabled = false,
                        label = "Created by"
                    )
                }
                permissionForm.createdAt?.let {
                    CustomTextFieldWithLabel(
                        value = it,
                        onValueChange = {},
                        enabled = false,
                        label = "Created at"
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var expanded by remember { mutableStateOf(false) }
                    Text(
                        text = "Method"
                    )

                    Box(){
                        Button(
                            onClick = {expanded=true},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceDarker,
                                contentColor = TextPrimary
                            ),
                        ) {
                            Text(permissionForm.method.name)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            Permission.Method.entries.forEach {method->
                                DropdownMenuItem(
                                    text = {Text(method.name)},
                                    onClick = {
                                        viewModel.updatePermissionForm(method = method)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var expanded by remember { mutableStateOf(false) }
                    Text(
                        text = "Module"
                    )

                    Box(){
                        Button(
                            onClick = {expanded=true},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceDarker,
                                contentColor = TextPrimary
                            ),
                        ) {
                            Text(permissionForm.module.name)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            Permission.Module.entries.forEach {module->
                                DropdownMenuItem(
                                    text = {Text(module.name)},
                                    onClick = {
                                        viewModel.updatePermissionForm(module = module)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                if (action==Action.UPDATE){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = "Status"
                        )
                        Button(
                            onClick = {
                                viewModel.updatePermissionForm(
                                    isActive = !permissionForm.isActive
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (permissionForm.isActive) Color.Green else Color.Red,
                                contentColor = Color.White
                            ),
                        ) {
                            Text(
                                text = if (permissionForm.isActive) "ACTIVE" else "INACTIVE"
                            )
                        }
                    }
                }
            }
        }
    )
}