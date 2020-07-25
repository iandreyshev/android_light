package ru.iandreyshev.light.ui.videoMaker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.videoMaker.ISaveVideoDraftUseCase
import ru.iandreyshev.light.domain.videoMaker.VideoDraft
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.singleLiveEvent
import ru.iandreyshev.light.utill.uiLazy
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class VideoMakerViewModel(scope: Scope) : ViewModel() {

    val eventExit = voidSingleLiveEvent()
    val eventShowError = singleLiveEvent<String>()

    private val mSaveDraft by uiLazy { scope.get<ISaveVideoDraftUseCase>() }

    fun onSave() {
        viewModelScope.launch {
            mSaveDraft(VideoDraft())
            eventExit()
        }
    }

}