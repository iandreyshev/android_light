package ru.iandreyshev.light.infrastructure.filePickers

import androidx.fragment.app.Fragment

interface IPhotoPicker {
    fun pickFromCamera(fragment: Fragment)
    fun pickFromGallery(fragment: Fragment)
}
