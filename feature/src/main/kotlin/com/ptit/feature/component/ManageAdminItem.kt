package com.ptit.feature.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.feature.permission.Permission
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.TextPrimary

@Composable
fun ManageAdminItem(
    modifier : Modifier = Modifier,
    permission: Permission,
    onClick: (Permission.Module)->Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = TextPrimary.copy(alpha = Alpha.TEN_PERCENT)
            )
            .background(SurfaceDarker)
            .clickable(onClick={onClick(permission.module)})
            .padding(
                vertical = 12.dp,
                horizontal = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(6.dp))
        permission.module.icon?.let{
            Icon(
                painter = painterResource(it),
                contentDescription = null,
                tint = IconSecondary
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = permission.module.title,
            fontSize = FontSize.REGULAR
        )
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(Resources.Icon.KeyboardRightArrow),
            contentDescription = null
        )

    }
}