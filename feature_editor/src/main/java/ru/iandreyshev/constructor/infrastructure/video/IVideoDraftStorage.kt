package ru.iandreyshev.constructor.infrastructure.video

import android.net.Uri
import ru.iandreyshev.constructor.domain.editor.files.VideoFiles
import ru.iandreyshev.constructor.domain.video.VideoSource

interface IVideoDraftStorage {
    fun getFiles(): VideoFiles
    fun save(uri: Uri): VideoSource?
    fun delete()
}
