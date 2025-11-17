package com.ptit.feature.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.data.DisplayResult
import com.ptit.feature.component.BookCard
import com.ptit.feature.component.FilterDrawer
import com.ptit.feature.domain.DrawerState
import com.ptit.feature.domain.isOpened
import com.ptit.feature.domain.opposite
import com.ptit.feature.form.CategoryForm
import com.ptit.feature.viewmodel.HomeViewModel
import com.ptit.shared.Alpha
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceBrand
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.component.BottomPageSelect
import com.ptit.shared.component.CustomSearchBar
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.InfoCard
import com.ptit.shared.component.LoadingCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    modifier: Modifier=Modifier,
    navigateToBookDetails:(Int)->Unit,
    onDrawerClick:()->Unit,
    viewModel: HomeViewModel
){
    val bookPaged by viewModel.bookPaged.collectAsState()
    val context = LocalContext.current

    var drawerState by remember { mutableStateOf(DrawerState.Closed) }
    val coroutineScope = rememberCoroutineScope()
    val filterPriceItem = viewModel.filterPriceItem
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    val isRefreshing = viewModel.isRefreshing
    val categories by viewModel.allCategory.collectAsState()
    val bookFilter by viewModel.bookFilter.collectAsState()
    val bookFilterName = viewModel.bookName
    val page by viewModel.currentPage.collectAsState()
    val maxPage by viewModel.maxPage.collectAsState()
    Box(modifier = modifier.fillMaxSize()){
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
                            value = bookFilterName,
                            onValueChange = {viewModel.updateBookFilterName(it)},
                            onSearch = {
                                viewModel.submitBookFilterName()
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
                                        .clickable(onClick={viewModel.submitBookFilterName()})
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
                    viewModel.bookRefresh()
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
            ){
                Column(modifier = Modifier.fillMaxSize()) {
                    if (categories.isSuccess()) {
                        CategoryChipRow(
                            categories = categories.getSuccessData(),
                            onClick = {
                                viewModel.onBookFilterCategory(it.name)
                            },
                            selectedCategory = bookFilter.category,
                            modifier = Modifier
                                .background(SurfaceLighter)
                                .padding(start = 12.dp)
                        )
                    }
                    bookPaged.DisplayResult(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                onClick = {focusManager.clearFocus()},
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ),
                        onError = {e->
                            ErrorCard(message= e,modifier = Modifier.fillMaxSize())
                        },
                        onLoading = {
                            LoadingCard(modifier = Modifier.fillMaxSize())
                        },
                        onSuccess = {listBook->
                            AnimatedContent(
                                targetState = listBook
                            ) { books->
                                if (books.isEmpty()){
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        item {
                                            InfoCard(
                                                image = Resources.Image.Cat,
                                                title = "No Books Found",
                                                subtitle = "Try searching another"
                                            )
                                        }
                                    }
                                }else{
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        items(
                                            items = books.chunked(3)
                                        ) {bookChunked->
                                            Row(
                                                modifier = Modifier
                                                    .padding(horizontal = 8.dp)
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                bookChunked.forEach {
                                                    BookCard(
                                                        item = it,
                                                        onClick = {
                                                            navigateToBookDetails(it.id)
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }


                            }
                        }
                    )
                    if (maxPage.isSuccess()){
                        val pages = maxPage.getSuccessData()
                        BottomPageSelect(
                            page = page,
                            maxPage = pages,
                            onForwardSelect = {viewModel.updateBookFilterPage(pages)},
                            onRewindSelect = {viewModel.updateBookFilterPage(1)},
                            onPageSelect = {viewModel.updateBookFilterPage(it)}
                        )
                    }
                }

            }


        }
        FilterDrawer(
            visible = drawerState.isOpened(),
            onCloseClick = {drawerState = drawerState.opposite()},
            onFilterClick = {
                viewModel.submitFilter()
                drawerState = drawerState.opposite()
            },
            checkedItem = filterPriceItem,
            onSelectFilterPriceItem = {isChecked,filterPriceItem->
                if (!isChecked) viewModel.selectFilterPriceItem(null)
                else viewModel.selectFilterPriceItem(filterPriceItem)
            }
        )
    }
}


@Composable
fun CategoryChipRow(
    modifier : Modifier = Modifier,
    categories: List<CategoryForm>,
    selectedCategory: String,
    onClick:(CategoryForm)->Unit
){
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
    ) {
        items(
            items = categories,
            key = {it.id}
        ) {category->
            CategoryChip(
                category = category,
                onClick = {
                    onClick(category)
                },
                selected = category.name == selectedCategory
            )
        }
    }
}

@Composable
fun CategoryChip(
    category: CategoryForm,
    onClick:()->Unit,
    selected:Boolean
){
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text=category.name
            )
        },
        border = FilterChipDefaults.filterChipBorder(
            borderColor = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT),
            selectedBorderColor = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT),
            enabled = true,
            selected = selected
        ),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SurfaceBrand
        )
    )
}