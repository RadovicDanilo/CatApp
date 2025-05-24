package com.example.catapp.features.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.catapp.core.compose.CatAppTopBar
import com.example.catapp.core.compose.LoadingIndicator
import com.example.catapp.data.quiz.model.QuestionType
import com.example.catapp.data.quiz.model.QuizQuestion
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel = viewModel(),
    onClose: () -> Unit,
) {
    val uiState by viewModel.state.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler { showExitDialog = true }

    Scaffold(
        topBar = {
            CatAppTopBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Cat Quiz - Time: ${uiState.remainingTime}s",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                navigationOnClick = { showExitDialog = true },
            )
        }) { padding ->
        val questions = uiState.questions
        val questionIdx = uiState.currentQuestionIdx
        val coroutineScope = rememberCoroutineScope()

        if (showExitDialog) {
            ExitQuizDialog(onDismiss = { showExitDialog = false }, onConfirm = {
                showExitDialog = false
                onClose()
            })
        }

        when {
            uiState.hasFinished -> QuizResultsScreen(
                timeLeft = uiState.remainingTime,
                correctAnswers = uiState.correctAnswerCount,
                totalQuestions = questions.size,
                totalScore = uiState.totalScore,
                onSubmit = { coroutineScope.launch { viewModel.submitResults() } })

            questions.isEmpty() -> LoadingIndicator()

            else -> QuestionScreen(
                padding = padding,
                question = questions[questionIdx],
                questionIdx = questionIdx,
                totalQuestions = questions.size,
                onAnswerSelected = { selectedIdx ->
                    coroutineScope.launch {
                        viewModel.setAnswerAndAdvance(selectedIdx)
                    }
                })
        }
    }
}

@Composable
private fun ExitQuizDialog(
    onDismiss: () -> Unit, onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Exit Quiz?", style = MaterialTheme.typography.titleMedium) },
        text = { Text("Your progress will be lost if you exit now.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Exit", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Continue Quiz")
            }
        })
}

@Composable
fun QuestionScreen(
    padding: PaddingValues,
    question: QuizQuestion,
    questionIdx: Int,
    totalQuestions: Int,
    onAnswerSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Question ${questionIdx + 1} of $totalQuestions",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(question.imageEntity.url)
                .crossfade(true).build(),
            contentDescription = "Cat image for question",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Text(
            text = if (question.type == QuestionType.GUESS_THE_BREED) "Which breed is this cat?"
            else "Which trait doesn't belong to this cat?",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            question.options.forEachIndexed { idx, option ->
                AnswerOption(
                    index = idx, text = option, onClick = { onAnswerSelected(idx) })
            }
        }
    }
}

@Composable
private fun AnswerOption(
    index: Int, text: String, onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = "${index + 1}. $text",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun QuizResultsScreen(
    timeLeft: Int,
    correctAnswers: Int,
    totalQuestions: Int,
    totalScore: Double,
    onSubmit: suspend () -> Unit
) {
    var submitted by remember { mutableStateOf(false) }
    val scorePercentage = (totalScore.coerceAtMost(100.0)).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Completed!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResultItem("Time remaining:", "$timeLeft seconds")
                ResultItem("Correct answers:", "$correctAnswers/$totalQuestions")
                ResultItem("Your score:", "$scorePercentage%")
            }
        }

        val coroutineScope = rememberCoroutineScope()
        Button(
            onClick = {
                if (!submitted) {
                    submitted = true
                    coroutineScope.launch { onSubmit() }
                }
            }, enabled = !submitted, modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (submitted) "Submitted!" else "Submit Results")
        }
    }
}

@Composable
private fun ResultItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(
            value, style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ), color = MaterialTheme.colorScheme.primary
        )
    }
}
