package com.example.catapp.features.leaderboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catapp.core.compose.PasswordAppTopBar
import com.example.catapp.data.api.leaderboard.model.QuizResultApiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel
) {
    val uiState by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            PasswordAppTopBar(
                modifier = Modifier.padding(8.dp),
                text = "CatApp",
            )
        }) { padding ->
        ResultsList(
            padding = padding,
            results = uiState.results,
        )
    }
}

@Composable
fun ResultsList(results: List<QuizResultApiModel>, padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .padding(8.dp)
    ) {
        itemsIndexed(results) { index, result ->
            ResultItem(index, result)
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun ResultItem(index: Int, result: QuizResultApiModel) {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    formatter.format(Date(result.createdAt))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        val modifier = Modifier.padding(vertical = 4.dp)

        Text(text = "$index", modifier)
        Text(text = result.nickname, modifier.weight(2f))
        Text(text = result.result.toString(), modifier)
    }
}
