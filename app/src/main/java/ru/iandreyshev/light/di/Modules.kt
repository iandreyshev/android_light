package ru.iandreyshev.light.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.iandreyshev.constructor.di.FEATURE_EDITOR_MODULE
import ru.iandreyshev.constructor.domain.editor.ISaveCourseDraftUseCase
import ru.iandreyshev.constructor.ui.editor.EditorViewModel
import ru.iandreyshev.constructor.ui.imageMaker.ImageMakerViewModel
import ru.iandreyshev.constructor.ui.quizMaker.QuizMakerViewModel
import ru.iandreyshev.constructor.ui.videoMaker.VideoMakerViewModel
import ru.iandreyshev.core_app.navGraphScope
import ru.iandreyshev.light.R
import ru.iandreyshev.light.domain.SaveDraftUseCase
import ru.iandreyshev.light.infrastructure.courseList.InMemoryCourseRepository
import ru.iandreyshev.light.infrastructure.player.RepositoryDataSource
import ru.iandreyshev.light.ui.courseList.CourseListViewModel
import ru.iandreyshev.light.ui.player.PlayerViewModel
import ru.iandreyshev.player_core.course.ICourseRepository
import ru.iandreyshev.player_core.course.IPlayerDataSource

fun Context.initDI() = startKoin {
    androidContext(this@initDI)
    modules(
        MAIN_MODULE,
        FEATURE_EDITOR_MODULE
    )
}

private val MAIN_MODULE = module {
    single<ICourseRepository> { InMemoryCourseRepository() }

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
        QuizMakerViewModel(it.component1())
            .apply { onCreate() }
    }
    viewModel {
        VideoMakerViewModel(it.component1())
    }
    viewModel {
        ImageMakerViewModel(it.component1())
    }
    navGraphScope(R.id.nav_editor) {
        scoped<ISaveCourseDraftUseCase> {
            SaveDraftUseCase(get())
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
