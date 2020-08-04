package ru.iandreyshev.light.ui.player.video

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.scope.Scope
import ru.iandreyshev.light.ui.player.PlayerItemState

class VideoPlayerViewModel(
    private val scope: Scope
) : ViewModel() {

    private val mVideoUri = MutableLiveData<String>(null)

    fun render(state: PlayerItemState.Video) {
        if (mVideoUri.value == state.uri) {
            return
        }


    }

}