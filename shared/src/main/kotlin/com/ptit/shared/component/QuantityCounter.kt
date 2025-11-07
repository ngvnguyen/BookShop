package com.ptit.shared.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ptit.shared.FontSize
import com.ptit.shared.IconPrimary
import com.ptit.shared.QuantityCounterSize
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceBrand
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextPrimary


@Composable
fun QuantityCounter(
    modifier: Modifier = Modifier,
    size: QuantityCounterSize,
    value:Int,
    onMinusClick:(Int)-> Unit,
    onPlusClick:(Int)->Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(size.spacing)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable{
                    if (value>1) onMinusClick(value-1)
                }
                .background(SurfaceBrand)
                .padding(size.padding),
            contentAlignment = Alignment.Center
        ){
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(Resources.Icon.Minus),
                contentDescription = "Minus icon",
                tint = IconPrimary
            )
        }
        Box(
            modifier = Modifier
                .background(SurfaceLighter)
                .clip(RoundedCornerShape(6.dp))
                .padding(size.padding),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "$value",
                fontSize = FontSize.SMALL,
                lineHeight = FontSize.SMALL,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable{
                    onPlusClick(value+1)
                }
                .background(SurfaceBrand)
                .padding(size.padding),
            contentAlignment = Alignment.Center
        ){
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(Resources.Icon.Plus),
                contentDescription = "Plus icon",
                tint = IconPrimary
            )
        }
    }
}