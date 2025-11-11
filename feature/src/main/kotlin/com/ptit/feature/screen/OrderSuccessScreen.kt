package com.ptit.feature.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources

@Composable
fun OrderSuccessScreen(
    modifier: Modifier = Modifier,
    navigateToHome:()->Unit,
    navigateToOrder:()->Unit
){
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Resources.Icon.Check),
            contentDescription = "Info image",
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier=Modifier.height(24.dp))
        Text(
            text = "Your order has been placed!",
            fontSize = FontSize.MEDIUM,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = navigateToHome) {
                Text(
                    text = "Back to Home"
                )
            }
            OutlinedButton(
                onClick = navigateToOrder,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = IconSecondary.copy(alpha = Alpha.HALF)
                )
            ) {
                Text(
                    text = "View Order"
                )
            }
        }
    }
}