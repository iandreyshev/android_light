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

    val state: LiveData<TState> by lazy(LazyThreadSafetyMode.NONE) { mState }
    val event: LiveData<Event<TEvent>> by lazy(LazyThreadSafetyMode.NONE) { mEvent }

    private val mState = MutableLiveData<TState>()
    private val mEvent = singleLiveEvent<TEvent>()

    init {
        initialState?.let(mState::setValue)
    }

    protected fun modifyState(modifier: TState.() -> TState) {
        mState.value = mState.value?.let(modifier)
    }

    protected fun event(eventBuilder: () -> TEvent) {
        mEvent(eventBuilder())
    }

}
