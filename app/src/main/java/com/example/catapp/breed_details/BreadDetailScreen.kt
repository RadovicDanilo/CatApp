package com.example.catapp.breed_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.catapp.core.compose.LoadingIndicator
import com.example.catapp.core.compose.NoDataContent
import com.example.catapp.users.list.model.DetailedBreadUiModel

@Composable
fun BreadDetailScreen(
    viewModel: BreadDetailsViewModel, onClose: () -> Unit
) {
    val uiState = viewModel.state.collectAsState()

    if (uiState.value.error != null) {
        NoDataContent(
            text = "Error = ${uiState.value.error!!.message}",
        )
    } else if (uiState.value.isLoading) {
        LoadingIndicator()
    } else {
        BreadDetails(
            uiState.value.data!!, onClose
        )
    }
}

@Composable
fun BreadDetails(data: DetailedBreadUiModel, onClose: () -> Unit) {
    NoDataContent(text = data.name)
}