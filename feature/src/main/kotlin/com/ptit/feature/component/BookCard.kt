package com.ptit.feature.component

import android.R
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
    cardHeight : Dp = 210.dp,
    elevation : Dp = 0.dp
){
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
                    .height(cardHeight)
                    .aspectRatio(0.666f)
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

            Text(
                text = item.name,
                fontSize = FontSize.EXTRA_REGULAR,
                color = TextSecondary,
                maxLines = 2
            )

            BadgedBox(
                badge = {
                    Text(
                        text = "-${item.discount}%",
                        fontSize = FontSize.EXTRA_SMALL,
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = "${item.price.toInt()} vnd",
                    fontSize = FontSize.REGULAR
                )
            }

        }
    }


}