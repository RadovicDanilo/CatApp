package com.example.catapp.breed_list

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catapp.core.compose.LoadingIndicator
import com.example.catapp.core.compose.NoDataContent
import com.example.catapp.core.compose.PasswordAppTopBar
import com.example.catapp.users.list.model.SimpleBreadUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreadListScreen(
    viewModel: BreadListViewModel,
    onBreadClick: (id: String) -> Unit,
) {
    val uiState = viewModel.state.collectAsState()
    Scaffold(topBar = {
        PasswordAppTopBar(
            modifier = Modifier.padding(8.dp), text = "CatApp", actionIcon = Icons.Default.Search
        )
    }) { padding ->
        if (uiState.value.error != null) {
            NoDataContent(
                text = "Error = ${uiState.value.error!!.message}"
            )
        } else if (uiState.value.isLoading) {
            LoadingIndicator()
        } else if (uiState.value.breads.isEmpty()) {
            NoDataContent(
                text = "No information on breads"
            )
        } else {
            BreadList(padding, onBreadClick, uiState.value.breads)
        }
    }
}

@Composable
private fun BreadList(
    padding: PaddingValues, onBreadClick: (String) -> Unit, breads: List<SimpleBreadUiModel>
) {
    LazyColumn(
        modifier = Modifier.padding(padding)
    ) {

        items(breads) { bread ->
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreadListItem(
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

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun TraitChips(traits: Array<String>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        traits.take(5).forEach { trait ->
            SuggestionChip(onClick = { Log.d("SuggestionChip", "hello world!") }, label = {
                Text(
                    text = trait, style = MaterialTheme.typography.labelLarge
                )
            })
        }
    }
}

@Composable
fun SearchBar() {

}
