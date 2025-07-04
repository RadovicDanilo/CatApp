package com.example.catapp.features.breed_details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.catapp.core.compose.CatAppTopBar
import com.example.catapp.core.compose.LoadingIndicator
import com.example.catapp.core.compose.NoDataContent
import com.example.catapp.data.db.model.BreedEntity
import com.example.catapp.data.db.model.ImageEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailScreen(
    viewModel: BreedDetailsViewModel, onClose: () -> Unit, navigateToGallery: (String) -> Unit
) {
    val uiState by viewModel.state.collectAsState()

    val details by uiState.details?.collectAsState(initial = null) ?: return
    val image by uiState.imageApiModel?.collectAsState(initial = null) ?: return

    Scaffold(topBar = {
        CatAppTopBar(
            modifier = Modifier.padding(8.dp),
            text = "CatApp",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            navigationOnClick = onClose,
        )
    }) { padding ->
        when {
            uiState.error != null -> {
                NoDataContent(
                    text = "Error = ${uiState.error!!.message}"
                )
            }

            details == null || image == null -> {
                LoadingIndicator()
            }

            else -> {
                BreedDetails(
                    details!!,
                    image!!,
                    padding,
                    navigateToGallery,
                    viewModel.breedId,
                )
            }
        }
    }

}

@Composable
fun BreedDetails(
    data: BreedEntity,
    image: ImageEntity,
    padding: PaddingValues,
    navigateToGallery: (String) -> Unit,
    breedId: String
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(scrollState), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = data.name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (data.isRare) {
                RareCard()
            }
        }

        BreedPicture(image.url)
        Button(onClick = {
            navigateToGallery(breedId)
        }) {
            Text("Gallery")
            Icon(
                imageVector = Icons.Default.Image, contentDescription = "Gallery"
            )
        }

        Text(
            text = data.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Countries of origin:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Chips(data.countriesOfOrigin.toTypedArray())

        Text(
            text = "Temperament:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Chips(data.temperament.toTypedArray())

        Text(
            text = "Life span: ${data.lifeSpan}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Breed characteristics:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        BreedAttribute("Adaptability", data.adaptability)
        BreedAttribute("Child friendly", data.childFriendly)
        BreedAttribute("Dog friendly", data.dogFriendly)
        BreedAttribute("Energy level", data.energyLevel)
        BreedAttribute("Health issues", data.healthIssues)
        BreedAttribute("Intelligence", data.intelligence)

        val uriHandler = LocalUriHandler.current
        Button(
            onClick = {
                uriHandler.openUri(data.wikipediaUrl)
            }, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "View on Wikipedia", style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun RareCard() {
    Row(
        verticalAlignment = CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            imageVector = Icons.Default.Star, contentDescription = null
        )
        Text(
            text = "Rare breed",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun BreedAttribute(name: String, value: Int) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$value/5",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        LinearProgressIndicator(
            progress = { value / 5f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun Chips(traits: Array<String>) {
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
private fun BreedPicture(imageUrl: String) {
    SubcomposeAsyncImage(
        modifier = Modifier.size(100.dp),
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                )
            }
        },
        error = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    imageVector = Icons.Default.Error, contentDescription = null
                )
            }
        })
}