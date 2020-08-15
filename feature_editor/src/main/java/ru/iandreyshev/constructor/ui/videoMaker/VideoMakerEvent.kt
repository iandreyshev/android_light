package ru.iandreyshev.constructor.ui.videoMaker

sealed class VideoMakerEvent {
    object PickVideo : VideoMakerEvent()
    data class ShowError(val text: String) : VideoMakerEvent()
    object Exit : VideoMakerEvent()
}
