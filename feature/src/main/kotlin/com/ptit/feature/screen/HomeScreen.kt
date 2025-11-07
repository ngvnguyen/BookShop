package com.ptit.feature.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ptit.data.DisplayResult
import com.ptit.feature.component.BookCard
import com.ptit.feature.component.FilterDrawer
import com.ptit.feature.domain.DrawerState
import com.ptit.feature.domain.isOpened
import com.ptit.feature.domain.opposite
import com.ptit.feature.form.BookForm
import com.ptit.feature.viewmodel.HomeViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.component.CustomSearchBar
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onDrawerClick:()->Unit,
    navigateToBookDetails:(Int)->Unit,
    navigateToBook:()->Unit,
    viewModel: HomeViewModel
){
    val bookPaged by viewModel.bookPaged.collectAsState()
    val context = LocalContext.current

    var drawerState by remember { mutableStateOf(DrawerState.Closed) }
    val coroutineScope = rememberCoroutineScope()
    val filterPriceItem = viewModel.filterPriceItem
    val searchQuery = viewModel.searchQuery
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    var isRefreshing = viewModel.isRefreshing
    Box(
        modifier = modifier.fillMaxSize()
    ){
        Scaffold(
            containerColor = if (drawerState.isOpened()) SurfaceDarker else SurfaceLighter,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (drawerState.isOpened())
                        Modifier
                            .clickable(
                                onClick ={drawerState = DrawerState.Closed},
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    else Modifier
                ),
            topBar = {
                TopAppBar(
                    title = {
                        CustomSearchBar(
                            value = searchQuery,
                            onValueChange = {viewModel.updateSearchQuery(it)},
                            onSearch = {

                            },
                            onFocusChange = {state->
                                isFocused = state.isFocused
                            },
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(Resources.Icon.Search),
                                    contentDescription = "Search",
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable(onClick={})
                                )
                            },
                            placeholder = "Search books"
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {onDrawerClick()}) {
                            Icon(
                                painter = painterResource(Resources.Icon.Menu),
                                contentDescription = "Menu Icon"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {drawerState = drawerState.opposite()}) {
                            Icon(
                                painter = painterResource(Resources.Icon.Filter),
                                contentDescription = "Filter icon"
                            )
                        }
                    }
                )

            }
        ) { padding->
            val state = rememberPullToRefreshState()
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    viewModel.homeRefresh()
                },
                modifier = Modifier
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    ),
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = isRefreshing,
                        state = state,
                        color = IconSecondary,
                        containerColor = SurfaceDarker
                    )
                },
                state = state
            ) {
                bookPaged.DisplayResult(
                    modifier = Modifier
                        .clickable(
                            onClick = {focusManager.clearFocus()},
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ),
                    onError = {e->
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            item {
                                ErrorCard(
                                    message = e,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                    },
                    onLoading = {
                        LoadingCard(modifier = Modifier.fillMaxSize())
                    },
                    onSuccess = {books->
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ){
                            LazyColumn(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                            ) {
                                item {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Row {
                                            VerticalDivider(
                                                modifier = Modifier.height(24.dp),
                                                thickness = 4.dp,
                                                color = IconSecondary
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "New",
                                                fontWeight = FontWeight.W500,
                                                fontSize = FontSize.EXTRA_REGULAR
                                            )
                                        }
                                        HorizontalBookCarousel(
                                            items = books,
                                            onClick = {book->
                                                navigateToBookDetails(book.id)
                                            }
                                        )

                                    }
                                }
                            }


                        }
                    }
                )
            }

        }

        FilterDrawer(
            visible = drawerState.isOpened(),
            onCloseClick = {drawerState = drawerState.opposite()},
            onFilterClick = {
                drawerState = drawerState.opposite()
            },
            checkedItem = filterPriceItem,
            onSelectFilterPriceItem = {isChecked,filterPriceItem->
                if (isChecked) viewModel.selectFilterPriceItem(null)
                else viewModel.selectFilterPriceItem(filterPriceItem)
            }
        )
    }


}





@Composable
fun HorizontalBookCarousel(
    modifier : Modifier = Modifier,
    items : List<BookForm>,
    onClick: (BookForm) -> Unit
){
    val listState = rememberLazyListState()
    val centeredIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val centeredOffset = (layoutInfo.viewportStartOffset+layoutInfo.viewportEndOffset)/2
            layoutInfo.visibleItemsInfo.minByOrNull { itemInfo->
                val itemCenter = itemInfo.offset + itemInfo.size/2
                abs(itemCenter - centeredOffset)
            }?.index
        }
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        state = listState
    ) {
        itemsIndexed(
            items = items,
            key = {index,item->item.id}
        ) { index,item->
            val animatedScale by animateFloatAsState(
                targetValue = if(centeredIndex == index) 1f else 0.95f,
                animationSpec = tween(300)
            )
            val animatedAlpha by animateFloatAsState(
                targetValue = if(centeredIndex == index) 1f else 0.8f,
                animationSpec = tween(300)
            )
            val animatedElevation by animateDpAsState(
                targetValue = if(centeredIndex == index) 8.dp else 0.dp,
                animationSpec = tween(300)
            )
            BookCard(
                item = item,
                onClick = {onClick(item)},
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                        alpha = animatedAlpha
                        shadowElevation = animatedElevation.toPx()
                    },
                imageHeight = 260.dp,
                elevation = 0.dp
            )
        }
    }
}

