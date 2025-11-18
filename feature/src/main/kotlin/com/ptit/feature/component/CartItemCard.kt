package com.ptit.feature.component

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.ptit.data.model.cart.CartData
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.QuantityCounterSize
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.QuantityCounter


@Composable
fun CartItemCard(
    modifier: Modifier= Modifier,
    item: CartData.CartItem,
    checked:Boolean,
    onCheckedChange:(Boolean)->Unit,
    onItemClick:()->Unit,
    onMinusClick:(Int)->Unit,
    onPlusClick:(Int)->Unit,
    onDeleteClick:()->Unit
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceLighter)
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT),
                shape = RoundedCornerShape(8.dp)
            )
    ){
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(120.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )
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
                    fontWeight = FontWeight.W500,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(0.85f)
                )

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${item.unitPrice}",
                        fontSize = FontSize.SMALL,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = " ₫",
                        fontSize = FontSize.EXTRA_SMALL
                    )
                    Text(
                        text = "-${item.discount}%",
                        fontSize = 9.sp,
                        color = Color.White,
                        fontStyle = FontStyle.Italic,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.Red),
                        style = TextStyle()
                    )
                }
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
                    Spacer(modifier = Modifier.weight(1f))
                    QuantityCounter(
                        size = QuantityCounterSize.Small,
                        value = item.quantity,
                        onMinusClick = onMinusClick,
                        onPlusClick = onPlusClick
                    )
                }
            }
        }

        IconButton(
            onClick = {
                onDeleteClick()
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(Resources.Icon.Delete),
                contentDescription = "delete"
            )
        }

    }


}