package ru.iandreyshev.player_core.quiz

import ru.iandreyshev.player_core.course.PlayerItem
import java.util.*

class QuizPlayer(
    private val resultListener: IQuizResultListener
) : IQuizPlayer {

    override val title: String
        get() = mQuizTitle
    override val currentQuestion: Question
        get() = mQuestions[currentQuestionPosition]
    override var currentQuestionPosition: Int = 0
        private set
    override val questionsCount: Int
        get() = mQuestions.count()
    override var result = QuizResult.UNDEFINED
        private set

    private var mQuizId = QuizId("")
    private var mQuizTitle = ""
    private var mQuestions = mutableListOf<Question>()

    override fun prepare(playerQuiz: PlayerItem.Quiz) {
        mQuestions.clear()

        playerQuiz.quiz
            .questions
            .mapIndexedTo(mQuestions) { index, question ->
                question.asPlayerQuestion(index)
            }

        mQuizId = playerQuiz.quiz.id
        mQuizTitle = playerQuiz.quiz.title

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

        return when {
            questionResults.contains(QuestionResult.UNDEFINED) -> QuizResult.UNDEFINED
            questionResults.all { it == QuestionResult.FAILURE } -> QuizResult.FAILURE
            questionResults.all { it == QuestionResult.SUCCESS } -> QuizResult.SUCCESS
            else -> QuizResult.PARTLY_SUCCESS
        }
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
        return when {
            variants.none { it.isSelectedAsCorrect } -> QuestionResult.UNDEFINED
            variants.all { it.isSuccess() } -> QuestionResult.SUCCESS
            variants.all { !it.isSuccess() } -> QuestionResult.FAILURE
            else -> QuestionResult.PARTLY_SUCCESS
        }
    }

    private fun Variant.isSuccess() =
        (isCorrect && isSelectedAsCorrect) || (!isCorrect && !isSelectedAsCorrect)

}
