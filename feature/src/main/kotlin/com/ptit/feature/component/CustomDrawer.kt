package com.ptit.feature.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ptit.feature.domain.CustomDrawerItem
import com.ptit.shared.FontSize
import com.ptit.shared.TextSecondary
import com.ptit.shared.bebasNeueFont

@Composable
fun CustomDrawer(
    modifier: Modifier = Modifier,
    isAdmin: Boolean,
    onClick:(CustomDrawerItem)->Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .fillMaxHeight()
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "BOOK SHOP",
            textAlign = TextAlign.Center,
            fontSize = FontSize.EXTRA_LARGE,
            color = TextSecondary,
            fontFamily = bebasNeueFont()
        )

        Text(
            text = "Some description",
            textAlign = TextAlign.Center,
            fontSize = FontSize.REGULAR
        )

        Spacer(modifier = Modifier.height(50.dp))

        CustomDrawerItem.entries.take(CustomDrawerItem.entries.size-1).forEach { item->
            DrawerItemCard(
                drawerItem = item,
                onClick = onClick
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        if (isAdmin) DrawerItemCard(
            drawerItem = CustomDrawerItem.Admin,
            onClick = onClick
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}