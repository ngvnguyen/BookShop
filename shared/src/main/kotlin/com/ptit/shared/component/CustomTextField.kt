package com.ptit.shared.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ptit.shared.Alpha
import com.ptit.shared.BorderError
import com.ptit.shared.BorderIdle
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextPrimary


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value:String,
    onValueChange :(String)-> Unit,
    placeholder:String?=null,
    enabled:Boolean = true,
    error:Boolean = false,
    expanded: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text
    ),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon : (@Composable ()->Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    fillMaxWidth:Boolean = true
){
    val borderColor by animateColorAsState(
        targetValue = if (error) BorderError else BorderIdle
    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clip(RoundedCornerShape(6.dp)),
        placeholder = {
            placeholder?.let {
                Text(
                    text = it,
                    fontSize = FontSize.REGULAR
                )
            }
        },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        singleLine = !expanded,
        shape = RoundedCornerShape(6.dp),
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = TextPrimary,
            focusedTextColor = TextPrimary,
            disabledTextColor = TextPrimary.copy(alpha = Alpha.DISABLE) ,
            errorTextColor = TextPrimary,
            focusedContainerColor = SurfaceLighter,
            unfocusedContainerColor = SurfaceLighter,
            disabledContainerColor = SurfaceDarker,
            errorContainerColor = SurfaceLighter,
            focusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
            unfocusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
            disabledPlaceholderColor = TextPrimary.copy(alpha = Alpha.DISABLE),
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            selectionColors = TextSelectionColors(
                handleColor = IconSecondary,
                backgroundColor = Color.Unspecified
            ),
            cursorColor = IconSecondary
        ),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        textStyle = textStyle
    )
}
@Composable
fun CustomTextFieldWithLabel(
    modifier: Modifier = Modifier,
    value:String,
    onValueChange :(String)-> Unit,
    placeholder:String?=null,
    enabled:Boolean = true,
    error:Boolean = false,
    expanded: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text
    ),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon : (@Composable ()->Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    fillMaxWidth:Boolean = true,
    label:String
){
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color.Black.copy(alpha = Alpha.HALF),
            maxLines = 1
        )
        CustomTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            enabled = enabled,
            error = error,
            expanded = expanded,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            textStyle = textStyle,
            fillMaxWidth = fillMaxWidth
        )
    }
}