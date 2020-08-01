package ru.iandreyshev.light.domain.player.quiz

import ru.iandreyshev.light.domain.course.CourseItem

class QuizPlayer(
    quiz: CourseItem.Quiz
) : IQuizPlayer {

    override val currentQuestion: Question
        get() = mQuestions[mCurrentQuestionPosition]
    override val currentQuestionPosition: Int
        get() = mCurrentQuestionPosition
    override val questionsCount: Int
        get() = mQuestions.count()

    private val mQuestions = quiz.questions
        .mapIndexed { index, question ->
            question.asPlayerQuestion(index)
        }
        .toMutableList()
    private var mCurrentQuestionPosition = 0

    override fun getQuizResult(): QuizResult {
        return QuizResult("Result")
    }

    override fun moveToNextQuestion() {
        if (mCurrentQuestionPosition < mQuestions.lastIndex) {
            mCurrentQuestionPosition++
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
    }

    private fun ru.iandreyshev.light.domain.quizMaker.Question.asPlayerQuestion(
        position: Int
    ) = Question(
        text = text,
        isMultipleMode = isMultipleMode,
        position = position,
        variants = variants.map {
            Variant(
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