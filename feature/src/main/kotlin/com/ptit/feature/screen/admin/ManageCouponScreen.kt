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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.setSelectedDate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.ptit.data.DisplayResult
import com.ptit.data.model.coupon.CouponData
import com.ptit.feature.domain.Gender
import com.ptit.feature.form.CategoryForm
import com.ptit.feature.form.CouponForm
import com.ptit.feature.viewmodel.Action
import com.ptit.feature.viewmodel.AdminViewModel
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.GrayDarker
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
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCouponScreen(
    adminViewModel: AdminViewModel,
    navigateBack:()->Unit
) {
    val rolePermission by adminViewModel.rolePermission
    val managePermission by remember {
        derivedStateOf {
            rolePermission.firstOrNull{it.isCoupon()}
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
    val coupons by adminViewModel.allCoupon.collectAsState()
    var dialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(
        containerColor = SurfaceDarker,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Coupon",
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
                        adminViewModel.resetCouponForm()
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
        coupons.DisplayResult(
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
            onSuccess = {allCoupon->
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = allCoupon
                    ) { item->
                        ManageCouponCard(
                            coupon = item,
                            onClick = {
                                adminViewModel.selectCouponForm(item)
                                adminViewModel.setAct(Action.UPDATE)
                                dialogVisible = true
                            },
                            canDelete = deleteMethod,
                            onDelete = {
                                adminViewModel.deleteCoupon(
                                    id = item.id,
                                    onSuccess = {message->
                                        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()
                                        dialogVisible = false
                                    },
                                    onError = {message->
                                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        )
                    }
                }

                AnimatedVisibility(visible = dialogVisible) {
                    ModifyCouponDialog(
                        onDismissRequest = {dialogVisible = false},
                        onConfirm = {
                            if (adminViewModel.action == Action.CREATE) adminViewModel.createCoupon(
                                onSuccess = {message->
                                    Toast.makeText(context,"Create successfully",Toast.LENGTH_SHORT).show()
                                    dialogVisible = false
                                },
                                onError = {message->
                                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                                }
                            )else adminViewModel.updateCoupon(
                                onSuccess = {message->
                                    Toast.makeText(context,"Update successfully",Toast.LENGTH_SHORT).show()
                                    dialogVisible = false
                                },
                                onError = {message->
                                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        action = adminViewModel.action,
                        coupon = adminViewModel.couponForm,
                        viewModel = adminViewModel
                    )
                }
            }
        )
    }
}

@Composable
fun ManageCouponCard(
    modifier : Modifier = Modifier,
    coupon : CouponForm,
    onClick:()-> Unit,
    canDelete:Boolean,
    onDelete: () -> Unit
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
                    text = coupon.code,
                    fontSize = FontSize.EXTRA_REGULAR,
                    fontWeight = FontWeight.W500,
                    color = TextSecondary
                )
                Text(
                    text = "Discount type: ${coupon.discountType.title}",
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.W400
                )
                Text(
                    text = "Discount value: ${coupon.discountValue}",
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.W400
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Description: ${coupon.description}",
                    color = Color.Black.copy(alpha = Alpha.HALF),
                    fontSize = FontSize.SMALL
                )
            }

        }
        Text(
            text = coupon.status.name,
            color = if (coupon.status == CouponData.Status.ACTIVE) Color.Green
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyCouponDialog(
    onDismissRequest:()->Unit,
    onConfirm:()->Unit,
    action: Action,
    coupon: CouponForm,
    viewModel: AdminViewModel
){

    val startAtLocalDate = coupon.startsAt.toLocalDate()
    val startAtLocalTime = coupon.startsAt.toLocalTime()
    val expiresAtLocalDate = coupon.expiresAt.toLocalDate()
    val expiresAtLocalTime = coupon.expiresAt.toLocalTime()

    var startAtDateOpen by remember { mutableStateOf(false) }
    var startAtTimeOpen by remember { mutableStateOf(false) }
    var expiresAtDateOpen by remember { mutableStateOf(false) }
    var expiresAtTimeOpen by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    LaunchedEffect(startAtDateOpen) {
        if (startAtDateOpen) {
            datePickerState.setSelectedDate(startAtLocalDate)
        }
    }
    LaunchedEffect(startAtTimeOpen) {
        if (startAtTimeOpen) {
            timePickerState.hour = startAtLocalTime.hour
            timePickerState.minute = startAtLocalTime.minute
        }
    }
    LaunchedEffect(expiresAtDateOpen) {
        if (expiresAtDateOpen) {
            datePickerState.setSelectedDate(expiresAtLocalDate)
        }
    }
    LaunchedEffect(expiresAtTimeOpen) {
        if (expiresAtTimeOpen) {
            timePickerState.hour = expiresAtLocalTime.hour
            timePickerState.minute = expiresAtLocalTime.minute
        }
    }

    AlertDialog(
        containerColor = SurfaceLighter,
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        shape = RoundedCornerShape(8.dp),
        title = {
            Text(
                text = if (action == Action.CREATE) "Create Coupon" else "Update Coupon",
                fontSize = FontSize.MEDIUM,
                color = TextSecondary,
                fontWeight = FontWeight.W400
            )
        },
        text = {
            Column{
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        CustomTextFieldWithLabel(
                            value = coupon.code,
                            onValueChange = {viewModel.updateCouponForm(code = it)},
                            label = "Coupon code",
                            placeholder = "Code"
                        )
                    }
                    item {
                        CustomTextFieldWithLabel(
                            value = coupon.name,
                            onValueChange = {viewModel.updateCouponForm(name = it)},
                            label = "Coupon name",
                            placeholder = "Name"
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = coupon.description,
                            onValueChange = {viewModel.updateCouponForm(description = it)},
                            label = "Coupon description",
                            placeholder = "Description"
                        )
                    }
                    item{
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CustomTextFieldWithLabel(
                                value = coupon.discountValue.toString(),
                                onValueChange = {
                                    val intValue = it.toIntOrNull() ?: 0
                                    viewModel.updateCouponForm(discountValue = intValue)
                                },
                                label = "Discount value",
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier.weight(2f)
                            ){
                                var dropdownMenuOpened by remember { mutableStateOf(false) }
                                CustomTextFieldWithLabel(
                                    value = coupon.discountType.title,
                                    onValueChange = {},
                                    enabled = false,
                                    trailingIcon = {
                                        IconButton(onClick = {dropdownMenuOpened = !dropdownMenuOpened}) {
                                            Icon(
                                                painter = if (dropdownMenuOpened) painterResource(Resources.Icon.ArrowUp)
                                                else painterResource(Resources.Icon.ArrowDown),
                                                contentDescription = "Expand Discount Type"
                                            )
                                        }
                                    },
                                    label = "Discount Type"
                                )
                                DropdownMenu(
                                    expanded = dropdownMenuOpened,
                                    onDismissRequest = {dropdownMenuOpened = false}
                                ) {
                                    CouponData.CouponType.entries.forEach { couponType->
                                        DropdownMenuItem(
                                            text = { Text(couponType.title) },
                                            onClick = {
                                                viewModel.updateCouponForm(discountType = couponType)
                                                dropdownMenuOpened = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = coupon.minimumOrderAmount.toString(),
                            onValueChange = {
                                val intValue = it.toIntOrNull() ?: 0
                                viewModel.updateCouponForm(minimumOrderAmount = intValue)
                            },
                            label = "Minimum Discount Amount",
                            placeholder = "Minimum Amount"
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = coupon.maximumDiscountAmount.toString(),
                            onValueChange = {
                                val intValue = it.toIntOrNull() ?: 0
                                viewModel.updateCouponForm(maximumDiscountAmount = intValue)
                            },
                            label = "Maximum Discount Amount",
                            placeholder = "Maximum Amount"
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = coupon.usageLimit.toString(),
                            onValueChange = {
                                val intValue = it.toIntOrNull() ?: 0
                                viewModel.updateCouponForm(usageLimit = intValue)
                            },
                            label = "Limit"
                        )
                    }
                    item{
                        CustomTextFieldWithLabel(
                            value = coupon.usageLimitPerCustomer.toString(),
                            onValueChange = {
                                val intValue = it.toIntOrNull() ?: 0
                                viewModel.updateCouponForm(usageLimitPerCustomer = intValue)
                            },
                            label = "Limit per customer",
                        )
                    }
                    item{
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier.weight(4f)
                            ){
                                CustomTextFieldWithLabel(
                                    value = startAtLocalDate.toString(),
                                    onValueChange = {},
                                    enabled = false,
                                    trailingIcon = {
                                        IconButton(onClick = {startAtDateOpen = !startAtDateOpen}) {
                                            Icon(
                                                painter = if (startAtDateOpen) painterResource(Resources.Icon.ArrowUp)
                                                else painterResource(Resources.Icon.ArrowDown),
                                                contentDescription = "Pick starts at date"
                                            )
                                        }
                                    },
                                    label = "Starts date"
                                )
                            }

                            Box(
                                modifier = Modifier.weight(3f)
                            ){
                                CustomTextFieldWithLabel(
                                    value = startAtLocalTime.toString(),
                                    onValueChange = {},
                                    enabled = false,
                                    trailingIcon = {
                                        IconButton(onClick = {startAtTimeOpen = !startAtTimeOpen}) {
                                            Icon(
                                                painter = if (startAtTimeOpen) painterResource(Resources.Icon.ArrowUp)
                                                else painterResource(Resources.Icon.ArrowDown),
                                                contentDescription = "Pick starts at time"
                                            )
                                        }
                                    },
                                    label = "Starts time"
                                )
                            }
                        }
                    }
                    item{
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier.weight(4f)
                            ){
                                CustomTextFieldWithLabel(
                                    value = expiresAtLocalDate.toString(),
                                    onValueChange = {},
                                    enabled = false,
                                    trailingIcon = {
                                        IconButton(onClick = {expiresAtDateOpen = !expiresAtDateOpen}) {
                                            Icon(
                                                painter = if (expiresAtDateOpen) painterResource(Resources.Icon.ArrowUp)
                                                else painterResource(Resources.Icon.ArrowDown),
                                                contentDescription = "Pick expires at date"
                                            )
                                        }
                                    },
                                    label = "Expires date"
                                )
                            }

                            Box(
                                modifier = Modifier.weight(3f)
                            ){
                                CustomTextFieldWithLabel(
                                    value = expiresAtLocalTime.toString(),
                                    onValueChange = {},
                                    enabled = false,
                                    trailingIcon = {
                                        IconButton(onClick = {expiresAtTimeOpen = !expiresAtTimeOpen}) {
                                            Icon(
                                                painter = if (expiresAtTimeOpen) painterResource(Resources.Icon.ArrowUp)
                                                else painterResource(Resources.Icon.ArrowDown),
                                                contentDescription = "Pick expires at time"
                                            )
                                        }
                                    },
                                    label = "Expires time"
                                )
                            }
                        }
                    }
                    if (action.isUpdate()) item{
                        var dropdownMenuOpened by remember { mutableStateOf(false) }
                        CustomTextFieldWithLabel(
                            value = coupon.status.name,
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
                                    CouponData.Status.entries.forEach { couponStatus->
                                        DropdownMenuItem(
                                            text = { Text(couponStatus.name) },
                                            onClick = {
                                                viewModel.updateCouponForm(status = couponStatus)
                                                dropdownMenuOpened = false
                                            }
                                        )
                                    }
                                }
                            },
                            label = "Status"
                        )

                    }

                }
                Spacer(modifier = Modifier.height(12.dp))
                PrimaryButton(
                    text = if (action == Action.CREATE) "Create" else "Update",
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

        }
    )

    if (startAtDateOpen || expiresAtDateOpen){
        DatePickerDialog(
            onDismissRequest = {
                startAtDateOpen = false
                expiresAtDateOpen = false
            },
            confirmButton = {
                TextButton(onClick = {
                    if (startAtDateOpen){
                        datePickerState.selectedDateMillis?.let {
                            val startAt = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.updateCouponForm(startsAt = LocalDateTime.of(startAt,startAtLocalTime))
                        }

                        startAtDateOpen = false

                    }
                    if (expiresAtDateOpen){
                        datePickerState.selectedDateMillis?.let {
                            val expiresAt = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.updateCouponForm(expiresAt = LocalDateTime.of(expiresAt,expiresAtLocalTime))
                        }
                        expiresAtDateOpen = false
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        startAtDateOpen = false
                        expiresAtDateOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GrayDarker
                    )
                ) {
                    Text("Dismiss")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
    if (startAtTimeOpen || expiresAtTimeOpen){
        TimePickerDialog(
            onDismissRequest = {
                startAtTimeOpen = false
                expiresAtTimeOpen = false
            },
            confirmButton = {
                TextButton(onClick = {
                    if (startAtTimeOpen){
                        startAtTimeOpen = false
                        with(timePickerState) {
                            val startAt = LocalTime.of(hour,minute)
                            viewModel.updateCouponForm(startsAt = LocalDateTime.of(startAtLocalDate,startAt))
                        }
                    }
                    if (expiresAtTimeOpen){
                        expiresAtTimeOpen = false
                        with(timePickerState) {
                            val expiresAt = LocalTime.of(hour,minute)
                            viewModel.updateCouponForm(expiresAt = LocalDateTime.of(expiresAtLocalDate,expiresAt))
                        }
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        startAtTimeOpen = false
                        expiresAtTimeOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GrayDarker
                    )
                ) {
                    Text("Dismiss")
                }
            },
            title = {
                Text(text = "Select Time")
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}