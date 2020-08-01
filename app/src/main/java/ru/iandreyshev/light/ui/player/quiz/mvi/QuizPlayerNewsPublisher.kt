package ru.iandreyshev.light.ui.player.quiz.mvi


class QuizPlayerNewsPublisher : Function3<Wish, Effect, State, News?> {

    override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
        Effect.Finish -> News.QuizFinished
        else -> null
    }

}
