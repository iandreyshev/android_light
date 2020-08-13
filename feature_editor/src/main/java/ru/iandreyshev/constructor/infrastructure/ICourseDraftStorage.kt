package ru.iandreyshev.constructor.infrastructure

import java.io.File

interface ICourseDraftStorage {
    fun getOrCreateCourseFolder(): File
    fun clear()
}
