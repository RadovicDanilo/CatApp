package com.example.catapp.data.quiz.model

import com.example.catapp.data.db.model.ImageEntity

class QuizQuestion private constructor(
    val type: QuestionType,
    val imageEntity: ImageEntity,
    val options: List<String>,
    val correctOptionIndex: Int,
    var answerIndex: Int? = null
) {
    companion object {
        fun create(
            type: QuestionType,
            imageEntity: ImageEntity,
            options: List<String>,
            correctAnswer: String
        ): QuizQuestion {
            require(options.toSet().size >= 4) { "At least 4 unique options required" }
            require(options.contains(correctAnswer)) { "The array must include the correct answer" }

            return QuizQuestion(
                type = type,
                imageEntity = imageEntity,
                options = options,
                correctOptionIndex = options.indexOf(correctAnswer),
            )
        }
    }
}