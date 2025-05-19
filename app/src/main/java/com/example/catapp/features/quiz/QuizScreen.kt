package com.example.catapp.features.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.catapp.core.compose.LoadingIndicator
import com.example.catapp.core.compose.PasswordAppTopBar
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

    BackHandler {
        showExitDialog = true
    }

    Scaffold(
        topBar = {
            PasswordAppTopBar(
                modifier = Modifier.padding(8.dp),
                text = "CatApp - Time: ${uiState.remainingTime}",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                navigationOnClick = { showExitDialog = true },
            )
        }) { padding ->

        val questions = uiState.questions
        val questionIdx = uiState.currentQuestionIdx

        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            onClose()
                        },
                    ) {
                        Text("Exit")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Exit Quiz?") },
                text = { Text("Are you sure you want to quit the quiz?") })
        }

        val coroutineScope = rememberCoroutineScope()
        when {
            uiState.hasFinished -> FinishedScreen(
                timeLeft = uiState.remainingTime,
                questions = questions,
                onSubmit = { viewModel.submitResults() },
                correctAnswers = uiState.correctAnswerCount,
                totalScore = uiState.totalScore
            )

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
fun QuestionScreen(
    padding: PaddingValues,
    question: QuizQuestion,
    questionIdx: Int,
    totalQuestions: Int,
    onAnswerSelected: (Int) -> Unit
) {
    val questionText = if (question.type == QuestionType.GUESS_THE_BREED) {
        "Which breed does the cat in the picture belong to?"
    } else {
        "Which trait doesn't belong to the cat in the picture?"
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Question ${questionIdx + 1}/$totalQuestions",
            style = MaterialTheme.typography.titleMedium
        )
        Text(text = questionText, style = MaterialTheme.typography.bodyLarge)

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(question.imageEntity.url)
                .crossfade(true).build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            question.options.forEachIndexed { idx, option ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAnswerSelected(idx) }) {
                    Text(
                        text = "$idx. $option",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun FinishedScreen(
    timeLeft: Int,
    questions: List<QuizQuestion>,
    correctAnswers: Int,
    totalScore: Double,
    onSubmit: suspend () -> Unit
) {
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Quiz Finished!", style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Time Left: $timeLeft", style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Correct Answers: $correctAnswers/${questions.size}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Score: ${totalScore.coerceAtMost(100.0).toInt()} / 100",
            style = MaterialTheme.typography.bodyLarge
        )
        val coroutineScope = rememberCoroutineScope()
        Button(
            onClick = {
                if (!submitted) {
                    submitted = true
                    coroutineScope.launch {
                        onSubmit()
                    }
                }
            }, enabled = !submitted
        ) {
            Text("Submit Results")
        }
    }
}
