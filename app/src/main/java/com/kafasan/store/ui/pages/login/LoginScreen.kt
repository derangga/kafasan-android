package com.kafasan.store.ui.pages.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kafasan.store.ui.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,
) {
    val maxEmail = 50
    val maxPassword = 50
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val isEmailError = uiState.value.error == LoginViewModel.FieldError.EMAIL
    val isPasswordError = uiState.value.error == LoginViewModel.FieldError.PASSWORD

    if (uiState.value.isSuccess) {
        navController.navigate(Route.Home.route) {
            popUpTo(Route.Login.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets =
            WindowInsets(
                left = 12.dp,
                top = 12.dp,
                right = 12.dp,
                bottom = 12.dp,
            ),
        topBar = {
            TopAppBar(
                title = { Text("Welcome", color = Color.White) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
            )
        },
    ) { ip ->
        Column(
            modifier =
                Modifier
                    .padding(ip)
                    .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { if (it.length <= maxEmail) email = it },
                label = { Text("Email") },
                maxLines = 1,
                isError = isEmailError,
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            if (isEmailError) {
                                Arrangement.SpaceBetween
                            } else {
                                Arrangement.End
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (isEmailError) {
                            Text(uiState.value.error.message)
                        }
                        Text(
                            "${password.length}/$maxPassword",
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            OutlinedTextField(
                value = password,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { if (it.length <= maxPassword) password = it },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Password") },
                maxLines = 1,
                isError = isPasswordError,
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            if (isPasswordError) {
                                Arrangement.SpaceBetween
                            } else {
                                Arrangement.End
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (isPasswordError) {
                            Text(uiState.value.error.message)
                        }
                        Text(
                            "${password.length}/$maxPassword",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions =
                    KeyboardActions(
                        onGo = {
                            keyboardController?.hide()
                            viewModel.simulateLogin(checked, email, password)
                        },
                    ),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                )
                Text("Remember me")
            }
            Spacer(modifier = Modifier.padding(top = 12.dp))
            Button(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !uiState.value.loading,
                onClick = { viewModel.simulateLogin(checked, email, password) },
            ) {
                if (uiState.value.loading) {
                    CircularProgressIndicator()
                } else {
                    Text("Login")
                }
            }
        }
    }
}
