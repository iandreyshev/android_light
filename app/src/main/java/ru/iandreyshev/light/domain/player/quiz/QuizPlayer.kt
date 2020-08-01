package ru.iandreyshev.light.domain.player.quiz

class QuizPlayer : IQuizPlayer {

    override fun submit(): Question {
        return Question("", false, listOf(), null)
    }

}