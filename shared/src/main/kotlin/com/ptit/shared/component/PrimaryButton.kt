package com.ptit.shared.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ptit.shared.Alpha
import com.ptit.shared.ButtonDisabled
import com.ptit.shared.ButtonPrimary
import com.ptit.shared.FontSize
import com.ptit.shared.TextPrimary


@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int?=null,
    enabled: Boolean=true,
    onClick:()->Unit,
    containerColor: Color = ButtonPrimary
){
    Button(
        onClick= onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = TextPrimary,
            disabledContainerColor = ButtonDisabled,
            disabledContentColor = TextPrimary.copy(alpha = Alpha.DISABLE)
        ),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier,
        contentPadding = PaddingValues(20.dp)
    ) {
        icon?.let { i->
            Icon(
                painter = painterResource(i),
                contentDescription = "Button icon",
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = text,
            fontSize = FontSize.REGULAR,
            fontWeight = FontWeight.Medium
        )
    }

}