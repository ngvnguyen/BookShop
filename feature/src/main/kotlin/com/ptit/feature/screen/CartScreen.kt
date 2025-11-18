package com.ptit.feature.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.ptit.data.DisplayResult
import com.ptit.data.model.cart.CartData
import com.ptit.feature.component.CartItemCard
import com.ptit.feature.viewmodel.HomeViewModel
import com.ptit.shared.Alpha
import com.ptit.shared.ButtonPrimary
import com.ptit.shared.FontSize
import com.ptit.shared.IconSecondary
import com.ptit.shared.QuantityCounterSize
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.CustomSearchBar
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.InfoCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import com.ptit.shared.component.QuantityCounter
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier=Modifier,
    navigateToCheckout:()->Unit,
    navigateToBookDetails:(Int)->Unit,
    navigateToPickAddress:()->Unit,
    navigateToPickCoupon:()->Unit,
    navigateToOrder:()->Unit,
    viewModel: HomeViewModel
) {
    val focusManager = LocalFocusManager.current
    val searchQuery by viewModel.cartSearchQuery.collectAsState()
    val cartItemsFiltered by viewModel.cartItemFilter.collectAsState()
    val isCartEmpty by viewModel.isCartEmpty.collectAsState()
    val selectedIds = viewModel.selectedIds
    val context = LocalContext.current
    val totalPrice by remember {
        derivedStateOf {
            if (cartItemsFiltered.isSuccess()){
                cartItemsFiltered.getSuccessData().sumOf { if (selectedIds.contains(it.id)) it.finalPrice*it.quantity else 0.0}
            }else 0.0
        }
    }
    val isRefreshing = viewModel.isRefreshing
    var isFocused by remember { mutableStateOf(false) }
    val address by viewModel.address.collectAsState()
    val couponCode = viewModel.couponCode

    Scaffold(
        containerColor = SurfaceDarker,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    CustomSearchBar(
                        value = searchQuery,
                        onValueChange = {viewModel.updateCartSearchQuery(it)},
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
                                    .clickable(onClick = {})
                            )
                        },
                        placeholder = "Search cart items",
                        modifier = Modifier.padding(end=14.dp)
                    )
                },
                actions = {
                    TextButton(
                        onClick = navigateToOrder,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "My Orders"
                        )
                    }
                }
            )

        }
    ){padding->
        val state = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                viewModel.cartRefresh()
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
            cartItemsFiltered.DisplayResult(
                modifier = Modifier
                    .clickable(
                        onClick = {focusManager.clearFocus()},
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                onSuccess = {cartItems->
                    AnimatedContent(
                        targetState = isCartEmpty
                    ) { isEmpty->
                        if (isEmpty){
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                item {
                                    InfoCard(
                                        title = "Your cart is empty",
                                        subtitle = "Add items to your cart to see them here",
                                        image = Resources.Image.ShoppingCart,
                                    )
                                }
                            }
                        }else{
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    stickyHeader {
                                        Row(
                                            modifier = Modifier
                                                .padding(start = 12.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Checkbox(
                                                checked = selectedIds.size == cartItems.size,
                                                onCheckedChange = {isChecked->
                                                    if (isChecked) viewModel.selectAllCartItem()
                                                    else viewModel.clearAllCartItemSelection()
                                                },
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Text(text = "Select all")
                                        }
                                    }
                                    items(
                                        items = cartItems,
                                        key = {it.id}
                                    ) {item->
                                        CartItemCard(
                                            item = item,
                                            onItemClick = {navigateToBookDetails(item.productId)},
                                            checked = selectedIds.contains(item.id),
                                            onCheckedChange = {isChecked->
                                                if (isChecked) viewModel.selectCartItem(item.id)
                                                else viewModel.removeCartItemSelection(item.id)
                                            },
                                            onMinusClick = {q->
                                                viewModel.updateCartItemQuantity(
                                                    productId = item.id,
                                                    quantity = q,
                                                    onSuccess = {},
                                                    onError = {e->
                                                        Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
                                                    }
                                                )
                                            },
                                            onPlusClick = {q->
                                                viewModel.updateCartItemQuantity(
                                                    productId = item.id,
                                                    quantity = q,
                                                    onSuccess = {},
                                                    onError = {e->
                                                        Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
                                                    }
                                                )

                                            },
                                            onDeleteClick = {
                                                viewModel.deleteCartItem(
                                                    productId = item.id,
                                                    onSuccess = {},
                                                    onError = {e->
                                                        Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
                                                    }
                                                )
                                            }
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(onClick = {navigateToPickCoupon()})
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Coupon",
                                            color = TextSecondary,
                                            fontSize = FontSize.REGULAR
                                        )
                                        if (couponCode == null){
                                            Text(
                                                text = "Select",
                                                color = Color.Black.copy(alpha = Alpha.HALF),
                                                fontSize = FontSize.REGULAR
                                            )
                                        }else{
                                            Text(
                                                text = couponCode,
                                                color = Color.Black.copy(alpha = Alpha.HALF),
                                                fontSize = FontSize.REGULAR
                                            )
                                        }
                                    }

                                    HorizontalDivider(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(onClick = navigateToPickAddress)
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Address",
                                            color = TextSecondary,
                                            fontSize = FontSize.REGULAR,
                                            modifier = Modifier.weight(2f)
                                        )
                                        address?.let {
                                            Column(
                                                modifier = Modifier.weight(5f),
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = "${it.ward}, ${it.district}, ${it.city}",
                                                    color = Color.Black.copy(alpha = Alpha.HALF),
                                                    fontSize = FontSize.SMALL,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = it.street,
                                                    color = Color.Black.copy(alpha = Alpha.HALF),
                                                    fontSize = FontSize.SMALL,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }

                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                                    )
                                    Row(
                                        modifier = Modifier
                                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Subtotal: ",
                                            color = TextSecondary,
                                            fontSize = FontSize.REGULAR
                                        )
                                        Text(
                                            text = "$totalPrice ₫",
                                            color = TextSecondary,
                                            fontSize = FontSize.EXTRA_REGULAR,
                                            fontWeight = FontWeight.W600
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Button(
                                            onClick = {
                                                viewModel.checkout()
                                                navigateToCheckout()
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = ButtonPrimary
                                            ),
                                            enabled = selectedIds.isNotEmpty()
                                        ) {
                                            Text(
                                                text = "Check out",
                                                fontSize = FontSize.REGULAR
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }

                },
                onError = {e->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        item {
                            ErrorCard(
                                message = e
                            )
                        }
                    }
                },
                onLoading = {
                    LoadingCard(modifier = Modifier.fillMaxSize())
                }
            )
        }

    }
}
