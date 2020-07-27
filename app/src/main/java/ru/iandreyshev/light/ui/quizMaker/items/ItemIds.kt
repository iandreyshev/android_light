package ru.iandreyshev.light.ui.quizMaker.items

object ItemIds {
    const val QUESTION = 0L
    const val ADD_NEW_VARIANT_BUTTON = 1L
    const val QUESTION_SETTINGS = 2L

    fun variantIdFrom(position: Int): Long = position * 1000L
}
