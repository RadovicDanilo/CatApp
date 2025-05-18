package com.example.catapp.data.quiz

import com.example.catapp.data.db.model.BreedEntity
import com.example.catapp.data.db.model.ImageEntity
import com.example.catapp.data.quiz.model.QuizQuestion
import com.example.catapp.data.quiz.model.QuestionType
import com.example.catapp.data.repository.BreedRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.random.Random

class QuizGenerator @Inject constructor(
    private val breedRepository: BreedRepository,
) {
    private var breeds: List<BreedEntity> = emptyList()
    private lateinit var breedMap: Map<String, BreedEntity>
    private var allTemperaments: List<String> = emptyList()
    private var allImages: List<ImageEntity> = emptyList()

    suspend fun generateQuiz(): List<QuizQuestion> {
        loadInitialData()
        ensureMinimumImagesAvailable()

        return allImages.take(MAX_QUESTIONS).map { image -> createQuestionForImage(image) }
    }

    private suspend fun loadInitialData() {
        breeds = breedRepository.observeAllBreeds().first()
        breedMap = breeds.associateBy { it.id }
        allTemperaments = breeds.flatMap { it.temperament }.distinct()
        allImages = loadAllImages().shuffled()
    }

    private suspend fun loadAllImages(): List<ImageEntity> {
        return breeds.flatMap { breed ->
            breedRepository.observeImagesForBreed(breed.id).first()
        }
    }

    private suspend fun ensureMinimumImagesAvailable() {
        if (allImages.size < MIN_IMAGES_REQUIRED) {
            refreshData()
        }
    }

    private suspend fun refreshData() {
        breedRepository.fetchAllBreeds()
        loadInitialData()

        for (breed in breeds) {
            breedRepository.fetchImagesForBreed(breed.id)
            loadInitialData()

            if (allImages.size >= MIN_IMAGES_REQUIRED) break
        }
    }

    private fun createQuestionForImage(image: ImageEntity): QuizQuestion {
        val breed = breedMap[image.breedId]!!

        return if (Random.nextBoolean()) {
            createGuessTheBreedQuestion(image, breed)
        } else {
            createOddOneOutQuestion(image, breed.temperament)
        }
    }

    private fun createGuessTheBreedQuestion(
        image: ImageEntity, breed: BreedEntity
    ): QuizQuestion {
        val wrongOptions = breeds.filter { it.id != breed.id }.shuffled().map { it.name }.distinct()
            .take(WRONG_OPTIONS_COUNT)

        val options = (wrongOptions + breed.name).shuffled()

        return QuizQuestion.create(
            type = QuestionType.GUESS_THE_BREED,
            imageEntity = image,
            options = options,
            correctAnswer = breed.name
        )
    }

    private fun createOddOneOutQuestion(
        image: ImageEntity, correctTemperaments: List<String>
    ): QuizQuestion {
        val wrongOptions = correctTemperaments.shuffled().take(WRONG_OPTIONS_COUNT)
        val oddOut = allTemperaments.filterNot { it in correctTemperaments }.shuffled().first()

        val options = (wrongOptions + oddOut).shuffled()

        return QuizQuestion.create(
            type = QuestionType.OOD_ONE_OUT,
            imageEntity = image,
            options = options,
            correctAnswer = oddOut
        )
    }

    companion object {
        private const val MAX_QUESTIONS = 20
        private const val MIN_IMAGES_REQUIRED = 20
        private const val WRONG_OPTIONS_COUNT = 3
    }
}
