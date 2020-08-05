package ru.iandreyshev.player_core.quiz

import ru.iandreyshev.player_core.course.PlayerItem
import java.util.*

class QuizPlayer : IQuizPlayer {

    override val currentQuestion: Question
        get() = mQuestions[currentQuestionPosition]
    override var currentQuestionPosition: Int = 0
        private set
    override val questionsCount: Int
        get() = mQuestions.count()
    override var result: QuizResult? = null
        private set

    private var mQuestions = mutableListOf<Question>()

    override fun prepare(playerQuiz: PlayerItem.Quiz) {
        mQuestions.clear()

        playerQuiz.quiz
            .questions
            .mapIndexedTo(mQuestions) { index, question ->
                question.asPlayerQuestion(index)
            }

        result = playerQuiz.result
    }

    override fun moveToNextQuestion() {
        if (currentQuestionPosition < mQuestions.lastIndex) {
            currentQuestionPosition++
        }
    }

    override fun switchCurrQuestionVariantValidState(variantPosition: Int) {
        if (currentQuestion.isSubmitted()) {
            return
        }

        val variant = currentQuestion.variants.getOrNull(variantPosition) ?: return
        var newVariants = currentQuestion.variants.toMutableList()
        val newVariantValidState = !variant.isSelectedAsValid

        if (!currentQuestion.isMultipleMode) {
            newVariants = newVariants.mapTo(mutableListOf()) {
                it.copy(isSelectedAsValid = false)
            }
        }

        newVariants[variantPosition] = variant.copy(isSelectedAsValid = newVariantValidState)
        mQuestions[currentQuestionPosition] = currentQuestion.copy(variants = newVariants)
    }

    override fun submitCurrentQuestion() {
        if (currentQuestion.isSubmitted()) {
            return
        }

        mQuestions[currentQuestionPosition] = currentQuestion
            .copy(result = currentQuestion.measureResult())

        if (mQuestions.all { it.result != null }) {
            result = QuizResult("Result")
        }
    }

    override fun onFinish() {
    }

    private fun Question.asPlayerQuestion(
        position: Int
    ) = Question(
        id = QuestionId(Date().toString()),
        text = text,
        isMultipleMode = isMultipleMode,
        position = position,
        variants = variants.map {
            Variant(
                id = VariantId(Date().toString()),
                text = it.text,
                isSelectedAsValid = false,
                isValid = it.isValid
            )
        },
        result = null
    )

    private fun Question.isSubmitted() = result != null

    private fun Question.measureResult(): QuestionResult {
        val (trueVariants, falseVariants) = variants.partition { variant ->
            variant.isValid && variant.isSelectedAsValid
        }

        return when {
            trueVariants.isNotEmpty() && falseVariants.isNotEmpty() -> QuestionResult.PARTLY_TRUE
            falseVariants.isEmpty() -> QuestionResult.TRUE
            else -> QuestionResult.FALSE
        }
    }

}