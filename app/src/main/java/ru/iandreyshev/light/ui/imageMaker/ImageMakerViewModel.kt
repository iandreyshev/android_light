package ru.iandreyshev.light.ui.imageMaker

import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.imageMaker.ISaveImageDraftUseCase
import ru.iandreyshev.light.domain.imageMaker.ImageSource
import ru.iandreyshev.light.domain.imageMaker.draft.ImageDraft
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.uiLazy
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class ImageMakerViewModel(scope: Scope) : ViewModel() {

    val textBalloon by uiLazy { mTextBalloon.distinctUntilChanged() }
    val picture by uiLazy { mPicture.distinctUntilChanged() }
    val hasPicture by uiLazy { picture.map { it != null } }

    val eventExit = voidSingleLiveEvent()

    private val mTextBalloon = MutableLiveData("")
    private val mPicture = MutableLiveData<Uri?>(null)

    private val mDraft = ImageDraft()
    private val mSaveDraft by uiLazy { scope.get<ISaveImageDraftUseCase>() }

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
        mDraft.source = ImageSource(uri.toString())
    }

    fun onPictureLoadError(picture: Any?) {
        mPicture.value = null
        mDraft.source = null
    }

}
