package ru.iandreyshev.light.ui.videoMaker

import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.videoMaker.ISaveVideoDraftUseCase
import ru.iandreyshev.light.domain.videoMaker.VideoDraft
import ru.iandreyshev.light.domain.videoMaker.VideoSource
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.singleLiveEvent
import ru.iandreyshev.light.utill.uiLazy
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class VideoMakerViewModel(
    private val scope: Scope
) : ViewModel() {

    val player by uiLazy { mPlayer.distinctUntilChanged() }
    val hasVideo by uiLazy { player.map { it != null }.distinctUntilChanged() }

    private val mPlayer = MutableLiveData<ExoPlayer?>(null)

    private val mDraft = VideoDraft()

    val eventExit = voidSingleLiveEvent()
    val eventShowError = singleLiveEvent<String>()

    private val mSaveDraft by uiLazy { scope.get<ISaveVideoDraftUseCase>() }

    override fun onCleared() {
        mPlayer.value?.release()
    }

    fun onCreateDraft() {
        viewModelScope.launch {
            mSaveDraft(VideoDraft())
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

        mDraft.source = VideoSource(uri.toString())
    }

}
