package ru.iandreyshev.constructor.ui.imageMaker

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
import ru.iandreyshev.core_ui.singleLiveEvent
import ru.iandreyshev.core_ui.voidSingleLiveEvent
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.core_utils.exhaustive
import ru.iandreyshev.core_utils.uiLazy
import timber.log.Timber

class ImageMakerViewModel(
    scope: Scope,
    imageMakerArgs: ImageMakerArgs
) : ViewModel() {

    val state by uiLazy { mState.distinctUntilChanged() }

    val eventTakePhoto = singleLiveEvent<String>()
    val eventShowError = singleLiveEvent<String>()
    val eventExit = voidSingleLiveEvent()

    private val mState = MutableLiveData<ImageMakerViewState>(
        ImageMakerViewState(
            cameraState = CameraState.AWAIT_PERMISSION,
            text = "",
            picture = null,
            isSaved = false
        )
    )

    private val mRepository by uiLazy {
        scope.get<IImageDraftRepository> {
            parametersOf(imageMakerArgs)
        }
    }
    private lateinit var mDraft: ImageDraft

    override fun onCleared() {
        GlobalScope.launch {
            if (mState.value?.isSaved == false) {
                mRepository.release()
            }
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
            eventTakePhoto(mRepository.getImageFilePath())
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
                    modifyState { copy(isSaved = true) }
                    eventExit()
                }
                is ImageDraftResult.Error -> when (saveResult.error) {
                    ImageDraftError.SOURCE_NOT_FOUND -> {
                        eventShowError("ERROR: Image source is null")
                    }
                }
            }.exhaustive
        }
    }

    fun onTakePhotoSuccess(filePath: String) {
        mDraft = mDraft.copy(source = ImageSource.Photo(filePath))
        modifyState {
            copy(
                picture = filePath,
                cameraState = CameraState.DISABLED
            )
        }
    }

    fun onPickFromGallerySuccess(uriString: String) {
        mDraft = mDraft.copy(source = ImageSource.Gallery(uriString))
        modifyState {
            copy(
                picture = uriString,
                cameraState = CameraState.DISABLED
            )
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

    private fun modifyState(stateModifier: ImageMakerViewState.() -> ImageMakerViewState) {
        mState.value = mState.value?.let(stateModifier)
    }

}
