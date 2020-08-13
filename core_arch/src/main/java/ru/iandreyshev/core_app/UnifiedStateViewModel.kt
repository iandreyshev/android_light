package ru.iandreyshev.core_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.iandreyshev.core_ui.Event
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.core_ui.singleLiveEvent

abstract class UnifiedStateViewModel<TState : Any, TEvent : Any>(
    initialState: TState? = null
) : ViewModel() {

    val state by lazy<LiveData<TState>>(LazyThreadSafetyMode.NONE) { mState }
    val event by lazy<LiveData<Event<TEvent>>>(LazyThreadSafetyMode.NONE) { mEvent }

    private val mState = MutableLiveData<TState>()
    private val mEvent = singleLiveEvent<TEvent>()

    init {
        initialState?.let(mState::setValue)
    }

    fun modifyState(modifier: TState.() -> TState) {
        mState.value = mState.value?.let(modifier)
    }

    fun event(eventBuilder: () -> TEvent) {
        mEvent(eventBuilder())
    }

}
