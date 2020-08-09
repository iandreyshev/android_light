package ru.iandreyshev.light.ui.player

import androidx.lifecycle.ViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.core_utils.uiLazy
import ru.iandreyshev.player_core.player.*
import ru.iandreyshev.player.PlayerLiveDataHolder
import ru.iandreyshev.player_core.IWishListener
import ru.iandreyshev.player_core.Player

class PlayerViewModel(
    scope: Scope,
    private val playerArgs: PlayerArgs
) : ViewModel() {

    val liveData by uiLazy { PlayerLiveDataHolder(mPlayer) }
    val wishListener: IWishListener by uiLazy { mPlayer }

    private val mPlayer = Player(
        dataSource = scope.get {
            parametersOf(playerArgs.courseId)
        }
    )

    fun onCreate() {
        mPlayer(Wish.Start)
    }

    override fun onCleared() {
        mPlayer.dispose()
    }

}
