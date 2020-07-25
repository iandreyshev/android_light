package ru.iandreyshev.light.ui.imageMaker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.imageMaker.ISaveImageDraftUseCase
import ru.iandreyshev.light.domain.imageMaker.ImageDuration
import ru.iandreyshev.light.domain.imageMaker.draft.ImageDraft
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.uiLazy
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class ImageMakerViewModel(scope: Scope) : ViewModel() {

    val duration by uiLazy { mDuration.distinctUntilChanged() }
    val textBalloon by uiLazy { mTextBalloon.distinctUntilChanged() }

    val eventExit = voidSingleLiveEvent()

    private val mDuration = MutableLiveData(ImageDuration.SEC_3)
    private val mTextBalloon = MutableLiveData("")

    private val mDraft = ImageDraft()
    private val mSaveDraft by uiLazy { scope.get<ISaveImageDraftUseCase>() }

    fun onSwitchDuration() {
        mDuration.value = mDraft.switchDuration()
    }

    fun onChangeText(text: String?) {
        mTextBalloon.value = text.orEmpty()
    }

    fun onSave() {
        viewModelScope.launch {
            mSaveDraft(mDraft)
            eventExit()
        }
    }

}