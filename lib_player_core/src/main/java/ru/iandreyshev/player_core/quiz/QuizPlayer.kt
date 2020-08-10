package ru.iandreyshev.player_core.quiz

import ru.iandreyshev.player_core.course.PlayerItem
import java.util.*

class QuizPlayer(
    private val resultListener: IQuizResultListener
) : IQuizPlayer {

    override val currentQuestion: Question
        get() = mQuestions[currentQuestionPosition]
    override var currentQuestionPosition: Int = 0
        private set
    override val questionsCount: Int
        get() = mQuestions.count()
    override var result = QuizResult.UNDEFINED
        private set

    private var mQuizId = QuizId("")
    private var mQuestions = mutableListOf<Question>()

    override fun prepare(playerQuiz: PlayerItem.Quiz) {
        mQuestions.clear()

        playerQuiz.quiz
            .questions
            .mapIndexedTo(mQuestions) { index, question ->
                question.asPlayerQuestion(index)
            }

        mQuizId = playerQuiz.quiz.id

        result = playerQuiz.result
    }

    override fun moveToNextQuestion() {
        if (currentQuestionPosition < mQuestions.lastIndex) {
            currentQuestionPosition++
        }
    }

    override fun switchCurrQuestionVariantCorrectState(variantPosition: Int) {
        if (currentQuestion.isSubmitted()) {
            return
        }

        val variant = currentQuestion.variants.getOrNull(variantPosition) ?: return
        var newVariants = currentQuestion.variants.toMutableList()
        val newVariantCorrectState = !variant.isSelectedAsCorrect

        if (!currentQuestion.isMultipleMode) {
            newVariants = newVariants.mapTo(mutableListOf()) {
                it.copy(isSelectedAsCorrect = false)
            }
        }

        newVariants[variantPosition] = variant.copy(isSelectedAsCorrect = newVariantCorrectState)
        mQuestions[currentQuestionPosition] = currentQuestion.copy(variants = newVariants)
    }

    override fun submitCurrentQuestion() {
        if (currentQuestion.isSubmitted()) {
            return
        }

        mQuestions[currentQuestionPosition] = currentQuestion
            .copy(result = currentQuestion.measureResult())

        val newResult = measureQuizResult()

        if (newResult != result) {
            result = newResult
            resultListener.onChange(mQuizId, result)
        }
    }

    private fun measureQuizResult(): QuizResult {
        val questionResults = mQuestions.map { it.result }

        val isTrue = questionResults.all { it == QuestionResult.TRUE }

        if (isTrue) {
            return QuizResult.TRUE
        }

        val isPartlyTrue = questionResults.any {
            it == QuestionResult.PARTLY_TRUE
                    || it == QuestionResult.PARTLY_TRUE
        }

        if (isPartlyTrue) {
            return QuizResult.PARTLY_TRUE
        }

        return QuizResult.UNDEFINED
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
                isSelectedAsCorrect = false,
                isCorrect = it.isCorrect
            )
        },
        result = QuestionResult.UNDEFINED
    )

    private fun Question.isSubmitted() = result != QuestionResult.UNDEFINED

    private fun Question.measureResult(): QuestionResult {
        val (trueVariants, falseVariants) = variants.partition { variant ->
            variant.isCorrect && variant.isSelectedAsCorrect
        }

        return when {
            trueVariants.isNotEmpty() && falseVariants.isNotEmpty() -> QuestionResult.PARTLY_TRUE
            falseVariants.isEmpty() -> QuestionResult.TRUE
            else -> QuestionResult.FALSE
        }
    }

}