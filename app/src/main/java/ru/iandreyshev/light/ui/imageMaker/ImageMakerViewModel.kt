package ru.iandreyshev.light.ui.imageMaker

import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.imageMaker.ISaveImageDraftUseCase
import ru.iandreyshev.light.domain.imageMaker.ImageDuration
import ru.iandreyshev.light.domain.imageMaker.ImageSource
import ru.iandreyshev.light.domain.imageMaker.draft.ImageDraft
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.uiLazy
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class ImageMakerViewModel(scope: Scope) : ViewModel() {

    val duration by uiLazy { mDuration.distinctUntilChanged() }
    val textBalloon by uiLazy { mTextBalloon.distinctUntilChanged() }
    val picture by uiLazy { mPicture.distinctUntilChanged() }

    val eventExit = voidSingleLiveEvent()

    private val mDuration = MutableLiveData(ImageDuration.SEC_3)
    private val mTextBalloon = MutableLiveData("")
    private val mPicture = MutableLiveData<Uri>(null)

    private val mDraft = ImageDraft()
    private val mSaveDraft by uiLazy { scope.get<ISaveImageDraftUseCase>() }

    fun onCreate() {
        mDuration.value = mDraft.duration
    }

    fun onSwitchDuration() {
        mDuration.value = mDraft.switchDuration()
    }

    fun onChangeText(text: String?) {
        mDraft.text = text
        mTextBalloon.value = mDraft.text.orEmpty()
    }

    fun onSave() {
        viewModelScope.launch {
            mSaveDraft(mDraft)
            eventExit()
        }
    }

    fun onPickFromGallery(uri: Uri) {
        mDraft.imageSource = ImageSource(uri.toString())
        mPicture.value = uri
    }

}
