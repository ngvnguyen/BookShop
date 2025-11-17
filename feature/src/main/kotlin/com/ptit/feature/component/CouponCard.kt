package com.ptit.feature.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.ptit.data.model.coupon.CouponData
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.TextSecondary

@Composable
fun CouponCard(
    modifier: Modifier = Modifier,
    coupon: CouponData,
    onClick:()-> Unit
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(ZigZagShape())
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT),
                shape = ZigZagShape()
            )
            .clickable(onClick = onClick)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Box(
                modifier = Modifier
                    //size must be divisible by 8.dp
                    .size(104.dp)
                    .background(
                        when (coupon.discountType){
                            CouponData.CouponType.PERCENTAGE ,CouponData.CouponType.FIXED_AMOUNT -> TextSecondary.copy(alpha = Alpha.HALF)
                            CouponData.CouponType.FREE_SHIPPING -> Color.Green.copy(alpha = Alpha.HALF)
                        }
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = when(coupon.discountType){
                        CouponData.CouponType.PERCENTAGE -> "${coupon.discountValue}%"
                        CouponData.CouponType.FIXED_AMOUNT -> "${coupon.discountValue} đ"
                        CouponData.CouponType.FREE_SHIPPING -> "${coupon.discountValue} đ"
                    },
                    fontSize = when(coupon.discountType){
                        CouponData.CouponType.FIXED_AMOUNT,CouponData.CouponType.FREE_SHIPPING -> FontSize.MEDIUM
                        CouponData.CouponType.PERCENTAGE -> FontSize.LARGE
                    },
                    fontWeight = FontWeight.W400
                )
            }

            Column(
                modifier = Modifier.weight(1f)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = coupon.code,
                    fontSize = FontSize.EXTRA_REGULAR,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = coupon.name,
                    fontSize = FontSize.EXTRA_REGULAR,
                    fontWeight = FontWeight.W300,
                    maxLines = 2
                )
                Text(
                    text = "${coupon.startsAt} - ${coupon.expiresAt}",
                    fontSize = FontSize.EXTRA_SMALL,
                    color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT),
                    maxLines = 1
                )
            }
        }
    }
}

class ZigZagShape(
    private val zigZagSize: Dp = 8.dp
): Shape{
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val zigZag = with(density) {zigZagSize.toPx() }
        val path = Path().apply {
            moveTo(0f,0f)
            var y = 0f
            lineTo(size.width - zigZag,0f)
            while (y<size.height){
                lineTo(size.width,y+zigZag/2)
                lineTo(size.width-zigZag,y+zigZag)
                y+=zigZag
            }
            lineTo(0f,y)
            close()
        }
        return Outline.Generic(path)
        
    }

}