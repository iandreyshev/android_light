package ru.iandreyshev.player_core.player

internal class PlayerNewsPublisher : Function3<Wish, Effect, State, News?> {

    override fun invoke(wish: Wish, effect: Effect, state: State): News? {
        return when (wish) {
            Wish.Exit -> return News.Exit
            else -> when (effect) {
                is Effect.Error -> News.ToastNews(effect.error)
                is Effect.PlayQuiz -> News.ShowQuiz(effect.quiz)
                else -> null
            }
        }
    }

}
