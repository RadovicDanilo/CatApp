package com.example.catapp.data.quiz

import com.example.catapp.data.db.model.BreedEntity
import com.example.catapp.data.db.model.ImageEntity
import com.example.catapp.data.quiz.model.QuestionType
import com.example.catapp.data.quiz.model.QuizQuestion
import com.example.catapp.data.repository.BreedRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.random.Random

class QuizGenerator @Inject constructor(
    private val breedRepository: BreedRepository,
) {
    private lateinit var breeds: List<BreedEntity>
    private lateinit var breedMap: Map<String, BreedEntity>
    private lateinit var allTemperaments: List<String>
    private lateinit var allImages: List<ImageEntity>

    suspend fun generateQuiz(): List<QuizQuestion> {
        fetchValidDataIfNeeded()
        return allImages.shuffled().take(QUESTION_COUNT)
            .map { image -> createQuestionForImage(image) }
    }

    private suspend fun fetchValidDataIfNeeded() {
        loadFromDatabase()

        if (allImages.size < QUESTION_COUNT) {
            fetchFromNetworkAndReload()
        }
    }

    private suspend fun loadFromDatabase() {
        breeds = breedRepository.observeAllBreeds().first()
        breedMap = breeds.associateBy { it.id }
        allTemperaments = breeds.flatMap { it.temperament }.distinct()
        allImages =
            breeds.flatMap { breed -> breedRepository.observeImagesForBreed(breed.id).first() }
                .shuffled()
    }

    private suspend fun fetchFromNetworkAndReload() {
        breedRepository.fetchAllBreeds()

        for (breed in breedRepository.observeAllBreeds().first()) {
            breedRepository.fetchImagesForBreed(breed.id)
            loadFromDatabase()

            if (allImages.size >= QUESTION_COUNT) break
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

    private fun createGuessTheBreedQuestion(image: ImageEntity, breed: BreedEntity): QuizQuestion {
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
        private const val QUESTION_COUNT = 20
        private const val WRONG_OPTIONS_COUNT = 3
    }
}
