package com.ptit.feature.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ptit.feature.form.AddressForm
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.TextSecondary
import kotlinx.coroutines.selects.select

@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    address: AddressForm,
    onClick:()->Unit,
    isSelected:Boolean,
    onLongClick:(Offset)->Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit){
                detectTapGestures(
                    onPress = {onClick()},
                    onLongPress = onLongClick
                )
            }
            .border(
                width = 0.5.dp,
                color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = address.receiverName,
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = address.phone,
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.W300,
                    color = Color.Black.copy(alpha = Alpha.HALF)
                )
            }
            Text(
                text = "${address.ward}, ${address.district}, ${address.city}",
                fontSize = FontSize.SMALL,
                fontWeight = FontWeight.W300,
                color = Color.Black.copy(alpha = Alpha.HALF)
            )
            Text(
                text = address.street,
                fontSize = FontSize.SMALL,
                fontWeight = FontWeight.W300,
                color = Color.Black.copy(alpha = Alpha.HALF)
            )
            Spacer(modifier = Modifier.height(12.dp))

        }
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = IconSecondary
            )
        )
    }
}