package com.ptit.feature.screen.admin

import android.widget.Toast
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import com.ptit.feature.viewmodel.AdminViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.ptit.data.DisplayResult
import com.ptit.feature.form.CategoryForm
import com.ptit.feature.viewmodel.Action
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoryScreen(
    navigateBack:()->Unit,
    adminViewModel: AdminViewModel
){
    val categories by adminViewModel.allCategory.collectAsState()
    val rolePermission by adminViewModel.rolePermission
    val managePermission by remember {
        derivedStateOf {
            rolePermission.firstOrNull{it.isCategory()}
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
    var dialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        containerColor = SurfaceDarker,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Category",
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
                        adminViewModel.setAct(Action.CREATE)
                        adminViewModel.resetCategoryForm()
                        dialogVisible = true
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
    ){padding->
        categories.DisplayResult(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            ),
            onError = {e->
                ErrorCard(
                    message = e,
                    modifier = Modifier.fillMaxSize()
                )
            },
            onLoading = {
                LoadingCard(
                    modifier = Modifier.fillMaxSize()
                )
            },
            onSuccess = {listCategory->
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = listCategory
                    ) { item->
                        CategoryCard(
                            categoryForm = item,
                            onClick = {
                                if (putMethod){
                                    adminViewModel.selectCategoryForm(item)
                                    adminViewModel.setAct(Action.UPDATE)
                                    dialogVisible = true
                                }else{
                                    Toast.makeText(context,"You don't have permission to update category",Toast.LENGTH_SHORT).show()
                                }
                            },
                            canDelete = deleteMethod,
                            onDelete = {

                            }
                        )
                    }
                }

                AnimatedVisibility(visible = dialogVisible) {
                    ModifyCategoryDialog(
                        onDismissRequest = {dialogVisible=false},
                        onConfirm = {
                            adminViewModel.createCategory(
                                onSuccess = {
                                    dialogVisible = false
                                },
                                onError = {e->
                                    Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        action = adminViewModel.action,
                        categoryForm = adminViewModel.categoryForm,
                        viewModel = adminViewModel
                    )
                }
            }
        )
    }
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    categoryForm: CategoryForm,
    onClick:()->Unit,
    canDelete:Boolean,
    onDelete:()->Unit
){
    Box(
        modifier = modifier
            .heightIn(min = 120.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT),
                shape = RoundedCornerShape(8.dp)
            )
            .background(SurfaceLighter)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(
                    text = categoryForm.name,
                    fontSize = FontSize.EXTRA_REGULAR,
                    fontWeight = FontWeight.W400
                )
                Text(
                    text = "Description: ${categoryForm.description}",
                    color = Color.Black.copy(alpha = Alpha.HALF),
                    fontSize = FontSize.SMALL
                )
            }

        }
        Text(
            text = categoryForm.status.name,
            color = if (categoryForm.status == CategoryForm.Status.ACTIVE) Color.Green
            else Color.Red,
            fontWeight = FontWeight.W600,
            fontSize = FontSize.REGULAR,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
        if (canDelete){
            Icon(
                painter = painterResource(Resources.Icon.Delete),
                contentDescription = "Delete",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable(onClick = onDelete)
            )
        }
    }
}

@Composable
fun ModifyCategoryDialog(
    onDismissRequest:()->Unit,
    onConfirm:()->Unit,
    action: Action,
    categoryForm: CategoryForm,
    viewModel: AdminViewModel
){
    AlertDialog(
        containerColor = SurfaceLighter,
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        shape = RoundedCornerShape(8.dp),
        title = {
            Text(
                text = if (action == Action.CREATE) "Create Category" else "Update Category",
                fontSize = FontSize.MEDIUM,
                color = TextSecondary,
                fontWeight = FontWeight.W400
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextFieldWithLabel(
                    value = categoryForm.name,
                    onValueChange = {viewModel.updateCategoryForm(name = it)},
                    label = "Category name",
                    placeholder = "Name"
                )
                CustomTextFieldWithLabel(
                    value = categoryForm.description,
                    onValueChange = {viewModel.updateCategoryForm(description = it)},
                    label = "Category description",
                    placeholder = "Description"
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status",
                        fontSize = FontSize.REGULAR
                    )
                    Switch(
                        checked = categoryForm.status == CategoryForm.Status.ACTIVE,
                        onCheckedChange = {checked->
                            viewModel.updateCategoryForm(
                                status = if (checked) CategoryForm.Status.ACTIVE else CategoryForm.Status.INACTIVE
                            )
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.White,
                            checkedTrackColor = Color.Green,
                            uncheckedTrackColor = Color.Gray
                        )
                    )

                }

                PrimaryButton(
                    text = if (action == Action.CREATE) "Create" else "Update",
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}