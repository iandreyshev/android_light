package ru.iandreyshev.constructor.ui.imageMaker

data class ImageMakerViewState(
    val cameraState: CameraState,
    val text: String,
    val picture: String?
) {

    val hasText = text.isNotBlank()
    val hasPicture = picture != null

}
