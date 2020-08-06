package ru.iandreyshev.constructor.ui.imageMaker

import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.constructor.domain.image.IImageDraftRepository
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft
import ru.iandreyshev.constructor.domain.image.ImageSource
import ru.iandreyshev.core_ui.singleLiveEvent
import ru.iandreyshev.core_ui.voidSingleLiveEvent
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.core_utils.uiLazy
import timber.log.Timber

class ImageMakerViewModel(
    scope: Scope,
    imageMakerArgs: ImageMakerArgs
) : ViewModel() {

    val cameraAvailability by uiLazy { mCameraAvailability.distinctUntilChanged() }
    val textBalloon by uiLazy { mTextBalloon.distinctUntilChanged() }
    val picture by uiLazy { mPicture.distinctUntilChanged() }
    val hasPicture by uiLazy { picture.map { it != null } }

    val eventTakePhoto = singleLiveEvent<String>()
    val eventExit = voidSingleLiveEvent()

    private val mCameraAvailability = MutableLiveData(CameraState.AWAIT_PERMISSION)
    private val mTextBalloon = MutableLiveData("")
    private val mPicture = MutableLiveData<String?>(null)

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
        mCameraAvailability.value = when (isGranted) {
            true -> CameraState.AVAILABLE
            else -> CameraState.DISABLED
        }
    }

    fun onTakePhotoClick() {
        viewModelScope.launch {
            eventTakePhoto(mRepository.getImageFilePath())
        }
    }

    fun onChangeText(text: String?) {
        mDraft = mDraft.copy(text = text)
        mTextBalloon.value = mDraft.text.orEmpty()
    }

    fun onCreateDraft() {
        viewModelScope.launch {
            mRepository.save(mDraft)
            eventExit()
        }
    }

    fun onTakePhotoSuccess(filePath: String) {
        mPicture.value = filePath
        mDraft = mDraft.copy(source = ImageSource(filePath))
    }

    fun onPickFromGallery(uriString: String) {
        mPicture.value = uriString
        mDraft = mDraft.copy(source = ImageSource(uriString))
    }

    fun onTakePhotoError(exception: ImageCaptureException) {
        Timber.d("Error")
        Timber.d(exception)
    }

    fun onPictureLoadError(picture: Any?) {
        mPicture.value = null
        mDraft = mDraft.copy(source = null)
    }

}
