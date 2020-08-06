package ru.iandreyshev.constructor.di

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.iandreyshev.constructor.R
import ru.iandreyshev.constructor.domain.editor.*
import ru.iandreyshev.constructor.domain.image.ISaveImageDraftUseCase
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft
import ru.iandreyshev.constructor.domain.quiz.ISaveQuizDraftUseCase
import ru.iandreyshev.constructor.domain.videoMaker.ISaveVideoDraftUseCase
import ru.iandreyshev.core_app.navGraphScope

val FEATURE_EDITOR_MODULE = module {
    navGraphScope(R.id.nav_editor) {
        scoped { CourseDraft(it.component1()) }

        scoped<ISaveQuizDraftUseCase> { AddQuizDraftToCourseUseCase(get()) }

        scoped { ImageDraft() }
        scoped<ISaveImageDraftUseCase> { AddImageDraftToCourseUseCase(get()) }

        scoped<ISaveVideoDraftUseCase> { AddVideoDraftToCourseUseCase(get()) }
        factory<ExoPlayer> {
            SimpleExoPlayer.Builder(androidApplication()).build()
        }
    }
}