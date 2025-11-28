package com.ptit.feature.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.feature.viewmodel.OrderDetailViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.component.CustomSearchBar
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import coil.compose.SubcomposeAsyncImage
import com.ptit.data.DisplayResult
import com.ptit.data.model.checkout.CheckoutData
import com.ptit.data.model.order.OrderData
import com.ptit.feature.component.ProductCard
import com.ptit.feature.domain.OrderEnum
import com.ptit.shared.Alpha
import com.ptit.shared.ButtonPrimary
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navigateBack:()->Unit
){
    val viewModel = koinViewModel<OrderDetailViewModel>()
    val numberFormatter = NumberFormat.getNumberInstance(Locale.GERMAN)
    val order by viewModel.order.collectAsState()
    var cancelOrderDialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(
        containerColor = SurfaceDarker,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout",
                        fontSize = FontSize.MEDIUM
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {navigateBack()}
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back"
                        )
                    }
                }
            )

        }
    ){padding->
        order.DisplayResult(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            ),
            onError = {e->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        ErrorCard(
                            message = e
                        )
                    }
                }
            },
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            },
            onSuccess = {orderData->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = 1.dp,
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                                    )
                                    .background(SurfaceLighter)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Receive address",
                                    color = TextSecondary,
                                    fontSize = FontSize.EXTRA_REGULAR,
                                    fontWeight = FontWeight.W500
                                )
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = orderData.shippingInfo.receiverName,
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.W500
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = orderData.shippingInfo.receiverPhone,
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.W300,
                                            color = Color.Black.copy(alpha = Alpha.HALF)
                                        )
                                    }
                                    Text(
                                        text = orderData.shippingInfo.receiverAddress,
                                        fontSize = FontSize.SMALL,
                                        fontWeight = FontWeight.W300,
                                        color = Color.Black.copy(alpha = Alpha.HALF),
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = 1.dp,
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                                    )
                                    .background(SurfaceLighter)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Order details",
                                    color = TextSecondary,
                                    fontSize = FontSize.EXTRA_REGULAR,
                                    fontWeight = FontWeight.W500,
                                )
                                orderData.items.forEachIndexed { index, item ->
                                    if (index != 0) HorizontalDivider(modifier = Modifier.fillMaxWidth())
                                    OrderDetailCard(
                                        item = item
                                    )
                                }

                            }
                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = 1.dp,
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                                    )
                                    .background(SurfaceLighter)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Text(
                                    text = "Payment method",
                                    color = TextSecondary,
                                    fontSize = FontSize.EXTRA_REGULAR,
                                    fontWeight = FontWeight.W500,
                                )
                                Text(
                                    text = orderData.paymentMethod
                                )
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = 1.dp,
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                                    )
                                    .background(SurfaceLighter)
                                    .padding(8.dp)
                            ){
                                Text(
                                    text = "Summary",
                                    color = TextSecondary,
                                    fontSize = FontSize.EXTRA_REGULAR,
                                    fontWeight = FontWeight.W500,
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Subtotal",
                                            color = Color.Black.copy(alpha = Alpha.HALF),
                                            fontSize = FontSize.SMALL
                                        )
                                        Text(
                                            text = "${numberFormatter.format(orderData.summary.subtotal)} ₫",
                                            color = Color.Black.copy(alpha = Alpha.HALF),
                                            fontSize = FontSize.SMALL
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Discount",
                                            color = Color.Black.copy(alpha = Alpha.HALF),
                                            fontSize = FontSize.SMALL
                                        )
                                        Text(
                                            text = "-${numberFormatter.format(orderData.summary.discountFee)} ₫",
                                            color = Color.Black.copy(alpha = Alpha.HALF),
                                            fontSize = FontSize.SMALL
                                        )
                                    }
                                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Grand total",
                                            fontWeight = FontWeight.W500,
                                            fontSize = FontSize.REGULAR
                                        )
                                        Text(
                                            text = "${numberFormatter.format(orderData.summary.finalPrice)} ₫",
                                            color = TextSecondary,
                                            fontWeight = FontWeight.W500,
                                            fontSize = FontSize.REGULAR
                                        )
                                    }
                                }
                            }
                        }

                    }

                    if (orderData.status == OrderEnum.PENDING.name) PrimaryButton(
                        text = "Cancel order",
                        onClick = {
                            cancelOrderDialogVisible = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                AnimatedVisibility(visible = cancelOrderDialogVisible){
                    AlertDialog(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Are you sure you want to cancel this order?",
                                    fontSize = FontSize.REGULAR,
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                )
                                Icon(
                                    painter = painterResource(Resources.Icon.Close),
                                    contentDescription = "close",
                                    modifier = Modifier
                                        .clickable(onClick = {
                                            cancelOrderDialogVisible = false
                                        })
                                )
                            }
                        },
                        onDismissRequest = {},
                        confirmButton = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                OutlinedButton(
                                    onClick = {viewModel.cancelOrder(
                                        onSuccess = {
                                            cancelOrderDialogVisible = false
                                            Toast.makeText(context,"Cancel order successfully",Toast.LENGTH_SHORT).show()
                                        },
                                        onError = {e->
                                            Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
                                        }
                                    )}
                                ) {
                                    Text(
                                        text = "Yes"
                                    )
                                }
                                Spacer(modifier = Modifier.width(48.dp))
                                OutlinedButton(
                                    onClick = {
                                        cancelOrderDialogVisible = false
                                    },

                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = ButtonPrimary
                                    )
                                ) {
                                    Text(
                                        text = "No"
                                    )
                                }

                            }
                        }
                    )
                }
            }

        )
    }
}
@Composable
fun OrderDetailCard(
    modifier : Modifier = Modifier,
    item : OrderData.Item
){
    val numberFormatter = NumberFormat.getNumberInstance(Locale.GERMAN)
    Box(
        modifier = modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SubcomposeAsyncImage(
                model = item.imageUrl,
                loading = {
                    LoadingCard()
                },
                error = {
                    Image(
                        painter = painterResource(Resources.Icon.Broken),
                        contentDescription = "broken image"
                    )
                },
                contentDescription = "book image",
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(0.666f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = item.productName,
                    fontSize = FontSize.EXTRA_REGULAR,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${numberFormatter.format(item.unitPrice)}",
                        fontSize = FontSize.REGULAR,
                        color = TextSecondary
                    )
                    Text(
                        text = " ₫",
                        fontSize = FontSize.SMALL
                    )
                }
                Text(
                    text = "x${item.quantity}",
                    fontSize = FontSize.SMALL,
                    color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                )
            }
        }

    }
}