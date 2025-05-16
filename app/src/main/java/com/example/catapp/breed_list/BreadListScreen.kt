package com.example.catapp.breed_list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catapp.core.compose.LoadingIndicator
import com.example.catapp.core.compose.NoDataContent
import com.example.catapp.core.compose.PasswordAppTopBar
import com.example.catapp.apitempasas.list.model.SimpleBreadUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreadListScreen(
    viewModel: BreadListViewModel,
    onBreadClick: (id: String) -> Unit,
) {
    val uiState by viewModel.state.collectAsState()
    var isSearchOpen by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PasswordAppTopBar(
                modifier = Modifier.padding(8.dp),
                text = "CatApp",
                actionIcon = Icons.Default.Search,
                actionOnClick = {
                    isSearchOpen = !isSearchOpen
                    viewModel.onEvent(BreadListScreenContract.UiEvent.UpdateSearchTerm(""))
                })
        }) { paddingValues ->
        when {
            uiState.error != null -> NoDataContent(text = "Error = ${uiState.error!!.message ?: "Unknown error"}")
            uiState.isLoading -> LoadingIndicator()
            uiState.breads.isEmpty() -> NoDataContent(text = "No information on breeds")
            else -> Content(
                paddingValues = paddingValues,
                uiState = uiState,
                viewModel = viewModel,
                isSearchOpen = isSearchOpen,
                onSearchClose = {
                    isSearchOpen = !isSearchOpen
                    viewModel.onEvent(BreadListScreenContract.UiEvent.UpdateSearchTerm(""))
                },
                onBreadClick = onBreadClick
            )
        }
    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    uiState: BreadListScreenContract.UiState,
    viewModel: BreadListViewModel,
    isSearchOpen: Boolean,
    onSearchClose: () -> Unit,
    onBreadClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        if (isSearchOpen) {
            SearchBar(searchText = uiState.searchTerm, onTextChange = { newText ->
                viewModel.onEvent(
                    BreadListScreenContract.UiEvent.UpdateSearchTerm(newText)
                )
            }, onClose = {
                onSearchClose()
            })
        }

        BreadList(
            breads = uiState.breads, searchTerm = uiState.searchTerm, onBreadClick = onBreadClick
        )
    }
}

@Composable
private fun BreadList(
    breads: List<SimpleBreadUiModel>, searchTerm: String, onBreadClick: (String) -> Unit
) {
    val filteredBreads = filterBreeds(breads, searchTerm)

    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(filteredBreads) { bread ->
            BreadListItem(
                breadId = bread.id,
                breadName = bread.name,
                alternateBreadName = bread.altNames,
                description = bread.description,
                traits = bread.traits,
                onBreadClick = onBreadClick
            )
        }
    }
}

@Composable
private fun BreadListItem(
    breadId: String,
    breadName: String,
    alternateBreadName: String?,
    description: String,
    traits: Array<String>,
    onBreadClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onSurface)
            .clickable { onBreadClick(breadId) }
            .padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = breadName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (!alternateBreadName.isNullOrBlank()) {
            Text(
                text = alternateBreadName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description.take(250),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 3,
        )

        Spacer(modifier = Modifier.height(8.dp))

        TraitChips(traits)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TraitChips(traits: Array<String>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        traits.take(5).forEach { trait ->
            SuggestionChip(
                onClick = {},
                label = { Text(text = trait, style = MaterialTheme.typography.labelLarge) })
        }
    }
}

@Composable
private fun SearchBar(
    searchText: String, onTextChange: (String) -> Unit, onClose: () -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text("Search...") },
        trailingIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close Search")
            }
        },
        singleLine = true
    )
}

private fun filterBreeds(
    breads: List<SimpleBreadUiModel>, searchTerm: String
): List<SimpleBreadUiModel> {
    return if (searchTerm.isBlank()) {
        breads
    } else {
        breads.filter {
            it.name.contains(searchTerm, ignoreCase = true) || it.altNames.contains(
                searchTerm, ignoreCase = true
            )
        }
    }
}
