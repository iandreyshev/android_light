package ru.iandreyshev.light.domain

import ru.iandreyshev.constructor.domain.editor.CourseDraft

interface IDraftSerializer {
    fun save(draft: CourseDraft)
}
