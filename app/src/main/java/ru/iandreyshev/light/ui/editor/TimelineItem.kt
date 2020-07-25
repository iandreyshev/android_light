package ru.iandreyshev.light.ui.editor

sealed class TimelineItem {
    class Quiz() : TimelineItem()
    class Image() : TimelineItem()
    class Video() : TimelineItem()
}
