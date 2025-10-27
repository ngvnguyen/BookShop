package com.ptit.shared.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ptit.shared.IconPrimary
import com.ptit.shared.IconSecondary

@Composable
fun LoadingCard(
    modifier : Modifier = Modifier,

){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            color = IconSecondary,
            strokeWidth = 2.dp,
            modifier = Modifier.size(24.dp)
        )
    }
}