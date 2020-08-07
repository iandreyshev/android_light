package ru.iandreyshev.constructor.ui.quizMaker

import com.xwray.groupie.Item

data class QuizMakerViewState(
    /*
    * Mutable cause com.xwray.groupie.GroupAdapter require MutableCollection
    * */
    val items: ArrayList<Item<*>>
)
