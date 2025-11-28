package com.ptit.feature.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.feature.domain.OrderEnum
import com.ptit.feature.viewmodel.OrderViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import org.koin.compose.viewmodel.koinViewModel
import com.ptit.data.DisplayResult
import com.ptit.feature.component.OrderCard
import com.ptit.shared.ButtonPrimary
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
    var cancelOrderDialogVisible by remember { mutableStateOf(false) }
    var selectedOrderId by remember { mutableIntStateOf(-1) }
    val context = LocalContext.current
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
                                onViewDetailClick = {navigateToOrderDetails(order.id)},
                                onCancelOrderClick = {
                                    cancelOrderDialogVisible = true
                                    selectedOrderId = order.id
                                },
                                onReviewClick = {},
                                orderEnum = OrderEnum.getOrderTabByIndex(selectedTabIndex)
                            )
                        }
                    }

                }
                AnimatedVisibility(visible = cancelOrderDialogVisible){
                    AlertDialog(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Are you sure you want to cancel this order?",
                                    fontSize = FontSize.REGULAR,
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                )
                                Icon(
                                    painter = painterResource(Resources.Icon.Close),
                                    contentDescription = "close",
                                    modifier = Modifier
                                        .clickable(onClick = {
                                            cancelOrderDialogVisible = false
                                        })
                                )
                            }
                        },
                        onDismissRequest = {},
                        confirmButton = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                OutlinedButton(
                                    onClick = {viewModel.cancelOrder(
                                        id = selectedOrderId,
                                        onSuccess = {
                                            cancelOrderDialogVisible = false
                                            Toast.makeText(context,"Cancel order successfully",Toast.LENGTH_SHORT).show()
                                        },
                                        onError = {e->
                                            Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
                                        }
                                    )}
                                ) {
                                    Text(
                                        text = "Yes"
                                    )
                                }
                                Spacer(modifier = Modifier.width(48.dp))
                                OutlinedButton(
                                    onClick = {
                                        cancelOrderDialogVisible = false
                                    },

                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = ButtonPrimary
                                    )
                                ) {
                                    Text(
                                        text = "No"
                                    )
                                }

                            }
                        }
                    )
                }
            }
        )
    }
}