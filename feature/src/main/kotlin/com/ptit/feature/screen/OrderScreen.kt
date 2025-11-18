package com.ptit.feature.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.feature.domain.OrderEnum
import com.ptit.feature.viewmodel.OrderViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceLighter
import org.koin.compose.viewmodel.koinViewModel
import com.ptit.data.DisplayResult
import com.ptit.feature.component.OrderCard
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.TextPrimary
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navigateBack:()->Unit,
    navigateToOrderDetails:(Int)->Unit
){
    val viewModel = koinViewModel<OrderViewModel>()
    val tabs = OrderEnum.entries
    val selectedTabIndex by viewModel.filterOrderIndex.collectAsState()
    val filteredOrder by viewModel.ordersFilter.collectAsState()
    Scaffold(
        containerColor = SurfaceDarker,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My orders",
                        fontSize = FontSize.MEDIUM
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back"
                        )
                    }
                }
            )

        }
    ){padding->
        filteredOrder.DisplayResult(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding()-10.dp,
                    bottom = padding.calculateBottomPadding()
                ),
            onError = {e->
                ErrorCard(
                    message = e,
                    modifier = Modifier.fillMaxSize()
                )
            },
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            },
            onSuccess = {orders->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SecondaryScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        edgePadding = 0.dp
                    ) {
                        tabs.forEachIndexed {index,tab->
                            Tab(
                                selected = index == selectedTabIndex,
                                onClick ={
                                    viewModel.setFilterOrderIndex(index)
                                },
                                text = {
                                    Text(
                                        text = tab.title,
                                        fontSize = FontSize.REGULAR,
                                        maxLines = 1,
                                        color = if (index == selectedTabIndex) TextSecondary else TextPrimary
                                    )
                                }
                            )
                        }
                    }
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        items(
                            items = orders,
                            key = {it.id}
                        ) {order->
                            OrderCard(
                                order = order,
                                onClick = {},
                                orderEnum = OrderEnum.getOrderTabByIndex(selectedTabIndex)
                            )
                        }
                    }

                }
            }
        )
    }
}