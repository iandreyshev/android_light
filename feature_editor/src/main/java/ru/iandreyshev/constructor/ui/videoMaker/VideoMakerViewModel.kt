package ru.iandreyshev.constructor.ui.videoMaker

import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.constructor.domain.video.IVideoDraftRepository
import ru.iandreyshev.constructor.domain.video.VideoSource
import ru.iandreyshev.constructor.domain.video.draft.VideoDraft
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.core_ui.voidSingleLiveEvent
import ru.iandreyshev.core_utils.uiLazy

class VideoMakerViewModel(
    private val scope: Scope,
    private val args: VideoMakerArgs
) : ViewModel() {

    val player by uiLazy { mPlayer.distinctUntilChanged() }
    val hasVideo by uiLazy {
        player.map { it != null }
            .distinctUntilChanged()
    }

    val eventExit = voidSingleLiveEvent()

    private val mRepository by uiLazy {
        scope.get<IVideoDraftRepository> {
            parametersOf(args)
        }
    }
    private lateinit var mDraft: VideoDraft
    private val mPlayer = MutableLiveData<ExoPlayer?>(null)

    override fun onCleared() {
        GlobalScope.launch {
            mRepository.release()
            mPlayer.value?.release()
        }
    }

    fun onCreate() {
        viewModelScope.launch {
            mDraft = mRepository.get()
        }
    }

    fun onCreateDraft() {
        viewModelScope.launch {
            mRepository.save(mDraft)
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

        mDraft = mDraft.copy(source = VideoSource(uri.toString()))
    }

}
