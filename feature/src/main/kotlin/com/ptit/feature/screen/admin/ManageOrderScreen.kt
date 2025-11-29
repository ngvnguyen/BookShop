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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.ptit.data.DisplayResult
import com.ptit.data.model.coupon.CouponData
import com.ptit.data.model.order.OrderData
import com.ptit.data.model.order.UpdateOrderStatusRequest
import com.ptit.feature.component.OrderItemCard
import com.ptit.feature.form.CategoryForm
import com.ptit.feature.viewmodel.Action
import com.ptit.feature.viewmodel.AdminViewModel
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.CustomTextField
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageOrderScreen(
    navigateBack:()->Unit,
    adminViewModel: AdminViewModel
){
    val rolePermission by adminViewModel.rolePermission
    val managePermission by remember {
        derivedStateOf {
            rolePermission.firstOrNull{it.isOrder()}
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
    val allOrder by adminViewModel.allOrder.collectAsState()
    var selectedId by remember { mutableIntStateOf(-1) }
    var dialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(
        containerColor = SurfaceDarker,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Order",
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
        allOrder.DisplayResult(
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
            onSuccess = {orders->
                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = orders.orders) { order->
                        OrderAdminCard(
                            order = order,
                            onClick = {
                                selectedId = order.id
                                dialogVisible = true
                            }
                        )
                    }
                }

                AnimatedVisibility(visible = dialogVisible) {
                    var selectedStatus by remember { mutableStateOf<UpdateOrderStatusRequest.OrderStatus?>(null) }
                    AlertDialog(
                        onDismissRequest = {dialogVisible = false},
                        confirmButton = {},
                        title = {
                            Text(
                                text = "Change order status",
                                fontSize = FontSize.EXTRA_REGULAR
                            )
                        },
                        text = {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text="New status"
                                    )
                                    Spacer(modifier = Modifier.width(32.dp))
                                    var dropdownMenuOpened by remember { mutableStateOf(false) }
                                    CustomTextField(
                                        value = selectedStatus?.name?:"",
                                        onValueChange = {},
                                        enabled = false,
                                        trailingIcon = {
                                            IconButton(onClick = {dropdownMenuOpened = !dropdownMenuOpened}) {
                                                Icon(
                                                    painter = if (dropdownMenuOpened) painterResource(Resources.Icon.ArrowUp)
                                                    else painterResource(Resources.Icon.ArrowDown),
                                                    contentDescription = "Expand status"
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = dropdownMenuOpened,
                                                onDismissRequest = {dropdownMenuOpened = false}
                                            ) {
                                                UpdateOrderStatusRequest.OrderStatus.entries.forEach { status->
                                                    DropdownMenuItem(
                                                        text = { Text(status.name) },
                                                        onClick = {
                                                            selectedStatus = status
                                                            dropdownMenuOpened = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    )

                                }
                                PrimaryButton(
                                    text = "Update Status",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    onClick = {
                                        selectedStatus?.let{
                                            adminViewModel.updateOrderStatus(
                                                id = selectedId,
                                                newStatus = it,
                                                onSuccess = {
                                                    Toast.makeText(context,"Update order status successfully",Toast.LENGTH_SHORT).show()
                                                    dialogVisible = false
                                                },
                                                onError = {e->
                                                    Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    )
                }
            }
        )

    }
}

@Composable
fun OrderAdminCard(
    modifier : Modifier = Modifier,
    order: OrderData,
    onClick:()->Unit
){
    var showMore by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
            )
            .background(SurfaceLighter)
            .padding(8.dp)
    ){
        Column(){
            order.items.firstOrNull()?.let{
                OrderItemCard(orderItem = it)
                if (order.items.size>1 && !showMore)
                    Text(
                        text = "Show more",
                        fontSize = FontSize.SMALL,
                        color = TextSecondary.copy(alpha = Alpha.HALF),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable(onClick={showMore = true})
                    )
            }
            AnimatedVisibility(
                visible = showMore
            ) {
                if (showMore){
                    Column(modifier = Modifier.fillMaxWidth()) {
                        order.items.takeLast(order.items.size-1).forEach { item->
                            OrderItemCard(orderItem = item)
                        }
                        Text(
                            text = "Show less",
                            fontSize = FontSize.SMALL,
                            color = TextSecondary.copy(alpha = Alpha.HALF),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable(onClick={showMore = false})
                        )
                    }
                }

            }
        }
        Text(
            text = order.status,
            color = if (order.status != "CANCELLED") Color.Green
            else Color.Red,
            fontWeight = FontWeight.W600,
            fontSize = FontSize.REGULAR,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }


}