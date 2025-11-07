package com.ptit.feature.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ptit.data.DisplayResult
import com.ptit.feature.component.CouponCard
import com.ptit.feature.viewmodel.CouponViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.component.CustomSearchBar
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickCouponScreen(
    navigateBack:()->Unit
){
    val viewModel = koinViewModel<CouponViewModel>()
    val coupons by viewModel.filterCoupons.collectAsState()
    val filterCode = viewModel.filterCode
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Choose coupon",
                        fontSize = FontSize.MEDIUM
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.selectCoupon(null)
                        navigateBack()
                    }) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back Icon"
                        )
                    }
                }
            )
        }
    ){padding->
        coupons.DisplayResult(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            },
            onError = {e->
                ErrorCard(message=e,modifier=Modifier.fillMaxSize())
            },
            onSuccess = {listCoupon->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        stickyHeader {
                            CustomSearchBar(
                                value = filterCode,
                                onValueChange = {viewModel.setFilterCde(it)},
                                placeholder = "Search by code",
                                onSearch = {viewModel.filter()},
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(Resources.Icon.Search),
                                        contentDescription = "Search",
                                        modifier = Modifier.clickable(onClick = {viewModel.filter()})
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(listCoupon) {coupon->
                            CouponCard(
                                coupon = coupon,
                                onClick = {
                                    viewModel.selectCoupon(coupon)
                                    navigateBack()
                                }
                            )
                        }
                    }

                }
            }
        )

    }
}