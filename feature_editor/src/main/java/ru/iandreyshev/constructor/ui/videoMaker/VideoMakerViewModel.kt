package ru.iandreyshev.constructor.ui.videoMaker

import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.constructor.domain.video.IVideoDraftRepository
import ru.iandreyshev.constructor.domain.video.draft.VideoDraft
import ru.iandreyshev.core_app.UnifiedStateViewModel
import ru.iandreyshev.core_utils.uiLazy

class VideoMakerViewModel(
    private val scope: Scope,
    private val args: VideoMakerArgs
) : UnifiedStateViewModel<VideoMakerViewState, VideoMakerEvent>(
    initialState = VideoMakerViewState(
        isLoading = true,
        title = "Unnamed video",
        player = null,
        duration = 0,
        position = 0
    )
) {

    private val mRepository by uiLazy {
        scope.get<IVideoDraftRepository> {
            parametersOf(args)
        }
    }
    private lateinit var mDraft: VideoDraft

    override fun onCleared() {
        if (mDraft.source == null) {
            GlobalScope.launch { mRepository.clear() }
        }
    }

    fun onCreate() {
        viewModelScope.launch {
            mDraft = mRepository.get()
            modifyState {
                copy(
                    isLoading = false,
                    title = mDraft.title
                )
            }
            event { VideoMakerEvent.PickVideo }

            while (coroutineContext.isActive) {
                delay(TIMELINE_UPDATE_DELAY)
                modifyState {
                    copy(
                        position = player?.currentPosition?.toInt() ?: position,
                        duration = player?.duration?.toInt() ?: duration
                    )
                }
            }
        }
    }

    fun onCreateDraft() {
        viewModelScope.launch {
            mRepository.save(mDraft)
            event { VideoMakerEvent.Exit }
        }
    }

    fun onPickFromGallery(uri: Uri?) {
        uri ?: kotlin.run {
            event { VideoMakerEvent.Exit }
            return
        }

        viewModelScope.launch {
            mDraft = mDraft.copy(
                source = mRepository.getGallerySource(uri) ?: kotlin.run {
                    event { VideoMakerEvent.ShowError("Error while video from gallery") }
                    releasePlayer()
                    event { VideoMakerEvent.Exit }
                    return@launch
                }
            )

            releasePlayer()

            val player = scope.get<ExoPlayer>()
            val dataSourceFactory = DefaultDataSourceFactory(
                scope.androidApplication(),
                Util.getUserAgent(scope.androidApplication(), "Light")
            )
            val videoUri = Uri.parse(mDraft.source?.filePath)
            val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri)

            player.prepare(videoSource)

            modifyState {
                copy(
                    player = player,
                    duration = player.duration.toInt()
                )
            }
        }
    }

    fun onChangeTitle(title: String) {
        mDraft = mDraft.copy(title = title)
        modifyState { copy(title = mDraft.title) }
    }

    fun onSeekTo(position: Int) {
        state.value?.player?.seekTo(position.toLong())
    }

    fun onPause() {
        state.value?.player?.let { player ->
            if (player.playWhenReady) {
                player.playWhenReady = false
            }
        }
    }

    fun onExit() {
        releasePlayer()
        event { VideoMakerEvent.Exit }
    }

    private fun releasePlayer() {
        state.value?.player?.let { player ->
            player.stop()
            player.release()

            modifyState {
                copy(player = null)
            }
        }
    }

    companion object {
        private const val TIMELINE_UPDATE_DELAY = 1000L
    }

}
