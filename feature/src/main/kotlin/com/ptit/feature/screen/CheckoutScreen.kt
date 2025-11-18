package com.ptit.feature.screen

import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import com.ptit.feature.viewmodel.CheckoutViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.ptit.data.DisplayResult
import com.ptit.data.model.checkout.CheckoutData
import com.ptit.shared.Alpha
import com.ptit.shared.QuantityCounterSize
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import com.ptit.shared.component.QuantityCounter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navigateBack:()->Unit,
    navigateToSuccess:()-> Unit
){
    val viewModel = koinViewModel<CheckoutViewModel>()
    val checkoutData by viewModel.checkoutData.collectAsState()
    val context = LocalContext.current
    Scaffold(
        containerColor = SurfaceDarker,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout",
                        fontSize = FontSize.MEDIUM
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigateBack()
                    }) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back Icon"
                        )
                    }
                }
            )
        }
    ) { padding ->
        checkoutData.DisplayResult(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            },
            onError = {e->
                ErrorCard(message=e,modifier=Modifier.fillMaxSize())
            },
            onSuccess = {checkout->
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
                            ){
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
                                            text = checkout.shippingAddress.name,
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.W500
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = checkout.shippingAddress.phone,
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.W300,
                                            color = Color.Black.copy(alpha = Alpha.HALF)
                                        )
                                    }
                                    Text(
                                        text = checkout.shippingAddress.address,
                                        fontSize = FontSize.SMALL,
                                        fontWeight = FontWeight.W300,
                                        color = Color.Black.copy(alpha = Alpha.HALF),
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }

                        item{
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
                                    text = "Order details",
                                    color = TextSecondary,
                                    fontSize = FontSize.EXTRA_REGULAR,
                                    fontWeight = FontWeight.W500,
                                )
                                checkout.items.forEachIndexed { index,item->
                                    if (index!=0) HorizontalDivider(modifier = Modifier.fillMaxWidth())
                                    ProductCard(
                                        item = item
                                    )
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
                            ){
                                Text(
                                    text = "Note",
                                    color = TextSecondary,
                                    fontSize = FontSize.EXTRA_REGULAR,
                                    fontWeight = FontWeight.W500,
                                )
                                TextField(
                                    value = viewModel.note,
                                    onValueChange = {viewModel.note = it},
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = SurfaceLighter,
                                        unfocusedContainerColor = SurfaceLighter
                                    ),
                                    maxLines = 3,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done
                                    )
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Payment method",
                                        color = TextSecondary,
                                        fontSize = FontSize.EXTRA_REGULAR,
                                        fontWeight = FontWeight.W500,
                                    )
                                    Text(
                                        text = checkout.paymentMethods
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Coupon",
                                        color = TextSecondary,
                                        fontSize = FontSize.EXTRA_REGULAR,
                                        fontWeight = FontWeight.W500,
                                    )
                                    Text(
                                        text = checkout.couponInfo?.code?:"No coupon"
                                    )
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
                                            text = "Total quantity",
                                            color = Color.Black.copy(alpha = Alpha.HALF),
                                            fontSize = FontSize.SMALL
                                        )
                                        Text(
                                            text = "${checkout.summary.totalQuantity}",
                                            color = Color.Black.copy(alpha = Alpha.HALF),
                                            fontSize = FontSize.SMALL
                                        )
                                    }
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
                                            text = "${checkout.summary.subtotal} ₫",
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
                                            text = "-${checkout.summary.cartDiscount} ₫",
                                            color = Color.Black.copy(alpha = Alpha.HALF),
                                            fontSize = FontSize.SMALL
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Shipping fee",
                                            color = Color.Black.copy(alpha = Alpha.HALF),
                                            fontSize = FontSize.SMALL
                                        )
                                        Text(
                                            text = "${checkout.summary.shippingFee} ₫",
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
                                            text = "${checkout.summary.grandTotal} ₫",
                                            color = TextSecondary,
                                            fontWeight = FontWeight.W500,
                                            fontSize = FontSize.REGULAR
                                        )
                                    }
                                }
                            }
                        }



                    }
                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        text = "Order now",
                        onClick = {
                            viewModel.order(
                                onSuccess = {
                                    navigateToSuccess()
                                },
                                onError = {e->
                                    Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )

                }

            }
        )
    }
}
@Composable
fun ProductCard(
    modifier : Modifier = Modifier,
    item : CheckoutData.Item
){
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
                        text = "${item.finalPrice}",
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