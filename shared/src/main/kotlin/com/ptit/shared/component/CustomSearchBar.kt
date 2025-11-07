package com.ptit.shared.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.TextPrimary
import com.ptit.shared.TextSecondary

@Composable
fun CustomSearchBar(
    modifier: Modifier=Modifier,
    value:String,
    onValueChange:(String)->Unit,
    trailingIcon:(@Composable ()->Unit)? =null,
    focusRequester: FocusRequester?=null,
    onFocusChange:(FocusState)->Unit={},
    onSearch:()->Unit,
    title:String?=null,
    placeholder:String?=null
){
    Column(modifier = modifier) {

        title?.let {
            Text(
                text = it,
                fontSize = FontSize.REGULAR
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(color = SurfaceDarker)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.weight(1f)){
                if (value.isEmpty() && placeholder!=null) {
                    Text(
                        text = placeholder,
                        fontSize = FontSize.REGULAR,
                        color = TextPrimary.copy(alpha = Alpha.HALF),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {onSearch()}
                    ),
                    modifier = Modifier
                        .background(Color.Unspecified)
                        .fillMaxWidth()
                        .onFocusChanged(onFocusChange)
                        .then(
                            if (focusRequester!=null) Modifier.focusRequester(focusRequester)
                            else Modifier
                        ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = FontSize.REGULAR
                    ),
                    cursorBrush = SolidColor(IconSecondary)
                )
            }

            trailingIcon?.invoke()
            Spacer(modifier = Modifier.width(8.dp))
        }
    }

}