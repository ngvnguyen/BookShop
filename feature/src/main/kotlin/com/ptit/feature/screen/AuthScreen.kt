package com.ptit.feature.screen


import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ptit.feature.viewmodel.AuthViewModel
import com.ptit.shared.component.CustomTextField
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.ptit.data.DisplayResult
import com.ptit.feature.viewmodel.AuthTarget
import com.ptit.feature.viewmodel.isLogin
import com.ptit.feature.viewmodel.isSignUp
import com.ptit.feature.viewmodel.opposite
import com.ptit.shared.Alpha
import com.ptit.shared.Black
import com.ptit.shared.CategoryPurple
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.SurfaceLighter
import com.ptit.shared.TextPrimary
import com.ptit.shared.TextSecondary
import com.ptit.shared.bebasNeueFont
import com.ptit.shared.component.LoadingCard
import com.ptit.shared.component.PrimaryButton
import com.ptit.feature.navigation.Screen
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.robotoCondensedFont
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    navigateToHome:()->Unit,
    navigateToForgotPassword:()->Unit
){
    val viewModel = koinViewModel<AuthViewModel>()
    var showPassword by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val loginStatus by viewModel.loginStatus.collectAsState()
    LaunchedEffect(loginStatus) {
        if (loginStatus.isSuccess()) {
            navigateToHome()
        }
        if (loginStatus.isError()){
            Toast.makeText(context,loginStatus.getErrorMessage(), Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title={
                    Text(
                        text = "Book Shop",
                        fontSize = FontSize.EXTRA_LARGE,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        fontFamily = bebasNeueFont()
                    )
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()

    ) {padding->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
        ){
            AnimatedContent(viewModel.authTarget) {target->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Welcome Back!",
                        fontSize = FontSize.LARGE,
                        color = TextPrimary,
                        fontFamily = robotoCondensedFont()
                    )
                    Text(
                        text = if (target.isLogin()) "Login to continue"
                            else "Create an account to continue",
                        fontSize = FontSize.EXTRA_REGULAR,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (target.isSignUp()){
                            CustomTextFieldWithLabel(
                                value = viewModel.getName(),
                                onValueChange = viewModel::changeName,
                                placeholder = "Your name",
                                label = "Name"
                            )
                        }
                        CustomTextFieldWithLabel(
                            value = viewModel.getEmail(),
                            onValueChange = viewModel::changeEmail,
                            label = "Email",
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Email
                            ),
                            placeholder = "Your email address"
                        )

                        if (target.isSignUp()){
                            CustomTextFieldWithLabel(
                                value = viewModel.getPhone(),
                                onValueChange = viewModel::changePhone,
                                label = "Phone",
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Phone
                                ),
                                placeholder = "Your phone number"
                            )
                        }
                        CustomTextFieldWithLabel(
                            value = viewModel.getPassword(),
                            label = "Password",
                            onValueChange = viewModel::changePassword,
                            placeholder = if (target.isLogin()) "Your password"
                                else "Min 6 characters",
                            visualTransformation = if (showPassword) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            trailingIcon = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(end = 12.dp)
                                ) {
                                    IconButton(
                                        onClick = {showPassword = !showPassword}
                                    ) {
                                        Icon(
                                            painter = painterResource(
                                                if (showPassword) Resources.Icon.VisibilityOff
                                                else Resources.Icon.Visibility
                                            ),
                                            contentDescription = "Toggle password visibility"
                                        )
                                    }
                                    if (target.isLogin()){
                                        VerticalDivider(
                                            modifier = Modifier.height(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Forgot?",
                                            color = TextSecondary,
                                            modifier = Modifier.clickable{
                                                navigateToForgotPassword()
                                            }
                                        )
                                    }
                                }
                            }
                        )
                        if (target.isSignUp()){
                            CustomTextFieldWithLabel(
                                value = viewModel.getConfirmPassword(),
                                onValueChange = viewModel::changeConfirmPassword,
                                label = "Confirm Password",
                                placeholder = "Confirm Password",
                                visualTransformation = if (showPassword) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(
                                        onClick = {showPassword = !showPassword}
                                    ) {
                                        Icon(
                                            painter = painterResource(
                                                if (showPassword) Resources.Icon.VisibilityOff
                                                else Resources.Icon.Visibility
                                            ),
                                            contentDescription = "Toggle password visibility"
                                        )
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(60.dp))

                        PrimaryButton(
                            text = if (target.isLogin()) "Sign in"
                                else "Sign up",
                            onClick = {
                                if (target.isLogin()){
                                    viewModel.login()
                                }else{viewModel.signUp(
                                    onSuccess = {message->
                                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                                        viewModel.changeAuthTarget(viewModel.authTarget.opposite())
                                        viewModel.resetSignUpStatus()
                                    },
                                    onError = {e->
                                        Toast.makeText(context,"Sign up failed $e",Toast.LENGTH_SHORT).show()
                                        viewModel.resetSignUpStatus()
                                    }
                                )}
                            },
                            enabled = viewModel.isFormValid
                        )
                        Button(
                            onClick = {
                                viewModel.changeAuthTarget(target.opposite())
                            },
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(20.dp),
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = CategoryPurple
                            )
                        ){
                            Text(
                                text = if (target.isLogin()) "Don't have an account? Sign up"
                                    else "Already have an account? Sign in",
                                fontSize = FontSize.REGULAR,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }


                }
            }
            if (viewModel.signUpStatus.isLoading() || loginStatus.isLoading()){
                LoadingCard(modifier = Modifier.fillMaxSize())
            }
        }

    }
}