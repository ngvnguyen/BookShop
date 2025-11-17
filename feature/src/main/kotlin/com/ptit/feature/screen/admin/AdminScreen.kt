package com.ptit.feature.screen.admin

import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.data.model.dashboard.OrderStatusCountData
import com.ptit.feature.viewmodel.AdminViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.ptit.data.model.dashboard.RevenueByMonthData
import com.ptit.feature.component.ManageAdminItem
import com.ptit.feature.permission.Permission
import com.ptit.shared.Alpha
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import kotlin.math.ceil
import kotlin.math.pow

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
) {
    val adminViewModel = koinViewModel<AdminViewModel>()
    val orderStatusCountData by adminViewModel.orderStatusCountData.collectAsState()
    val overviewData by adminViewModel.overviewData.collectAsState()
    val revenueByMonthData by adminViewModel.revenueByMonthData.collectAsState()
    val rolePermission by adminViewModel.rolePermission

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
    ){padding->
        val colors = listOf<Color>(Color.Blue,Color.Red,Color.Yellow,Color.Green,Color.Cyan)
        if (orderStatusCountData.isLoading() && overviewData.isLoading() && revenueByMonthData.isLoading()){
            LoadingCard(
                modifier = Modifier.fillMaxSize()
            )
        }
        if (orderStatusCountData.isError() && overviewData.isError() && revenueByMonthData.isError()){
            ErrorCard(
                message = orderStatusCountData.getErrorMessage(),
                modifier = Modifier.fillMaxSize()
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                if (revenueByMonthData.isSuccess()){
                    val data = revenueByMonthData.getSuccessData()
                    val maxValue by remember {
                        mutableIntStateOf(data.maxOf { it.totalRevenue })
                    }
                    val maxDisplayValue by remember(maxValue) {
                        val p = maxValue.toString().length
                        val pow = 10.0.pow(p.toDouble()-1)
                        mutableIntStateOf(
                            ceil(maxValue*1.2/pow).toInt() * pow.toInt()
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Revenue(vnd)",
                            fontSize = FontSize.EXTRA_REGULAR,
                            color = TextSecondary,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        ColumnChart(
                            values = data.take(5),
                            colors = colors,
                            maxDisplayValue = maxDisplayValue
                        )
                    }
                }
            }

            item {
                if (orderStatusCountData.isSuccess()){
                    val data = orderStatusCountData.getSuccessData()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Order Status",
                            fontSize = FontSize.EXTRA_REGULAR,
                            color = TextSecondary,
                            fontWeight = FontWeight.W500
                        )
                        data.chunked(2).forEach { twoOrderStatus->
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                twoOrderStatus.forEach { orderStatus ->
                                    OrderStatusCard(
                                        modifier = Modifier.weight(1f),
                                        data = orderStatus
                                    )
                                }
                                if (twoOrderStatus.size == 1){
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
            item {
                if (overviewData.isSuccess()){
                    val data = overviewData.getSuccessData()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Overview",
                            fontSize = FontSize.EXTRA_REGULAR,
                            color = TextSecondary,
                            fontWeight = FontWeight.W500
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OverviewCard(
                                modifier = Modifier.weight(1f),
                                text1 = "Total users",
                                text2 = data.totalUsers.toString(),
                                icon = Resources.Icon.Person
                            )
                            OverviewCard(
                                modifier = Modifier.weight(1f),
                                text1 = "Total books",
                                text2 = data.totalBooks.toString(),
                                icon = Resources.Icon.Book
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OverviewCard(
                                modifier = Modifier.weight(1f),
                                text1 = "Total orders",
                                text2 = data.totalOrders.toString(),
                                icon = Resources.Icon.ShoppingCart
                            )
                            OverviewCard(
                                modifier = Modifier.weight(1f),
                                text1 = "Total revenue",
                                text2 = data.totalRevenue.toString(),
                                icon = Resources.Icon.Money
                            )
                        }
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Tools",
                        fontSize = FontSize.EXTRA_REGULAR,
                        color = TextSecondary,
                        fontWeight = FontWeight.W500
                    )
                    rolePermission.forEach { permission->
                        ManageAdminItem(
                            permission = permission,
                            onClick = {module->
                                when(module){
                                    Permission.Module.USERS ->{
                                        navigateToManageUser()
                                    }
                                    Permission.Module.ROLES -> {
                                        navigateToManageRole()
                                    }
                                    Permission.Module.CATEGORIES -> {
                                        navigateToManageCategory()

                                    }
                                    Permission.Module.AUTHORS -> {
                                        navigateToManageAuthor()

                                    }
                                    Permission.Module.PUBLISHERS -> {
                                        navigateToManagePublisher()

                                    }
                                    Permission.Module.BOOKS -> {
                                        navigateToManageBook()
                                    }
                                    Permission.Module.CARTS -> {
                                        navigateToManageCart()

                                    }
                                    Permission.Module.PERMISSIONS -> {
                                        navigateToManagePermission()
                                    }
                                }
                            }
                        )
                    }


                }
            }
        }
    }
}


@Composable
fun ColumnChart(
    values: List<RevenueByMonthData>,
    colors: List<Color>,
    maxDisplayValue:Int
){
    val step = 5
    val stepValue = maxDisplayValue/step
    Canvas(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .height(250.dp)
            .padding(end = 16.dp, start = 40.dp)
    ) {
        val columnWidth = size.width/(values.size*2)
        val spacing = columnWidth
        val textSiz = 28f

        for (i in 0..step){
            val y = size.height - (i/step.toFloat())*size.height
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(0f,y),
                end = Offset(size.width,y),
                strokeWidth = 1.dp.toPx()
            )

            drawContext.canvas.nativeCanvas.drawText(
                "${(i*stepValue).toInt()}",
                -70f,
                y,
                Paint().apply {
                    color = android.graphics.Color.DKGRAY
                    textSize = textSiz
                }
            )


        }

        drawContext.canvas.nativeCanvas.drawText(
            "Month",
            -70f,
            size.height+textSiz*1.3f,
            Paint().apply {
                color = android.graphics.Color.DKGRAY
                textSize = textSiz
            }
        )

        values.forEachIndexed { index,value->
            val barHeight = (value.totalRevenue.toFloat()/maxDisplayValue)*size.height
            val left = spacing/2 + index*(columnWidth+spacing)
            val top = size.height - barHeight
            val paint = Paint().apply {
                color = android.graphics.Color.DKGRAY
                textSize = textSiz
            }
            val labelWidth = paint.measureText(value.month.toString())
            val centerLabel = left + (columnWidth - labelWidth) / 2
            val valueWidth = paint.measureText(value.totalRevenue.toString())
            val centerValue = left + (columnWidth - valueWidth) / 2
            drawRect(
                color = colors[index],
                topLeft = Offset(left,top),
                size = Size(columnWidth,barHeight)
            )
            drawContext.canvas.nativeCanvas.drawText(
                value.month.toString(),
                centerLabel,
                size.height + textSiz*1.2f,
                paint
            )
            drawContext.canvas.nativeCanvas.drawText(
                value.totalRevenue.toString(),
                centerValue,
                top - textSiz,
                paint
            )
        }
    }
}

@Composable
fun OrderStatusCard(
    modifier: Modifier = Modifier,
    data : OrderStatusCountData
){
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "${data.orderStatus}: ",
            fontWeight = FontWeight.W400,
            fontSize = FontSize.REGULAR
        )
        Text(
            text = "${data.count}",
            fontSize = FontSize.REGULAR,
            color = TextSecondary
        )
    }
}

@Composable
fun OverviewCard(
    modifier : Modifier = Modifier,
    text1 : String,
    text2 : String,
    @DrawableRes icon:Int
){
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                vertical = 16.dp,
                horizontal = 8.dp
            )
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null
        )
        Spacer(
            modifier = Modifier.width(4.dp)
        )
        Text(
            text = "$text1: ",
            fontWeight = FontWeight.W400,
            fontSize = FontSize.REGULAR
        )
        Text(
            text = text2,
            fontSize = FontSize.REGULAR,
            color = TextSecondary
        )
    }
}