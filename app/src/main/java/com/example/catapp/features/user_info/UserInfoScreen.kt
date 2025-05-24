package com.example.catapp.features.user_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.catapp.core.compose.CatAppTopBar
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
            CatAppTopBar(
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
        Spacer(modifier = Modifier.height(8.dp))
        ResultsList(results.sortedBy { -it.result })
    }
}

@Composable
fun UserAccountInfo(userInfo: UserAccount, bestResult: Double) {
    val roundedBestResult = "%.2f".format(bestResult)

    Text(
        text = "Account Information",
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    HorizontalDivider(
        thickness = 1.dp, color = MaterialTheme.colorScheme.outline
    )

    UserInfoRow(label = "First Name:", value = userInfo.firstName)
    UserInfoRow(label = "Last Name:", value = userInfo.lastName)
    UserInfoRow(label = "Nickname:", value = userInfo.nickname)
    UserInfoRow(label = "Email:", value = userInfo.email)

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        UserInfoRow(
            label = "Best Rank:",
            value = userInfo.bestRank.toString(),
            modifier = Modifier.weight(1f)
        )

        UserInfoRow(
            label = "Best Result:",
            value = roundedBestResult,
            modifier = Modifier.weight(1f),
            valueColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun UserInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = " $value",
            style = MaterialTheme.typography.bodyLarge,
            color = valueColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun ResultsList(
    results: List<QuizResultEntity>
) {
    Text(
        text = "Quiz History",
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleLarge,
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(results) { index, result ->
            ResultItem(index + 1, result)
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun ResultItem(index: Int, result: QuizResultEntity) {
    val formattedScore = "%.2f".format(result.result)
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val formattedDate = formatter.format(Date(result.createdAt))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        color = when (index) {
                            1 -> MaterialTheme.colorScheme.primary
                            2 -> MaterialTheme.colorScheme.secondary
                            3 -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.surface
                        }
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$index",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = when (index) {
                        1 -> MaterialTheme.colorScheme.onPrimary
                        2 -> MaterialTheme.colorScheme.onSecondary
                        3 -> MaterialTheme.colorScheme.onTertiary
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = formattedScore,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
