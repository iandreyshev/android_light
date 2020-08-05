package ru.iandreyshev.core_ui

import androidx.lifecycle.MutableLiveData

class Event<T>(val value: T) {
    var isConsumed: Boolean = false
        private set

    fun consume(action: (T) -> Unit) {
        if (!isConsumed) {
            action(value)
            isConsumed = true
        }
    }
}

fun <T> singleLiveEvent() = MutableLiveData<Event<T>>()
fun <T> singleLiveEvent(default: T) = MutableLiveData<Event<T>>(
    Event(default)
)
fun voidSingleLiveEvent() = MutableLiveData<Event<Unit>>()

operator fun MutableLiveData<Event<Unit>>.invoke() =
    postValue(Event(Unit))

operator fun <T> MutableLiveData<Event<T>>.invoke(value: T) =
    postValue(Event(value))
