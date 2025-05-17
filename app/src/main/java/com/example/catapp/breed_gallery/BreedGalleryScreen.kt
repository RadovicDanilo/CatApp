package com.example.catapp.breed_gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.SubcomposeAsyncImage
import com.example.catapp.apitempasas.list.model.ImageEntity
import com.example.catapp.core.compose.NoDataContent
import com.example.catapp.core.compose.PasswordAppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedGalleryScreen(
    viewModel: BreedGalleryViewModel, onClose: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()
    val images by uiState.images.collectAsState(initial = emptyList())

    Scaffold(topBar = {
        PasswordAppTopBar(
            modifier = Modifier.padding(8.dp),
            text = "CatApp",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            navigationOnClick = onClose,
        )
    }) { padding ->
        if (uiState.error != null) {
            NoDataContent(
                text = "Error = ${uiState.error!!.message}"
            )

        } else {
            BreedGallery(
                images!!,
                padding,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BreedGallery(images: List<ImageEntity>, padding: PaddingValues) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    if (selectedIndex != null) {
        FullscreenImagePager(
            images = images, startIndex = selectedIndex!!, onDismiss = { selectedIndex = null })
    }

    LazyVerticalGrid(
        modifier = Modifier
            .padding(padding)
            .padding(8.dp),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(images) { image ->
            SubcomposeAsyncImage(
                model = image.url,
                contentDescription = null,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize()
                    .clickable { selectedIndex = images.indexOf(image) },
                contentScale = ContentScale.Crop
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullscreenImagePager(
    images: List<ImageEntity>, startIndex: Int, onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = startIndex, pageCount = { images.size })

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Box {
                HorizontalPager(
                    state = pagerState
                ) { page ->
                    SubcomposeAsyncImage(
                        model = images[page].url,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Close"
                    )
                }
            }
        }
    }
}
