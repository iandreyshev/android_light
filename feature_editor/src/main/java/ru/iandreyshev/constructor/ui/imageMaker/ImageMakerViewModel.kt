package ru.iandreyshev.constructor.ui.imageMaker

import android.net.Uri
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.*
import com.bumptech.glide.load.engine.GlideException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.constructor.domain.image.IImageDraftRepository
import ru.iandreyshev.constructor.domain.image.ImageDraftError
import ru.iandreyshev.constructor.domain.image.ImageDraftResult
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft
import ru.iandreyshev.constructor.domain.image.ImageSource
import ru.iandreyshev.core_app.UnifiedStateViewModel
import ru.iandreyshev.core_utils.exhaustive
import ru.iandreyshev.core_utils.uiLazy
import timber.log.Timber

class ImageMakerViewModel(
    scope: Scope,
    imageMakerArgs: ImageMakerArgs
) : UnifiedStateViewModel<ImageMakerViewState, ImageMakerEvent>(
    initialState = ImageMakerViewState(
        cameraState = CameraState.AWAIT_PERMISSION,
        text = "",
        picture = null
    )
) {

    private val mRepository by uiLazy {
        scope.get<IImageDraftRepository> {
            parametersOf(imageMakerArgs)
        }
    }
    private lateinit var mDraft: ImageDraft

    override fun onCleared() {
        GlobalScope.launch {
            mRepository.release()
        }
    }

    fun onCreate() {
        viewModelScope.launch {
            mDraft = mRepository.get()
        }
    }

    fun onCameraPermissionGranted(isGranted: Boolean) {
        modifyState {
            copy(
                cameraState = when (isGranted) {
                    true -> CameraState.AVAILABLE
                    else -> CameraState.AWAIT_PERMISSION
                }
            )
        }
    }

    fun onTakePhotoClick() {
        viewModelScope.launch {
            val source = mRepository.getPhotoSource()
            event { ImageMakerEvent.TakePhoto(source) }
        }
    }

    fun onOpenCameraClick() {
        mDraft = mDraft.copy(source = null)
        modifyState {
            copy(
                cameraState = CameraState.AVAILABLE,
                picture = null
            )
        }
    }

    fun onChangeText(text: String?) {
        mDraft = mDraft.copy(text = text)
        modifyState { copy(text = mDraft.text.orEmpty()) }
    }

    fun onSaveDraft() {
        viewModelScope.launch {
            when (val saveResult = mRepository.save(mDraft)) {
                ImageDraftResult.Success -> {
                    mRepository.release()
                    event { ImageMakerEvent.Exit }
                }
                is ImageDraftResult.Error -> when (saveResult.error) {
                    ImageDraftError.SOURCE_NOT_FOUND ->
                        event { ImageMakerEvent.ShowError("ERROR: Image source is null") }
                }
            }.exhaustive
        }
    }

    fun onTakePhotoSuccess(source: ImageSource.Photo) {
        mDraft = mDraft.copy(source = source)
        modifyState {
            copy(
                picture = source.filePath,
                cameraState = CameraState.DISABLED
            )
        }
    }

    fun onPickFromGallerySuccess(uri: Uri) {
        viewModelScope.launch {
            val source = mRepository.getGallerySource(uri) ?: kotlin.run {
                event { ImageMakerEvent.ShowError("Error while copy image from gallery") }
                return@launch
            }

            mDraft = mDraft.copy(source = source)
            modifyState {
                copy(
                    picture = source.filePath,
                    cameraState = CameraState.DISABLED
                )
            }
        }
    }

    fun onTakePhotoError(exception: ImageCaptureException) {
        Timber.d(exception)

        mDraft = mDraft.copy(source = null)
        modifyState {
            copy(
                picture = null,
                cameraState = CameraState.AVAILABLE
            )
        }
    }

    fun onPictureLoadError(exception: GlideException?) {
        Timber.d(exception)

        mDraft = mDraft.copy(source = null)
        modifyState {
            copy(
                picture = null,
                cameraState = CameraState.AVAILABLE
            )
        }
    }

    fun onExit() {
        viewModelScope.launch {
            mRepository.release()
            event { ImageMakerEvent.Exit }
        }
    }

}
