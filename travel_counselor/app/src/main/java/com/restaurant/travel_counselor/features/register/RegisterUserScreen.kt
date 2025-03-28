package com.restaurant.travel_counselor.features.register

import MyTextField
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.travel_counselor.shared.components.ErrorDialog
import com.restaurant.travel_counselor.shared.components.MyPasswordField
import com.restaurant.travel_counselor.shared.components.TopBar


@Composable
fun RegisterUserScreen(onNavigateTo: (String) -> Unit) {
    val registerUserViewModel: RegisterUserViewModel = viewModel()

    Scaffold(
        topBar = { TopBar() }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            RegisterUserFields(registerUserViewModel, onNavigateTo)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserFields(registerUserViewModel: RegisterUserViewModel, onNavigateTo: (String) -> Unit) {
    var registerUser = registerUserViewModel.uiState.collectAsState()
    val ctx = LocalContext.current

    MyTextField(
        label = "User",
        value = registerUser.value.user,
        onValueChange = {
            registerUserViewModel.onUserChange(it)
        },
    )
    MyTextField(
        label = "E-mail",
        value = registerUser.value.email,
        onValueChange = {
            registerUserViewModel.onEmailChange(it)
        }
    )
    MyPasswordField(
        label = "Password",
        value = registerUser.value.password,
        errorMessage = registerUser.value.validatePassord(),
        onValueChange = {
            registerUserViewModel.onPasswordChange(it)
        })
    MyPasswordField(
        label = "Confirm password",
        value = registerUser.value.confirmPassword,
        errorMessage = registerUser.value.validateConfirmPassword(),
        onValueChange = {
            registerUserViewModel.onConfirmPassword(it)
        })

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = {
            if (registerUserViewModel.register()) {
                Toast.makeText(
                    ctx, "User registered",
                    Toast.LENGTH_SHORT
                ).show()
                onNavigateTo("LoginScreen")
            }
        }
    ) {
        Text(text = "Register user")
    }

    if (registerUser.value.errorMessage.isNotBlank()) {
        ErrorDialog(
            error = registerUser.value.errorMessage,
            onDismissRequest = {
                registerUserViewModel.cleanErrorMessage()
            }
        )
    }

}


@Composable
@Preview(showSystemUi = true, showBackground = true, device = "id:Galaxy Nexus")
fun RegisterUserPreview() {
    RegisterUserScreen(onNavigateTo = {})
}