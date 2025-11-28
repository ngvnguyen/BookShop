package com.ptit.feature.component

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.ptit.data.model.checkout.CheckoutData
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.LoadingCard
import java.text.NumberFormat
import java.util.Locale


@Composable
fun ProductCard(
    modifier : Modifier = Modifier,
    item : CheckoutData.Item
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
                        text = "${numberFormatter.format(item.finalPrice)}",
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