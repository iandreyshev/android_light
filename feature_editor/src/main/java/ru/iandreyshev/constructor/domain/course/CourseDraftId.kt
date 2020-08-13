package ru.iandreyshev.constructor.domain.course

import ru.iandreyshev.constructor.utils.newUID
import java.io.Serializable

inline class CourseDraftId(val value: String) : Serializable {

    companion object {
        fun random() = CourseDraftId(newUID())
    }

}
