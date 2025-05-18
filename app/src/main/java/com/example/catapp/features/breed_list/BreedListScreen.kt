package com.example.catapp.features.breed_list

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
import com.example.catapp.data.db.model.BreedEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedListScreen(
    viewModel: BreedListViewModel,
    onBreedClick: (id: String) -> Unit,
) {
    val uiState by viewModel.state.collectAsState()
    val breedsList by uiState.breeds.collectAsState(initial = emptyList())
    var isSearchOpen by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PasswordAppTopBar(
                modifier = Modifier.padding(8.dp),
                text = "CatApp",
                actionIcon = Icons.Default.Search,
                actionOnClick = {
                    isSearchOpen = !isSearchOpen
                    viewModel.onEvent(BreedListScreenContract.UiEvent.UpdateSearchTerm(""))
                })
        }) { paddingValues ->
        when {
            uiState.error != null -> NoDataContent(
                text = "Error = ${uiState.error?.message ?: "Unknown error"}"
            )

            uiState.isLoading -> LoadingIndicator()
            breedsList.isEmpty() -> NoDataContent(text = "No information on breeds")
            else -> Content(
                breedsList = breedsList,
                paddingValues = paddingValues,
                uiState = uiState,
                isSearchOpen = isSearchOpen,
                onEvent = viewModel::onEvent,
                onSearchClose = {
                    isSearchOpen = false
                    viewModel.onEvent(BreedListScreenContract.UiEvent.UpdateSearchTerm(""))
                },
                onBreedClick = onBreedClick
            )
        }
    }
}

@Composable
private fun Content(
    breedsList: List<BreedEntity>,
    paddingValues: PaddingValues,
    uiState: BreedListScreenContract.UiState,
    onEvent: (BreedListScreenContract.UiEvent) -> Unit,
    isSearchOpen: Boolean,
    onSearchClose: () -> Unit,
    onBreedClick: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        if (isSearchOpen) {
            SearchBar(
                searchText = uiState.searchTerm, onTextChange = { newText ->
                    onEvent(
                        BreedListScreenContract.UiEvent.UpdateSearchTerm(newText)
                    )
                }, onClose = onSearchClose
            )
        }

        BreedList(
            breeds = breedsList, searchTerm = uiState.searchTerm, onBreedClick = onBreedClick
        )
    }
}

@Composable
private fun BreedList(
    breeds: List<BreedEntity>, searchTerm: String, onBreedClick: (String) -> Unit
) {
    val filteredBreeds = filterBreeds(breeds, searchTerm)

    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(filteredBreeds, key = { it.id }) { breed ->
            BreedListItem(
                breedId = breed.id,
                breedName = breed.name,
                alternateBreedName = breed.altNames,
                description = breed.description,
                traits = breed.temperament,
                onBreedClick = onBreedClick
            )
        }
    }
}

@Composable
private fun BreedListItem(
    breedId: String,
    breedName: String,
    alternateBreedName: String?,
    description: String,
    traits: List<String>,
    onBreedClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onSurface)
            .clickable { onBreedClick(breedId) }
            .padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = breedName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (!alternateBreedName.isNullOrBlank()) {
            Text(
                text = alternateBreedName,
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
private fun TraitChips(traits: List<String>) {
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
    breeds: List<BreedEntity>, searchTerm: String
): List<BreedEntity> {
    return if (searchTerm.isBlank()) {
        breeds
    } else {
        breeds.filter {
            it.name.contains(searchTerm, ignoreCase = true) || it.altNames.contains(
                searchTerm, ignoreCase = true
            )
        }
    }
}
