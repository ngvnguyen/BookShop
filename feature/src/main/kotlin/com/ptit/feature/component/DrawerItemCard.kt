package com.ptit.feature.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.feature.domain.CustomDrawerItem
import com.ptit.shared.FontSize
import com.ptit.shared.SurfaceDarker

@Composable
fun DrawerItemCard(
    modifier: Modifier = Modifier,
    drawerItem: CustomDrawerItem,
    onClick:(CustomDrawerItem)->Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(99.dp))
            .clickable{onClick(drawerItem)}
            .padding(
                vertical = 12.dp,
                horizontal = 12.dp
            )

    ) {
        Icon(
            painter = painterResource(drawerItem.icon),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = drawerItem.title,
            fontSize = FontSize.EXTRA_REGULAR
        )
    }
}