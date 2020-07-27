package ru.iandreyshev.light.domain.quizMaker.draft

import ru.iandreyshev.light.domain.quizMaker.QuestionId
import ru.iandreyshev.light.domain.quizMaker.Quiz
import ru.iandreyshev.light.domain.quizMaker.VariantId

class QuizDraft constructor() {

    constructor(quiz: Quiz) : this() {
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

    val currentQuestion: QuestionDraft
        get() = mQuestions[mCurrentQuestionCursor]
    val currentQuestionPosition: Int
        get() = mCurrentQuestionCursor
    val hasPrevious: Boolean
        get() = currentQuestionPosition > 0
    val hasNext: Boolean
        get() = currentQuestionPosition < questionsCount - 1
    val questionsCount: Int
        get() = mQuestions.count()

    private var mQuestions = mutableListOf<QuestionDraft>()
    private var mCurrentQuestionCursor: Int = 0

    fun nextQuestion() {
        if (mCurrentQuestionCursor < mQuestions.lastIndex) {
            mCurrentQuestionCursor++
        }
    }

    fun previousQuestion() {
        if (mCurrentQuestionCursor > 0) {
            mCurrentQuestionCursor--
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

    fun updateQuestionText(position: Int, text: String) {
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

    fun updateVariantText(
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
        mCurrentQuestionCursor = mCurrentQuestionCursor.coerceIn(0, mQuestions.lastIndex)
    }

    fun deleteVariantAt(qPosition: Int, vPosition: Int) {
        mQuestions.getOrNull(qPosition)
            ?.variants
            ?.removeAt(vPosition)
    }

}

data class QuestionDraft(
    val id: QuestionId,
    var text: String,
    var isMultipleMode: Boolean,
    val variants: MutableList<VariantDraft>
)

data class VariantDraft(
    val id: VariantId,
    var text: String,
    var isValid: Boolean
) {

    companion object {
        fun empty() = VariantDraft(
            id = VariantId(""),
            text = "",
            isValid = false
        )
    }

}
