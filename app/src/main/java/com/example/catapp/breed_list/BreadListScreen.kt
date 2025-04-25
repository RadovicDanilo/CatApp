package com.example.catapp.breed_list

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BreadListScreen(
    onBreadClick: (id: Int) -> Unit,
) {
    Scaffold(
        topBar = {
            // TODO: add search bar
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            itemsIndexed(List(10) { it }) { index, _ ->
                BreadListItem(
                    breadName = "Norwegian Forest",
                    alternateBreadName = "Skogkatt",
                    description = "Strong, long-haired breed with a calm temperament and a love for climbing.Strong, long-haired breed with a calm temperament and a love for climbing.Strong, long-haired breed with a calm temperament and a love for climbing.Strong, long-haired breed with a calm temperament and a love for climbing.Strong, long-haired breed with a calm temperament and a love for climbing.Strong, long-haired breed with a calm temperament and a love for climbing.Strong, long-haired breed with a calm temperament and a love for climbing.Strong, long-haired breed with a calm temperament and a love for climbing.",
                    traits = arrayOf(
                        "Fluffy", "Friendly", "Large", "Independent", "Agile", "ERR", "ERR"
                    ),
                    onBreadClick = { onBreadClick(index) })
            }
        }
    }
}

@Composable
fun BreadListItem(
    breadName: String,
    alternateBreadName: String?,
    description: String,
    traits: Array<String>,
    onBreadClick: (id: Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onSurface)
            .clickable { onBreadClick }
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
}
