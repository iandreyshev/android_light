package ru.iandreyshev.constructor.ui.videoMaker

import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.core.scope.Scope
import ru.iandreyshev.constructor.domain.videoMaker.ISaveVideoDraftUseCase
import ru.iandreyshev.constructor.domain.videoMaker.VideoDraft
import ru.iandreyshev.constructor.domain.videoMaker.VideoSource
import ru.iandreyshev.core_ui.voidSingleLiveEvent
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.core_utils.uiLazy

class VideoMakerViewModel(
    private val scope: Scope
) : ViewModel() {

    val player by uiLazy { mPlayer.distinctUntilChanged() }
    val hasVideo by uiLazy {
        player.map { it != null }.distinctUntilChanged()
    }

    private val mPlayer = MutableLiveData<ExoPlayer?>(null)

    private val mDraft = VideoDraft()

    val eventExit = voidSingleLiveEvent()

    private val mSaveDraft by uiLazy { scope.get<ISaveVideoDraftUseCase>() }

    override fun onCleared() {
        mPlayer.value?.release()
    }

    fun onCreateDraft() {
        viewModelScope.launch {
            mSaveDraft(mDraft)
            eventExit()
        }
    }

    fun onPickFromGallery(uri: Uri) {
        mPlayer.value?.release()
        mPlayer.value = null

        val player = scope.get<ExoPlayer>()
        val dataSourceFactory = DefaultDataSourceFactory(
            scope.androidApplication(),
            Util.getUserAgent(scope.androidApplication(), "Light")
        )
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)

        player.prepare(videoSource)

        mPlayer.value = player

        mDraft.source =
            VideoSource(uri.toString())
    }

}
