package com.ptit.feature.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ptit.shared.SurfaceDarker

@Composable
fun FilterPriceRangeItem(
    minPrice:String,
    maxPrice:String?,
    onChecked:(Boolean)->Unit,
    isChecked:Boolean
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = SurfaceDarker,
                uncheckedColor = SurfaceDarker
            ),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = if (maxPrice!=null) "$minPrice - $maxPrice vnd"
            else "Above $minPrice vnd",
        )
    }
}