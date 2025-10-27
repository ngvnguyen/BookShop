package com.ptit.feature.component

import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.feature.domain.NavigationEnum
import com.ptit.shared.FontSize
import com.ptit.shared.IconPrimary
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.TextPrimary
import com.ptit.shared.TextSecondary

@Composable
fun NavigationItem(
    navigationEnum: NavigationEnum,
    onClick:()->Unit,
    isSelect: Boolean,
    needBadge:Boolean=false
){
    BadgedBox(badge = {
        if (needBadge){
            Badge(
                containerColor = IconSecondary,
            )
        }
    }){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(navigationEnum.icon),
                contentDescription = navigationEnum.title,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onClick),
                tint = if (isSelect) IconSecondary else IconPrimary
            )
            Text(
                text = navigationEnum.title,
                fontSize = FontSize.SMALL,
                color= if (isSelect) TextSecondary else TextPrimary
            )
        }
    }

}