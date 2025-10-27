package com.ptit.feature.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ptit.data.DisplayResult
import com.ptit.feature.viewmodel.ResetPasswordViewModel
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.component.CustomTextField
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    navigateBack:()->Unit,
    navigateToAuth:()->Unit
) {
    val resetPasswordViewModel = koinViewModel<ResetPasswordViewModel>()
    val context = LocalContext.current
    val snackBarHostState = remember{
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reset Password",
                        fontSize = FontSize.EXTRA_MEDIUM
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
        },
        snackbarHost ={
            SnackbarHost(
                hostState = snackBarHostState
            ){data->
                Snackbar(
                    snackbarData = data
                )
            }
        }
    ) { padding->
        resetPasswordViewModel.sendOTPState.DisplayResult(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            onSuccess = {
                resetPasswordViewModel.verifyOTPState.DisplayResult(
                    onIdle = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(
                                horizontal = 12.dp
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text =" Please enter the OTP sent to your email ",
                                fontSize = FontSize.SMALL
                            )
                            CustomTextField(
                                value = resetPasswordViewModel.otp,
                                onValueChange = resetPasswordViewModel::changeOTP,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                placeholder = "Enter OTP",
                                modifier = Modifier.widthIn(128.dp),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                fillMaxWidth = false
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                            PrimaryButton(
                                text = "Verify",
                                onClick = resetPasswordViewModel::verifyOTP
                            )
                        }
                    },
                    onSuccess = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(
                                horizontal = 12.dp
                            )
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            CustomTextField(
                                value = resetPasswordViewModel.newPassword,
                                onValueChange = resetPasswordViewModel::changeNewPassword,
                                placeholder = "New password(minimum 6 characters)"
                            )
                            CustomTextField(
                                value = resetPasswordViewModel.confirmNewPassword,
                                onValueChange = resetPasswordViewModel::changeConfirmNewPassword,
                                placeholder = "Confirm new password"
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            PrimaryButton(
                                text = "Change Password",
                                onClick = {
                                    resetPasswordViewModel.changePassword(
                                        onSuccess = {message->
                                            coroutineScope.launch {
                                                navigateToAuth()
                                                snackBarHostState.showSnackbar(
                                                    message = message,
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        },
                                        onError = {message->
                                            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                },
                                enabled = resetPasswordViewModel.isPasswordCorrect
                            )
                        }
                    },
                    onError = {message->
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ErrorCard(message = message)
                            Spacer(modifier = Modifier.height(24.dp))
                            PrimaryButton(
                                text = "Retry OTP",
                                onClick = resetPasswordViewModel::resetVerifyOTP
                            )
                        }
                    },
                    onLoading = {
                        LoadingCard()
                    }
                )
            },
            onError = {message->
                ErrorCard(message = message)
            },
            onLoading = {
                LoadingCard(
                    modifier = Modifier.fillMaxSize()
                )
            }
        )
    }
}

