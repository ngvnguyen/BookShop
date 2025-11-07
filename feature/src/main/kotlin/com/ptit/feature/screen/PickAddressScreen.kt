package com.ptit.feature.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ptit.feature.viewmodel.AddressViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.ptit.data.DisplayResult
import com.ptit.feature.component.AddressCard
import com.ptit.feature.form.AddressForm
import com.ptit.shared.Alpha
import com.ptit.shared.ButtonPrimary
import com.ptit.shared.IconPrimary
import com.ptit.shared.IconSecondary
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextSecondary
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickAddressScreen(
    navigateBack:(Int)->Unit
){
    val viewModel = koinViewModel<AddressViewModel>()
    val allAddress by viewModel.allAddress.collectAsState()
    val context = LocalContext.current
    val selectedId = viewModel.selectedAddressId
    var showDialog by remember { mutableStateOf(false) }
    val isEdit = viewModel.isEdit
    var offset by remember { mutableStateOf(Offset(0)) }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Choose address",
                        fontSize = FontSize.MEDIUM
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigateBack(-1)
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
        allAddress.DisplayResult(
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
            onSuccess = {listAddress->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(
                            items = listAddress,
                            key = {it.id}
                        ) {address->
                            var expanded by remember { mutableStateOf(false) }
                            Box(){
                                AddressCard(
                                    address = address,
                                    isSelected = (selectedId==-1 && address.isDefault) || (selectedId == address.id),
                                    onClick = {
                                        viewModel.selectAddressId(address.id)
                                    },
                                    onLongClick = {o->
                                        offset = o
                                        expanded = true
                                    }
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    containerColor = SurfaceLighter,
                                    offset = DpOffset(
                                        x = (screenWidth*2/3).dp,
                                        y = -100.dp
                                    )
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "Edit"
                                            )
                                        },
                                        onClick = {
                                            viewModel.selectAddress(address)
                                            viewModel.edit()
                                            showDialog = true
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "Delete"
                                            )
                                        },
                                        onClick = {

                                        }
                                    )
                                }
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = {
                                        viewModel.create()
                                        showDialog = true
                                    })
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Plus),
                                    contentDescription = "add icon",
                                    modifier = Modifier.size(12.dp),
                                    tint = IconSecondary
                                )
                                Text(
                                    text = " Add new address",
                                    color = TextSecondary.copy(alpha = Alpha.HALF),
                                    fontSize = FontSize.SMALL
                                )
                            }
                        }
                        item {
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp
                            )
                        }
                    }

                    PrimaryButton(
                        text = "Select",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        onClick = {
                            viewModel.submitAddress(
                                onSuccess = {
                                    navigateBack(selectedId)
                                },
                                onError = {e->
                                    Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )
                }
            }
        )

        AnimatedVisibility(visible = showDialog) {
            ModifyAddressDialog(
                onDismiss = {showDialog = false},
                onSubmit = {
                    if (isEdit) viewModel.updateAddress(
                        onSuccess = {
                            showDialog = false
                        },
                        onError = {e->
                            Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
                        }
                    )
                    else viewModel.createAddress(
                        onSuccess = {
                            showDialog = false
                        },
                        onError = {e->
                            Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                address = viewModel.addressForm,
                isEdit = isEdit,
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyAddressDialog(
    onDismiss:()->Unit,
    onSubmit:()->Unit,
    address: AddressForm,
    isEdit:Boolean,
    viewModel: AddressViewModel
){
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        containerColor = SurfaceLighter,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(8.dp),
        confirmButton = {},
        title = {
            Column {
                Text(
                    text = if(isEdit) "Edit address" else "Add new address",
                    fontSize = FontSize.MEDIUM,
                    color = TextSecondary
                )
                Text(
                    text = "Please fill in the information below",
                    fontSize = FontSize.SMALL,
                    color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomTextFieldWithLabel(
                    value = address.receiverName,
                    onValueChange = {
                        viewModel.updateReceiverName(it)
                    },
                    label = "Receiver name",
                    placeholder = "Receiver name"
                )
                CustomTextFieldWithLabel(
                    value = address.phone,
                    onValueChange = {
                        viewModel.updatePhone(it)
                    },
                    label = "Phone",
                    placeholder = "Phone number",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    )
                )
                CustomTextFieldWithLabel(
                    value = address.city,
                    onValueChange = {
                        viewModel.updateCity(it)
                    },
                    label = "City",
                    placeholder = "City"
                )
                CustomTextFieldWithLabel(
                    value = address.district,
                    onValueChange = {
                        viewModel.updateDistrict(it)
                    },
                    label = "District",
                    placeholder = "District"
                )
                CustomTextFieldWithLabel(
                    value = address.ward,
                    onValueChange = {
                        viewModel.updateWard(it)
                    },
                    label = "Ward",
                    placeholder = "Ward"
                )
                CustomTextFieldWithLabel(
                    value = address.street,
                    onValueChange = {
                        viewModel.updateStreet(it)
                    },
                    label = "Street",
                    placeholder = "Street"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Set as default address",
                        color = TextSecondary
                    )
                    Switch(
                        checked = address.isDefault,
                        onCheckedChange = {
                            viewModel.updateIsDefault(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ButtonPrimary,
                            checkedTrackColor = IconSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                PrimaryButton(
                    text = if(isEdit) "Save changes" else "Add address",
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
    )
}