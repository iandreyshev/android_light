package ru.iandreyshev.constructor.domain.quiz.draft

import ru.iandreyshev.constructor.domain.course.CourseItem
import ru.iandreyshev.constructor.domain.quiz.QuestionId

class QuizDraft constructor() {

    constructor(quiz: CourseItem.Quiz) : this() {
        mQuestions = quiz.questions
            .map { question ->
                QuestionDraft(
                    id = question.id,
                    text = question.text,
                    isMultipleMode = question.isMultipleMode,
                    variants = question.variants
                        .map { variant ->
                            VariantDraft(
                                id = variant.id,
                                text = variant.text,
                                isValid = variant.isValid
                            )
                        }
                        .toMutableList()
                )
            }
            .toMutableList()
    }

    val questions: List<QuestionDraft>
        get() = mQuestions
    val currentQuestion: QuestionDraft
        get() = mQuestions[mCurrentQuestionPosition]
    val currentQuestionPosition: Int
        get() = mCurrentQuestionPosition
    val hasPrevious: Boolean
        get() = currentQuestionPosition > 0
    val hasNext: Boolean
        get() = currentQuestionPosition < questionsCount - 1
    val questionsCount: Int
        get() = mQuestions.count()

    private var mQuestions = mutableListOf<QuestionDraft>()
    private var mCurrentQuestionPosition: Int = 0

    fun moveToNextQuestion() {
        if (mCurrentQuestionPosition < mQuestions.lastIndex) {
            mCurrentQuestionPosition++
        }
    }

    fun moveToPreviousQuestion() {
        if (mCurrentQuestionPosition > 0) {
            mCurrentQuestionPosition--
        }
    }

    fun addQuestion(): Int {
        val question = QuestionDraft(
            id = QuestionId(""),
            text = "",
            isMultipleMode = false,
            variants = mutableListOf()
        )
        mQuestions.add(question)

        return mQuestions.lastIndex
    }

    fun setQuestionText(position: Int, text: String) {
        val question = mQuestions.getOrNull(position) ?: return
        question.text = text
    }

    fun switchQuestionMultipleMode(position: Int) {
        val question = mQuestions.getOrNull(position) ?: return
        question.isMultipleMode = !question.isMultipleMode
        question.variants.forEach {
            it.isValid = false
        }
    }

    fun addVariant(qPosition: Int) {
        mQuestions[qPosition].variants.add(VariantDraft.empty())
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

    fun deleteQuestionAt(position: Int) {
        if (mQuestions.count() < 2) {
            return
        }

        mQuestions.removeAt(position)
        mCurrentQuestionPosition = mCurrentQuestionPosition.coerceIn(0, mQuestions.lastIndex)
    }

    fun deleteVariantAt(qPosition: Int, vPosition: Int) {
        mQuestions.getOrNull(qPosition)
            ?.variants
            ?.removeAt(vPosition)
    }

}
