package com.example.catapp.features.user_info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catapp.core.compose.PasswordAppTopBar
import com.example.catapp.data.account_store.UserAccount
import com.example.catapp.data.db.model.QuizResultEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    viewModel: UserInfoViewModel
) {
    val uiState by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            PasswordAppTopBar(
                modifier = Modifier.padding(8.dp),
                text = "CatApp",
            )
        }) { padding ->
        UserInfo(
            padding = padding,
            uiState = uiState,
        )
    }
}

@Composable
fun UserInfo(
    padding: PaddingValues,
    uiState: UserInfoScreenContract.UiState,
) {
    val userInfo by uiState.userInfo.collectAsState()
    val results by uiState.results.collectAsState(emptyList())
    val bestResult = results.maxOfOrNull { it.result }?.toDouble() ?: 0.0

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userInfo?.let { UserAccountInfo(it, bestResult) }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Quiz History", modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
        UserResultsList(results)
    }
}

@Composable
fun UserResultsList(results: List<QuizResultEntity>) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(results, key = { it.id }) { result ->
            UserResultItem(result)
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun UserResultItem(result: QuizResultEntity) {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val createdAtDate = formatter.format(Date(result.createdAt))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = "${result.result}", modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        )
        Text(
            text = createdAtDate, modifier = Modifier
                .weight(2f)
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun UserAccountInfo(userInfo: UserAccount, bestResult: Double) {
    val textModifier = Modifier
        .padding(vertical = 4.dp)
        .fillMaxWidth()

    Text(text = "First Name: ${userInfo.firstName}", modifier = textModifier)
    Text(text = "Last Name: ${userInfo.lastName}", modifier = textModifier)
    Text(text = "Nickname: ${userInfo.nickname}", modifier = textModifier)
    Text(text = "Email: ${userInfo.email}", modifier = textModifier)
    Text(text = "Best Rank: ${userInfo.bestRank}", modifier = textModifier)
    Text(text = "Best Result: $bestResult", modifier = textModifier)
}
