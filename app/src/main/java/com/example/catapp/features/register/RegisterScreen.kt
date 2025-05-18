package com.example.catapp.features.register

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.catapp.core.compose.PasswordAppTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel
) {
    val uiState by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.error) {
        uiState.error?.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            PasswordAppTopBar(
                modifier = Modifier.padding(8.dp),
                text = "CatApp",
            )
        }) { padding ->
        RegisterForm(
            padding = padding,
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onSubmit = viewModel::submit
        )
    }
}

@Composable
fun RegisterForm(
    padding: PaddingValues,
    uiState: RegisterScreenContract.UiState,
    onEvent: (RegisterScreenContract.UiEvent) -> Unit,
    onSubmit: suspend () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = uiState.firstName,
            onValueChange = { onEvent(RegisterScreenContract.UiEvent.UpdateFirstName(it)) },
            label = { Text("First Name") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )
        TextField(
            value = uiState.lastName,
            onValueChange = { onEvent(RegisterScreenContract.UiEvent.UpdateLastName(it)) },
            label = { Text("Last Name") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )
        TextField(
            value = uiState.nickname,
            onValueChange = { onEvent(RegisterScreenContract.UiEvent.UpdateNickname(it)) },
            label = { Text("Nickname") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )
        TextField(
            value = uiState.email,
            onValueChange = { onEvent(RegisterScreenContract.UiEvent.UpdateEmail(it)) },
            label = { Text("Email") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )

        val coroutineScope = rememberCoroutineScope()
        Button(
            onClick = {
                coroutineScope.launch {
                    onSubmit()
                }
            }, modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Register")
        }
    }
}
