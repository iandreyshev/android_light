package ru.iandreyshev.constructor.domain.quiz.quizMaker

fun QuizMakerVariant.isValid() =
    text.isNotBlank()

fun QuizMakerQuestion.isValid() =
    text.isNotBlank() && variants.count { it.isValid() } > 1
