package com.ptit.feature.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.feature.domain.opposite
import com.ptit.feature.form.CategoryForm
import com.ptit.feature.viewmodel.HomeViewModel
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.TextSecondary
import com.ptit.shared.bebasNeueFont
import com.ptit.shared.component.CustomSearchBar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onDrawerClick:()->Unit
){
    val homeViewModel = koinViewModel<HomeViewModel>()
    var searchBarOpened by remember{mutableStateOf(false) }
    val focusRequester = remember {
        FocusRequester()
    }
    val bookPaged by homeViewModel.bookPaged.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(searchBarOpened) {
        if (searchBarOpened) focusRequester.requestFocus()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxWidth(),
        topBar = {
            AnimatedContent(
                targetState = searchBarOpened
            ) {visible->
                if (visible){
                    SearchBar(
                        inputField = {
                            CustomSearchBar(
                                value = homeViewModel.searchQuery,
                                onValueChange = homeViewModel::updateSearchQuery,
                                trailingIcon = {
                                    IconButton(onClick = {searchBarOpened = false}) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Close),
                                            contentDescription = "Close search bar"
                                        )
                                    }
                                },
                                focusRequester = focusRequester,
                                onSearch = {}
                            )
                        },
                        expanded = false,
                        onExpandedChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        content = {},
                        colors = SearchBarDefaults.colors(
                            containerColor = Color.Unspecified
                        )
                    )
                }else{
                    TopAppBar(
                        title = {
                            Text(
                                text = "BookShop",
                                fontFamily = bebasNeueFont(),
                                fontSize = FontSize.LARGE,
                                color = TextSecondary
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
                            IconButton(onClick = {searchBarOpened = true}) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Search),
                                    contentDescription = "Search icon"
                                )
                            }
                        }
                    )
                }

            }

        }
    ) { padding->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding()
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                item {
                    if (bookPaged.isSuccess()){
                        Column {
                            Row {
                                VerticalDivider(modifier = Modifier.width(2.dp))
                                Text(text = "New")
                            }
                            HorizontalBookCarousel(
                                items = bookPaged.getSuccessData(),
                                onClick = {
                                    Toast.makeText(context,it.name,Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                    }
                }
            }


        }
    }
}

@Composable
fun CategoryChipRow(
    modifier : Modifier = Modifier,
    categories: List<CategoryForm>,
    onClick:(List<CategoryForm>)->Unit
){
    val categoriesSelected = remember { mutableStateListOf<CategoryForm>() }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(
            items = categories,
            key = {it.id}
        ) {category->
            CategoryChip(
                category = category,
                onClick = {
                    if (categoriesSelected.contains(category)){
                        categoriesSelected.remove(category)
                    }else{
                        categoriesSelected.add(category)
                    }
                    onClick(categoriesSelected)
                },
                selected = categoriesSelected.contains(category)
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
            borderColor = Color.Black.copy(alpha = Alpha.HALF),
            selectedBorderColor = IconSecondary.copy(alpha = Alpha.EIGHTY_PERCENT),
            enabled = true,
            selected = selected
        )
    )
}