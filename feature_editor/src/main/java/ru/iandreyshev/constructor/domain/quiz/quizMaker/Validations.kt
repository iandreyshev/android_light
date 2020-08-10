package ru.iandreyshev.constructor.domain.quiz.quizMaker

fun QuizMakerVariant.isValid() =
    text.isNotBlank()

fun QuizMakerQuestion.isValid(): Boolean {
    if (text.isBlank()) {
        return false
    }

    val validVariants = variants.filter { it.isValid() }

    return validVariants.count() > 1
            && validVariants.any { it.isCorrect }
}
