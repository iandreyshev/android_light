package ru.iandreyshev.light.infrastructure.filePickers

import androidx.fragment.app.Fragment

interface IVideoPicker {
    fun pickFromCamera(fragment: Fragment)
    fun pickFromGallery(fragment: Fragment)
}
