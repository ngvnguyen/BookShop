package com.ptit.shared.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.shared.Alpha
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceSecondary

@Composable
fun BottomPageSelect(
    page:Int,
    maxPage:Int,
    onForwardSelect:()->Unit,
    onRewindSelect:()->Unit,
    onPageSelect:(Int)->Unit
){
    val left = if (page>2) 2 else page-1
    val right = if (maxPage-page>2) 2 else maxPage-page
    val dotLeft = page>3
    val dotRight = maxPage-page>2
    val boxSize = 24.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 6.dp
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        if (page!=1) {
            Icon(
                painter = painterResource(Resources.Icon.FastRewind),
                contentDescription = "Rewind",
                modifier = Modifier.clickable(onClick = onRewindSelect)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (dotLeft) Text(text = "...")

        for (i in 1..left){
            val pageNum = page-left+i-1
            Spacer(modifier = Modifier.width(2.dp))
            Box(
                modifier = Modifier
                    .background(SurfaceDarker)
                    .size(boxSize)
                    .clickable(onClick = {onPageSelect(pageNum)}),
                contentAlignment = Alignment.Center
            ){Text(text = "$pageNum")}
        }
        Spacer(modifier = Modifier.width(2.dp))
        Box(
            modifier = Modifier
                .background(SurfaceSecondary.copy(alpha = Alpha.HALF))
                .size(boxSize),
            contentAlignment = Alignment.Center
        ){Text(text = "$page")}

        for (i in 1..right){
            val pageNum = page+i
            Spacer(modifier = Modifier.width(2.dp))
            Box(
                modifier = Modifier
                    .background(SurfaceDarker)
                    .size(boxSize)
                    .clickable(onClick = {onPageSelect(pageNum)}),
                contentAlignment = Alignment.Center
            ){Text(text = "$pageNum")}
        }

        if (dotRight) {
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "...")
        }

        if (page!=maxPage){
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(Resources.Icon.FastForward),
                contentDescription = "Forward",
                modifier = Modifier.clickable(onClick = onForwardSelect)
            )
        }
    }

}