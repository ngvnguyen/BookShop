package com.ptit.feature.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.ptit.feature.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ptit.feature.component.BookCard
import com.ptit.feature.component.CustomDrawer
import com.ptit.feature.component.NavigationItem
import com.ptit.feature.domain.CustomDrawerItem
import com.ptit.feature.domain.DrawerState
import com.ptit.feature.domain.NavigationEnum
import com.ptit.feature.domain.isOpened
import com.ptit.feature.domain.opposite
import com.ptit.feature.form.BookForm
import com.ptit.feature.form.CategoryForm
import com.ptit.feature.navigation.Screen
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.Surface
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.bebasNeueFont
import com.ptit.shared.component.CustomSearchBar
import kotlinx.coroutines.delay
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen(
    modifier: Modifier = Modifier,
    navigateToAuthScreen:()-> Unit,
    navigateToAdminScreen:()->Unit,
    navigateToProfileScreen:()->Unit
){
    val context = LocalContext.current
    val homeViewModel = koinViewModel<HomeViewModel>()
    var drawerState by remember { mutableStateOf(DrawerState.Closed) }

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val offsetValue = (screenWidth/1.5).dp
    val animatedBackground by animateColorAsState(
        targetValue = if (drawerState.isOpened()) SurfaceLighter else Surface
    )
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f
    )
    val animatedRadius by animateDpAsState(
        targetValue = if (drawerState.isOpened()) 20.dp else 0.dp
    )

    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()


    val selectedDestination by remember {
        derivedStateOf {
            val route = currentRoute?.destination?.route.toString()
            when{
                route.contains(NavigationEnum.Book.screen.javaClass.simpleName)-> NavigationEnum.Book
                route.contains(NavigationEnum.Cart.screen.javaClass.simpleName)-> NavigationEnum.Cart
                else ->NavigationEnum.Home
            }
        }

    }


    Box(
        modifier = modifier
            .background(color = animatedBackground)
            .fillMaxSize()
            //.systemBarsPadding()
    ){
        if (drawerState.isOpened()){
            CustomDrawer(
                isAdmin = homeViewModel.isAdminUnlocked,
                onClick = {drawerItem->
                    when(drawerItem){
                        CustomDrawerItem.Profile -> navigateToProfileScreen()
                        CustomDrawerItem.Logout ->{
                            homeViewModel.signOut(
                                onSuccess = {message->
                                    navigateToAuthScreen()
                                },
                                onError = {message->
                                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                        CustomDrawerItem.Admin -> {
                            navigateToAdminScreen()
                        }

                        CustomDrawerItem.Category -> {

                        }
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(animatedScale)
                .clip(RoundedCornerShape(animatedRadius))
                .offset(x = animatedOffset)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(animatedRadius)
                )
        ){
            Column {

                NavHost(
                    navController = navController,
                    startDestination = Screen.HomeGraph.Home,
                    modifier = Modifier.weight(1f)
                ){
                    composable<Screen.HomeGraph.Home> {
                        HomeScreen(
                            onDrawerClick = {drawerState = drawerState.opposite()}
                        )
                    }
                    composable<Screen.HomeGraph.Book> {

                    }

                    composable<Screen.HomeGraph.Cart> {

                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceDarker)
                        .padding(horizontal = 18.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NavigationEnum.entries.forEach {navEnum->
                        NavigationItem(
                            navigationEnum = navEnum,
                            onClick = {
                                navController.navigate(navEnum.screen){
                                    launchSingleTop = true
                                    popUpTo<Screen.HomeGraph.Home>{
                                        inclusive = false
                                        saveState = true
                                    }
                                    restoreState = true
                                }
                            },
                            isSelect = navEnum == selectedDestination,
                            needBadge = navEnum == NavigationEnum.Cart
                        )
                    }
                }


            }

        }
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
                cardHeight = 260.dp,
                elevation = 0.dp
            )
        }
    }
}