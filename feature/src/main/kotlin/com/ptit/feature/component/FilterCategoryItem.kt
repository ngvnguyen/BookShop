package com.ptit.feature.component

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ptit.feature.form.CategoryForm
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.SurfaceDarker

@Composable
fun FilterCategoryItem(
    category: CategoryForm,
    isSelected:Boolean,
    onClick:(Boolean)->Unit
){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(horizontal = 2.dp)
            .clickable(onClick={onClick(!isSelected)})
            .background(
                color = if (isSelected) IconSecondary.copy(alpha = Alpha.HALF)
                    else SurfaceDarker
            )
    ){
        Text(
            text = category.name,
            fontSize = FontSize.REGULAR,
            fontWeight = FontWeight.W200
        )
    }
}