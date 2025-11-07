package com.ptit.feature.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import coil.compose.rememberAsyncImagePainter
import com.ptit.data.DisplayResult
import com.ptit.data.RequestState
import com.ptit.feature.domain.Gender
import com.ptit.feature.viewmodel.Action

import com.ptit.feature.viewmodel.ProfileViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.GrayDarker
import com.ptit.shared.IconSecondary
import com.ptit.shared.Resources
import com.ptit.shared.component.CustomTextField
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateBack:()-> Unit
){
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel = koinViewModel<ProfileViewModel>()
    val profileForm = viewModel.profileForm
    val profileState by viewModel.profileState.collectAsState()
    var datePickerOpened by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var dropdownMenuOpened by remember { mutableStateOf(false)}
    val context = LocalContext.current
    val pickImageForResult = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {uri->
        uri?.let {
            viewModel.updateAvatar(
                uri = it,
                context = context,
                onSuccess = {

                },
                onError = {e->
                    snackbarHostState.showSnackbar(e)
                    println(e)
                }
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontSize = FontSize.MEDIUM
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "back",
                            tint = IconSecondary
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { padding ->
        profileState.DisplayResult(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            onSuccess = {
                Column(
                    modifier = Modifier
                        .padding(
                            vertical = 12.dp,
                            horizontal = 12.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(160.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(6.dp),
                                color = Color.Black
                            )


                    ){
                        val uploadState = viewModel.uploadState
                        when{
                            uploadState.isSuccess() || uploadState.isIdle()->{
                                Image(
                                    painter = rememberAsyncImagePainter(profileForm.avatarUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable(onClick = {
                                            pickImageForResult.launch("image/*")
                                        }),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            uploadState.isLoading()->{
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                            uploadState.isError()->{
                                IconButton(onClick = {viewModel.resetUploadState()}) {
                                    Icon(
                                        painter = painterResource(Resources.Icon.Replay),
                                        contentDescription = "Retry upload",
                                    )
                                }
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    CustomTextFieldWithLabel(
                        value = profileForm.name,
                        onValueChange = viewModel::updateName,
                        label = "Name"
                    )
                    CustomTextFieldWithLabel(
                        value = profileForm.email,
                        onValueChange = {},
                        enabled = false,
                        label="Email"
                    )
                    CustomTextFieldWithLabel(
                        value = profileForm.phone,
                        onValueChange = viewModel::updatePhone,
                        label = "Phone number",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        )
                    )

                    Row {
                        CustomTextFieldWithLabel(
                            value = viewModel.getDateString(profileForm.dateOfBirth),
                            onValueChange = {},
                            enabled = false,
                            trailingIcon = {
                                IconButton(onClick = {datePickerOpened = !datePickerOpened}) {
                                    Icon(
                                        painter = if (datePickerOpened) painterResource(Resources.Icon.ArrowUp)
                                        else painterResource(Resources.Icon.ArrowDown),
                                        contentDescription = "Expand date picker"
                                    )
                                }
                            },
                            label = "Birthday",
                            modifier = Modifier.weight(5f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier.weight(3f)
                        ){
                            CustomTextFieldWithLabel(
                                value = profileForm.gender.name,
                                onValueChange = {},
                                enabled = false,
                                trailingIcon = {
                                    IconButton(onClick = {dropdownMenuOpened = !dropdownMenuOpened}) {
                                        Icon(
                                            painter = if (dropdownMenuOpened) painterResource(Resources.Icon.ArrowUp)
                                            else painterResource(Resources.Icon.ArrowDown),
                                            contentDescription = "Expand Gender"
                                        )
                                    }
                                },
                                label = "Gender"
                            )
                            DropdownMenu(
                                expanded = dropdownMenuOpened,
                                onDismissRequest = {dropdownMenuOpened = false}
                            ) {
                                Gender.entries.forEach {gender->
                                    DropdownMenuItem(
                                        text = { Text(gender.name) },
                                        onClick = {
                                            viewModel.updateGender(gender)
                                            dropdownMenuOpened = false
                                        }
                                    )
                                }
                            }
                        }
                    }



                    Spacer(modifier = Modifier.weight(1f))

                    PrimaryButton(
                        text = "Update",
                        onClick = {viewModel.updateProfile(
                            onError = {e->
                                snackbarHostState.showSnackbar("e")
                            },
                            onSuccess = {
                                snackbarHostState.showSnackbar("Updated")
                            }
                        )},
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (datePickerOpened)
                    DatePickerDialog(
                        onDismissRequest = {datePickerOpened = false},
                        confirmButton = {
                            Button(
                                onClick = {
                                    datePickerState.selectedDateMillis?.let{
                                        viewModel.updateDateOfBirth(it)
                                    }
                                    datePickerOpened = false
                                }
                            ) {
                                Text("Select")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {datePickerOpened = false},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GrayDarker
                                )
                            ) {
                                Text("Cancel")
                            }
                        }
                    ){
                        DatePicker(state = datePickerState)
                    }

            },
            onError = {e->
                ErrorCard(message = e)
            },
            onLoading = {
                LoadingCard()
            }
        )
    }
}