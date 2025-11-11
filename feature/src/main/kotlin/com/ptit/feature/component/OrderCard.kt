package com.ptit.feature.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ptit.data.model.order.OrderData
import com.ptit.shared.Alpha
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.SubcomposeAsyncImage
import com.ptit.feature.domain.OrderEnum
import com.ptit.shared.ButtonPrimary
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton


@Composable
fun OrderCard(
    modifier : Modifier = Modifier,
    order: OrderData,
    onClick: ()->Unit,
    orderEnum: OrderEnum
){
    var showMore by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
            )
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
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

        Text(
            text = "Total price: ${order.summary.finalPrice} ₫",
            modifier = Modifier.align(Alignment.End)
        )
        if (orderEnum == OrderEnum.DELIVERED || orderEnum == OrderEnum.CANCELLED || orderEnum == OrderEnum.SHIPPED){
            Button(
                onClick= {},
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonPrimary
                ),
                shape = RoundedCornerShape(16.dp)
            ){
                Text(
                    text = "Order again"
                )
            }
        }

    }
}

@Composable
fun OrderItemCard(
    modifier : Modifier = Modifier,
    orderItem: OrderData.Item
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
                model = orderItem.imageUrl,
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
                    text = orderItem.productName,
                    fontSize = FontSize.EXTRA_REGULAR,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${orderItem.totalPrice}",
                        fontSize = FontSize.REGULAR,
                        color = TextSecondary
                    )
                    Text(
                        text = " ₫",
                        fontSize = FontSize.SMALL
                    )
                }
                Text(
                    text = "x${orderItem.quantity}",
                    fontSize = FontSize.SMALL,
                    color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                )
            }
        }

    }
}