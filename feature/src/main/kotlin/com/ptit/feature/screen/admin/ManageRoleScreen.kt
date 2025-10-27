package com.ptit.feature.screen.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.ptit.feature.viewmodel.AdminViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.ui.unit.dp
import com.ptit.data.DisplayResult
import com.ptit.feature.form.RoleForm
import com.ptit.feature.viewmodel.Action
import com.ptit.feature.viewmodel.getStatus
import com.ptit.shared.Alpha
import com.ptit.shared.ButtonPrimary
import com.ptit.shared.LineHeight
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.CustomTextField
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import kotlinx.coroutines.flow.compose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageRoleScreen(
    navigateBack:()->Unit
){
    val adminViewModel = koinViewModel<AdminViewModel>()
    val roles by adminViewModel.allRole.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDialog by remember { mutableStateOf(false) }

    val rolePermission by adminViewModel.rolePermission
    val managePermission by remember {
        derivedStateOf {
            rolePermission.firstOrNull{it.isRole()}
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
                        text = "Manage Role",
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            if (postMethod)
                FloatingActionButton(
                    onClick = {
                        adminViewModel.setAct(Action.CREATE)
                        adminViewModel.resetRoleForm()
                        showDialog = true
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
        modifier = Modifier.fillMaxSize()
    ){ padding->
        roles.DisplayResult(
            onSuccess = {allRole->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        )
                        .padding(
                            vertical = 12.dp,
                            horizontal = 12.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(allRole) {role->
                        RoleItemCard(
                            roleForm = role,
                            onClick = {
                                adminViewModel.setAct(Action.UPDATE)
                                adminViewModel.selectRoleForm(role)
                                showDialog = true
                            },
                            onDelete = {
                                adminViewModel.deleteRole(
                                    id = role.id,
                                    onError = {e->
                                        snackbarHostState.showSnackbar(e)
                                    },
                                    onSuccess = {
                                        snackbarHostState.showSnackbar("Deleted")
                                    }
                                )
                            },
                            canDelete = deleteMethod
                        )
                    }
                }

                AnimatedVisibility(visible = showDialog) {
                    RoleFormDialog(
                        action = adminViewModel.action,
                        onDismiss = {showDialog=false},
                        onConfirm = {
                            if (adminViewModel.action.isUpdate()){
                                adminViewModel.updateRole(
                                    onError = {e->
                                        snackbarHostState.showSnackbar(e)
                                    },
                                    onSuccess = {
                                        showDialog = false
                                        snackbarHostState.showSnackbar("Updated")
                                    }
                                )
                            }else{
                                adminViewModel.createRole(
                                    onError = {e->
                                        snackbarHostState.showSnackbar(e)
                                    },
                                    onSuccess = {
                                        showDialog = false
                                        snackbarHostState.showSnackbar("Created")
                                    }
                                )
                            }
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
                LoadingCard(
                    modifier = Modifier.fillMaxSize()
                )
            }
        )

    }
}

@Composable
fun RoleItemCard(
    modifier: Modifier = Modifier,
    roleForm: RoleForm,
    onClick:()->Unit,
    onDelete:()->Unit,
    canDelete:Boolean
){

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = Color.Black.copy(alpha = Alpha.TEN_PERCENT),
                    shape = RoundedCornerShape(12.dp)
                )
                .background(SurfaceDarker)
                .clickable(onClick = onClick)
                .padding(
                    vertical = 8.dp,
                    horizontal = 12.dp
                )
        ) {
            Column(
                modifier = Modifier.weight(5f)
            ) {
                Text(
                    text = roleForm.name,
                    fontSize = FontSize.MEDIUM,
                    fontWeight = FontWeight.W500,
                    maxLines = 1
                )

                Text(
                    text = roleForm.description,
                    fontSize = FontSize.REGULAR,
                    color = Color.Black.copy(alpha = Alpha.HALF),
                    lineHeight = LineHeight.SMALL
                )
            }

            Text(
                text = roleForm.status.name,
                color = if (roleForm.status == RoleForm.Status.ACTIVE) Color.Green else Color.Red,
                fontSize = FontSize.EXTRA_REGULAR,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .weight(2f),
                textAlign = TextAlign.End
            )
        }

        if (canDelete)
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(Resources.Icon.Delete),
                    contentDescription = "delete"
                )
            }
    }

}

