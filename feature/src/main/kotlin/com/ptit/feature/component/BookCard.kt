package com.ptit.feature.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.ptit.feature.form.BookForm
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.LoadingCard

@Composable
fun BookCard(
    modifier: Modifier = Modifier,
    item: BookForm,
    onClick:()-> Unit,
    imageHeight : Dp = 160.dp,
    elevation : Dp = 0.dp,
){
    val cardWidth = imageHeight*0.66f
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
        ),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceLighter
        )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    start = 8.dp,
                    end = 8.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .height(imageHeight)
                    .aspectRatio(0.66f)
                    .border(width = 1.dp,color = Color.Black.copy(alpha = Alpha.HALF))
            ){
                SubcomposeAsyncImage(
                    model = item.image,
                    contentDescription = "book image",
                    loading = {
                        LoadingCard()
                    },
                    error = {
                        Icon(
                            painter = painterResource(Resources.Icon.Broken),
                            contentDescription = "error image"
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.name,
                fontSize = FontSize.REGULAR,
                color = TextSecondary,
                maxLines = 1,
                fontWeight = FontWeight.W500,
                modifier = Modifier.width(cardWidth)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.price.toInt()} ₫",
                    modifier = Modifier.widthIn(max = cardWidth),
                    maxLines = 1,
                    textDecoration = TextDecoration.LineThrough,
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

            Text(
                text = "${(item.price - item.price*item.discount/100).toInt()} ₫",
                modifier = Modifier.widthIn(max = cardWidth),
                maxLines = 1,
                fontSize = FontSize.SMALL
            )


        }
    }


}