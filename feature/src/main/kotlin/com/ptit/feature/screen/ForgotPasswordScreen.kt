package com.ptit.feature.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ptit.shared.FontSize
import com.ptit.shared.Resources
import com.ptit.shared.component.CustomTextField
import com.ptit.shared.component.CustomTextFieldWithLabel
import com.ptit.shared.component.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navigateToAuth:()-> Unit,
    navigateToOTP:(String)->Unit
){
    var email by remember {
        mutableStateOf("")
    }
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
                    IconButton(onClick = navigateToAuth) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding->
        Column(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextFieldWithLabel(
                value = email,
                onValueChange = {email = it},
                label = "Your email",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )
            PrimaryButton(
                text = "Submit",
                onClick = {
                    navigateToOTP(email)
                }
            )
        }
    }
}