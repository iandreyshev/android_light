package ru.iandreyshev.player_core.quiz


internal class QuizPlayerNewsPublisher : Function3<Wish, Effect, State, News?> {

    override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
        Effect.Finish -> News.QuizFinished
        else -> null
    }

}
