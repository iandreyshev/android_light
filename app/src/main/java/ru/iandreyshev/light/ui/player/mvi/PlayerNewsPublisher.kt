package ru.iandreyshev.light.ui.player.mvi

class PlayerNewsPublisher : Function3<Wish, Effect, State, News?> {

    override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
        is Effect.Error -> News.ToastNews(effect.error)
        is Effect.PlayQuiz -> News.ShowQuiz(effect.quiz)
        else -> null
    }

}
