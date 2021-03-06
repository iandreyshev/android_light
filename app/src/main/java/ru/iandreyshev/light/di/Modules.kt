package ru.iandreyshev.light.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import ru.iandreyshev.constructor.di.FEATURE_EDITOR_MODULE
import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.infrastructure.image.IImageDraftStorage
import ru.iandreyshev.constructor.infrastructure.video.IVideoDraftStorage
import ru.iandreyshev.constructor.domain.image.IImageDraftRepository
import ru.iandreyshev.constructor.domain.quiz.IQuizDraftRepository
import ru.iandreyshev.constructor.domain.video.IVideoDraftRepository
import ru.iandreyshev.constructor.infrastructure.CourseDraftStorage
import ru.iandreyshev.constructor.infrastructure.ICourseDraftStorage
import ru.iandreyshev.constructor.infrastructure.video.VideoDraftStorage
import ru.iandreyshev.constructor.infrastructure.image.ImageDraftStorage
import ru.iandreyshev.constructor.ui.editor.EditorArgs
import ru.iandreyshev.constructor.ui.editor.EditorViewModel
import ru.iandreyshev.constructor.ui.imageMaker.ImageMakerArgs
import ru.iandreyshev.constructor.ui.imageMaker.ImageMakerViewModel
import ru.iandreyshev.constructor.ui.quizMaker.QuizMakerArgs
import ru.iandreyshev.constructor.ui.quizMaker.QuizMakerViewModel
import ru.iandreyshev.constructor.ui.videoMaker.VideoMakerArgs
import ru.iandreyshev.constructor.ui.videoMaker.VideoMakerViewModel
import ru.iandreyshev.core_app.navGraphScope
import ru.iandreyshev.light.R
import ru.iandreyshev.light.domain.ICourseRepository
import ru.iandreyshev.light.domain.IDraftSerializer
import ru.iandreyshev.light.infrastructure.courseList.CourseStorage
import ru.iandreyshev.light.infrastructure.courseList.InMemoryCourseRepository
import ru.iandreyshev.light.infrastructure.editor.CourseDraftRepository
import ru.iandreyshev.light.infrastructure.editor.ImageDraftRepository
import ru.iandreyshev.light.infrastructure.editor.QuizDraftRepository
import ru.iandreyshev.light.infrastructure.editor.VideoDraftRepository
import ru.iandreyshev.light.infrastructure.player.RepositoryDataSource
import ru.iandreyshev.light.ui.courseList.CourseListViewModel
import ru.iandreyshev.light.ui.player.PlayerViewModel
import ru.iandreyshev.player_core.course.IPlayerDataSource

fun Context.initDI() = startKoin {
    androidContext(this@initDI)

    modules(
        MAIN_MODULE,
        FEATURE_EDITOR_MODULE
    )
}

private val MAIN_MODULE = module {
    single {
        InMemoryCourseRepository(
            storage = CourseStorage(
                context = androidContext()
            )
        )
    }
    single<ICourseRepository> { get<InMemoryCourseRepository>() }
    single<IDraftSerializer> { get<InMemoryCourseRepository>() }

    viewModel {
        CourseListViewModel(it.component1())
            .apply { onCreate() }
    }
    navGraphScope(R.id.nav_main)

    viewModel {
        EditorViewModel(it.component1(), it.component2())
            .apply { onCreate() }
    }
    viewModel {
        QuizMakerViewModel(it.component1(), it.component2())
            .apply { onCreate() }
    }
    viewModel {
        VideoMakerViewModel(it.component1(), it.component2())
            .apply { onCreate() }
    }
    viewModel {
        ImageMakerViewModel(it.component1(), it.component2())
            .apply { onCreate() }
    }
    navGraphScope(R.id.nav_editor) {
        factory<ICourseDraftStorage> {
            CourseDraftStorage(
                context = androidContext(),
                courseId = it.component1()
            )
        }
        factory<IVideoDraftStorage> {
            VideoDraftStorage(
                context = androidContext(),
                courseStorage = get { parametersOf(it.component1()) },
                videoId = it.component2()
            )
        }
        factory<IImageDraftStorage> {
            ImageDraftStorage(
                context = androidContext(),
                courseStorage = get { parametersOf(it.component1()) },
                imageId = it.component2()
            )
        }
        factory<IImageDraftRepository> {
            val args = it.component1<ImageMakerArgs>()

            ImageDraftRepository(
                id = args.imageDraftId,
                courseRepository = get(),
                storage = get {
                    parametersOf(args.courseDraftId, args.imageDraftId)
                }
            )
        }
        factory<IVideoDraftRepository> {
            val args: VideoMakerArgs = it.component1()

            VideoDraftRepository(
                id = args.videoDraftId,
                courseRepository = get(),
                storage = get {
                    parametersOf(args.courseDraftId, args.videoDraftId)
                }
            )
        }
        scoped<ICourseDraftRepository> {
            val editorArgs: EditorArgs = it.component1()

            CourseDraftRepository(
                id = editorArgs.courseDraftId,
                defaultTitle = editorArgs.courseTitle,
                storage = get { parametersOf(editorArgs.courseDraftId) },
                serializer = get()
            )
        }
        scoped<IQuizDraftRepository> {
            val args: QuizMakerArgs = it.component1()

            QuizDraftRepository(
                id = args.quizDraftId,
                courseRepository = get()
            )
        }
    }

    viewModel {
        PlayerViewModel(
            it.component1(),
            it.component2()
        ).apply { onCreate() }
    }
    navGraphScope(R.id.nav_player) {
        scoped<IPlayerDataSource> {
            RepositoryDataSource(
                it.component1(),
                get()
            )
        }
    }
}
