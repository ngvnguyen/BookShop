package com.ptit.feature.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ptit.feature.domain.DrawerState
import com.ptit.feature.domain.FilterPriceItem
import com.ptit.feature.domain.isOpened
import com.ptit.feature.domain.opposite
import com.ptit.shared.Alpha
import com.ptit.shared.Black
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.PrimaryButton

@Composable
fun BoxScope.FilterDrawer(
    modifier: Modifier = Modifier,
    visible:Boolean,
    onCloseClick:()->Unit,
    onFilterClick:()->Unit,
    onSelectFilterPriceItem:(Boolean, FilterPriceItem)->Unit,
    checkedItem: FilterPriceItem?
){
    AnimatedVisibility(
        visible = visible,
        modifier = modifier
            .systemBarsPadding()
            .fillMaxHeight()
            .fillMaxWidth(0.7f)
            .align(Alignment.CenterEnd)
            .background(SurfaceLighter)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = Alpha.TEN_PERCENT),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                onClick = {},
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        enter = slideInHorizontally(initialOffsetX = {it})+ fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = {it})+ fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(
                    painter = painterResource(Resources.Icon.Close),
                    contentDescription = "Close icon",
                    modifier = Modifier
                        .clickable(onClick = onCloseClick)
                        .scale(0.8f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Filter Products",
                    fontSize = FontSize.MEDIUM,
                    color = TextSecondary,
                    fontWeight = FontWeight.W700
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Black.copy(alpha = Alpha.TEN_PERCENT)
            )
            LazyColumn(
                modifier = Modifier.weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Price",
                        fontSize = FontSize.EXTRA_REGULAR,
                        fontWeight = FontWeight.W500
                    )
                }
                item{
                    Column {
                        FilterPriceItem.entries.forEach { item->
                            FilterPriceRangeItem(
                                minPrice = item.minPrice,
                                maxPrice = item.maxPrice,
                                onChecked = {
                                    onSelectFilterPriceItem(it,item)
                                },
                                isChecked = checkedItem == item
                            )
                        }
                    }
                }

            }

            Column(modifier = Modifier.padding(16.dp)) {
                PrimaryButton(
                    text = "Filter",
                    onClick = onFilterClick,
                    icon = Resources.Icon.Filter,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}