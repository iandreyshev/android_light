package ru.iandreyshev.light.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.iandreyshev.light.R
import ru.iandreyshev.light.domain.courseList.ICourseRepository
import ru.iandreyshev.light.domain.editor.*
import ru.iandreyshev.light.domain.imageMaker.ISaveImageDraftUseCase
import ru.iandreyshev.light.domain.quizMaker.IQuizMakerRepository
import ru.iandreyshev.light.domain.quizMaker.ISaveQuizDraftUseCase
import ru.iandreyshev.light.domain.videoMaker.ISaveVideoDraftUseCase
import ru.iandreyshev.light.domain.courseList.SaveDraftUseCase
import ru.iandreyshev.light.infrastructure.courseList.InMemoryCourseRepository
import ru.iandreyshev.light.infrastructure.editor.QuizMakerRepository
import ru.iandreyshev.light.ui.courseList.CourseListViewModel
import ru.iandreyshev.light.ui.editor.EditorViewModel
import ru.iandreyshev.light.ui.imageMaker.ImageMakerViewModel
import ru.iandreyshev.light.ui.quizMaker.QuizMakerViewModel
import ru.iandreyshev.light.ui.videoMaker.VideoMakerViewModel
import ru.iandreyshev.light.ui.viewer.ViewerViewModel
import ru.iandreyshev.light.system.flowQualifier

fun Application.initDI() = startKoin {
    androidContext(this@initDI)
    modules(listOf(
        module {
            single<ICourseRepository> { InMemoryCourseRepository() }

            viewModel { CourseListViewModel(it.component1()) }
            scope(flowQualifier(R.id.nav_main)) {
            }

            viewModel { EditorViewModel(it.component1(), it.component2()) }
            viewModel { QuizMakerViewModel(it.component1()) }
            viewModel { VideoMakerViewModel(it.component1()) }
            viewModel { ImageMakerViewModel(it.component1()) }
            scope(flowQualifier(R.id.nav_editor)) {
                scoped { CourseDraft(it.component1()) }
                scoped<ISaveCourseDraftUseCase> { SaveDraftUseCase(get()) }
                scoped<IQuizMakerRepository> { QuizMakerRepository() }
                scoped<ISaveQuizDraftUseCase> { AddQuizDraftToCourseUseCase(get()) }
                scoped<ISaveImageDraftUseCase> { AddImageDraftToCourseUseCase(get()) }
                scoped<ISaveVideoDraftUseCase> { AddVideoDraftToCourseUseCase(get()) }
            }

            viewModel { ViewerViewModel() }
        }
    ))
}
