package ru.iandreyshev.constructor.domain.quiz.draft

import ru.iandreyshev.constructor.domain.quiz.QuestionDraftId
import ru.iandreyshev.constructor.domain.quiz.quizMaker.QuizMakerQuestion
import ru.iandreyshev.constructor.domain.quiz.quizMaker.QuizMakerVariant

class QuizMaker(
    private val mStartDraft: QuizDraft
) {

    val currentQuestion: QuizMakerQuestion
        get() = mQuestions[currentQuestionPosition]
    var currentQuestionPosition: Int = 0
        private set
    val hasPrevious: Boolean
        get() = currentQuestionPosition > 0
    val hasNext: Boolean
        get() = currentQuestionPosition < questionsCount - 1
    val questionsCount: Int
        get() = mQuestions.count()

    private var mQuestions = mutableListOf<QuizMakerQuestion>()

    fun createDraft() =
        mStartDraft.copy(
            questions = mQuestions.map { quizMakerQuestion ->
                QuestionDraft(
                    id = quizMakerQuestion.id,
                    text = quizMakerQuestion.text,
                    isMultipleMode = quizMakerQuestion.isMultipleMode,
                    variants = quizMakerQuestion.variants
                        .map {
                            VariantDraft(
                                id = it.id,
                                isValid = it.isValid,
                                text = it.text
                            )
                        }
                )
            }
        )

    fun moveToNextQuestion() {
        if (currentQuestionPosition < mQuestions.lastIndex) {
            ++currentQuestionPosition
        }
    }

    fun moveToPreviousQuestion() {
        if (currentQuestionPosition > 0) {
            --currentQuestionPosition
        }
    }

    fun addQuestion(): Int {
        val question = QuizMakerQuestion(
            id = QuestionDraftId(""),
            text = "",
            isMultipleMode = false,
            variants = mutableListOf()
        )
        mQuestions.add(question)

        return mQuestions.lastIndex
    }

    fun setQuestionText(text: String) {
        val question = mQuestions.getOrNull(currentQuestionPosition) ?: return
        question.text = text
    }

    fun switchQuestionMultipleMode() {
        val question = mQuestions.getOrNull(currentQuestionPosition) ?: return
        question.isMultipleMode = !question.isMultipleMode
        question.variants.forEach {
            it.isValid = false
        }
    }

    fun addVariant() {
        mQuestions[currentQuestionPosition]
            .variants
            .add(QuizMakerVariant.empty())
    }

    fun setVariantText(
        qPosition: Int,
        vPosition: Int,
        text: String
    ) {
        val variant = mQuestions.getOrNull(qPosition)
            ?.variants?.getOrNull(vPosition) ?: return

        variant.text = text
    }

    fun switchVariantValidState(
        qPosition: Int,
        vPosition: Int
    ) {
        val variants = mQuestions.getOrNull(qPosition)?.variants
        val targetVariant = variants?.getOrNull(vPosition) ?: return

        if (currentQuestion.isMultipleMode) {
            targetVariant.isValid = !targetVariant.isValid
            return
        }

        if (targetVariant.isValid) {
            return
        }

        variants.forEach { it.isValid = false }
        targetVariant.isValid = true
    }

    fun deleteQuestion() {
        if (mQuestions.count() < 2) {
            return
        }

        mQuestions.removeAt(currentQuestionPosition)
        currentQuestionPosition = currentQuestionPosition.coerceIn(0, mQuestions.lastIndex)
    }

    fun deleteVariantAt(qPosition: Int, vPosition: Int) {
        mQuestions.getOrNull(qPosition)
            ?.variants
            ?.removeAt(vPosition)
    }

}
