package com.ptit.feature.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ptit.data.DisplayResult
import com.ptit.feature.viewmodel.ChangePasswordViewModel
import com.ptit.feature.viewmodel.ResetPasswordViewModel
import com.ptit.shared.Alpha
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.component.CustomTextField
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.component.ErrorCard
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navigateBack:()->Unit
) {
    val viewModel = koinViewModel<ChangePasswordViewModel>()
    val context = LocalContext.current
    val snackBarHostState = remember{
        SnackbarHostState()
    }
    val changePasswordForm = viewModel.changePasswordForm
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Change Password",
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
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 12.dp
                )
        ) {
            CustomTextFieldWithLabel(
                value = changePasswordForm.currentPassword,
                onValueChange = viewModel::updateCurrentPassword,
                label = "Current password"
            )
            CustomTextFieldWithLabel(
                value = changePasswordForm.newPassword,
                onValueChange = viewModel::updateNewPassword,
                label = "New password"
            )
            CustomTextFieldWithLabel(
                value = changePasswordForm.confirmNewPassword,
                onValueChange = viewModel::updateConfirmNewPassword,
                label = "Confirm password"
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column {
                Row {
                    Text(
                        text = if (viewModel.isValidPassword) "✓" else "✗ ",
                        color = if (viewModel.isValidPassword) Color.Green else Color.Red
                    )
                    Text(
                        text = if (viewModel.isValidPassword) " Password meets criteria"
                        else " Password must be at least 8 characters long, contain an uppercase letter, a number, and a special character",
                        fontSize = FontSize.SMALL,
                        color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                    )
                }
                Row {
                    Text(
                        text = if (viewModel.isPassMatchConfirmPass) "✓" else "✗ ",
                        color = if (viewModel.isPassMatchConfirmPass) Color.Green else Color.Red
                    )
                    Text(
                        text = if (viewModel.isPassMatchConfirmPass) " Password matches"
                        else " Password must match confirm password",
                        fontSize = FontSize.SMALL,
                        color = Color.Black.copy(alpha = Alpha.TWENTY_PERCENT)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = "Change Password",
                onClick = {
                    focusManager.clearFocus()
                    viewModel.changePassword(
                        onSuccess = {message->
                            snackBarHostState.showSnackbar(message)
                        },
                        onError = {message->
                            snackBarHostState.showSnackbar(message)
                        }
                    )
                },
                enabled = viewModel.isValidPassword && viewModel.isPassMatchConfirmPass,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
