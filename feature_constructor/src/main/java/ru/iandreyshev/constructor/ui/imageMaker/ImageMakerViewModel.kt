package ru.iandreyshev.constructor.ui.imageMaker

import android.net.Uri
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.constructor.domain.image.ISaveImageDraftUseCase
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft
import ru.iandreyshev.constructor.domain.image.draft.ImageSource
import ru.iandreyshev.core_ui.singleLiveEvent
import ru.iandreyshev.core_ui.voidSingleLiveEvent
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.core_utils.uiLazy
import timber.log.Timber

// TODO: 8/5/2020 Перевести состояние экрана в один data-класс
class ImageMakerViewModel(scope: Scope) : ViewModel() {

    val cameraAvailability by uiLazy { mCameraAvailability.distinctUntilChanged() }
    val textBalloon by uiLazy { mTextBalloon.distinctUntilChanged() }
    val picture by uiLazy { mPicture.distinctUntilChanged() }
    val hasPicture by uiLazy { picture.map { it != null } }

    val eventTakePhoto = singleLiveEvent<String>()
    val eventExit = voidSingleLiveEvent()

    private val mCameraAvailability = MutableLiveData(CameraState.AWAIT_PERMISSION)
    private val mTextBalloon = MutableLiveData("")
    private val mPicture = MutableLiveData<Uri?>(null)

    private val mDraft by uiLazy { scope.get<ImageDraft>() }
    private val mSaveDraft by uiLazy { scope.get<ISaveImageDraftUseCase>() }

    fun onCameraPermissionGranted(isGranted: Boolean) {
        mCameraAvailability.value = when (isGranted) {
            true -> CameraState.AVAILABLE
            else -> CameraState.DISABLED
        }
    }

    fun onTakePhoto() {
        eventTakePhoto("file.temp")
    }

    fun onTakePhotoSuccess(uri: Uri?) {
        Timber.d("Photo captured")
    }

    fun onTakePhotoError(exception: ImageCaptureException) {
        Timber.d("Error")
        Timber.d(exception)
    }

    fun onChangeText(text: String?) {
        mDraft.text = text
        mTextBalloon.value = mDraft.text.orEmpty()
    }

    fun onCreateDraft() {
        viewModelScope.launch {
            mSaveDraft(mDraft)
            eventExit()
        }
    }

    fun onPickFromGallery(uri: Uri) {
        mPicture.value = uri
        mDraft.source =
            ImageSource(uri.toString())
    }

    fun onPictureLoadError(picture: Any?) {
        mPicture.value = null
        mDraft.source = null
    }

}