@Composable
fun RoleFormDialog(
    action: Action,
    onDismiss:()->Unit,
    onConfirm:()->Unit,
    viewModel: AdminViewModel
){
    val roleForm = viewModel.roleForm
    var showPermissionPicker by remember { mutableStateOf(false) }
    val mapPermission by viewModel.mapPermissionFlow.collectAsState()
    AlertDialog(
        shape = RoundedCornerShape(6.dp),
        containerColor = SurfaceLighter,
        onDismissRequest = onDismiss,
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceDarker,
                )
            ) {
                Text("Cancel")
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonPrimary,
                )
            ) {
                Text("Confirm")
            }
        },
        title = {
            Text(
                text = if (action.isUpdate()) "Update Role" else "Create Role",
                fontSize = FontSize.MEDIUM,
                fontWeight = FontWeight.W500,
                color = TextSecondary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextFieldWithLabel(
                    value = roleForm.name,
                    onValueChange = {
                        viewModel.updateRoleName(it)
                    },
                    placeholder = "Role Name",
                    label = "Name"
                )
                CustomTextFieldWithLabel(
                    value = roleForm.description,
                    onValueChange = {
                        viewModel.updateRoleDescription(it)
                    },
                    placeholder = "Role Description",
                    expanded = true,
                    label = "Description"
                )
                roleForm.createdBy?.let {
                    CustomTextFieldWithLabel(
                        value = it,
                        onValueChange = {},
                        enabled = false,
                        label = "Created By"
                    )
                }
                roleForm.createdAt?.let {
                    CustomTextFieldWithLabel(
                        value = it,
                        onValueChange = {},
                        enabled = false,
                        label = "Created At"
                    )
                }
                if (action==Action.UPDATE) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Status"
                        )
                        Button(
                            onClick = {
                                viewModel.updateRoleStatus(status = roleForm.status.opposite())
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (roleForm.status == RoleForm.Status.ACTIVE) Color.Green else Color.Red,
                                contentColor = Color.White
                            ),
                        ) {
                            Text(
                                text = roleForm.status.name
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Permissions"
                    )
                    Button(
                        onClick = {
                            showPermissionPicker = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceDarker
                        )
                    ) {
                        Text(
                            text = "Choose"
                        )
                    }
                }
            }

            AnimatedVisibility(visible = showPermissionPicker) {
                PermissionPicker(
                    onDismiss = {showPermissionPicker = false},
                    onDeleteClick = viewModel::deletePermissionInRole,
                    onAddClick = {id,name->
                        viewModel.addPermissionInRole(id,name)
                    },
                    selectedPermission = roleForm.listPermission,
                    allPermission = mapPermission
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionPicker(
    onDismiss: () -> Unit,
    onDeleteClick:(Int)->Unit,
    onAddClick:(Int,String)->Unit,
    selectedPermission: Map<Int,String>,
    allPermission: Map<Int,String>
){
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceLighter,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal =12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Choose",
                    fontSize = FontSize.MEDIUM,
                    color = TextSecondary
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allPermission.entries.toList()) {(id,name)->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .then(
                                if (selectedPermission.containsKey(id))
                                    Modifier
                                        .background(color = TextSecondary.copy(alpha = Alpha.TEN_PERCENT))
                                else Modifier
                            )
                            .clickable(onClick = {
                                if (selectedPermission.containsKey(id)) onDeleteClick(id)
                                else onAddClick(id,name)
                            })
                            .padding(
                                vertical = 12.dp,
                                horizontal = 8.dp
                            )


                    ) {
                        Text(
                            text = name,
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = FontSize.EXTRA_REGULAR
                        )
                    }
                }
            }

        }
    }
}