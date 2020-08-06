package ru.iandreyshev.constructor.di

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.iandreyshev.constructor.R
import ru.iandreyshev.core_app.navGraphScope

val FEATURE_EDITOR_MODULE = module {
    navGraphScope(R.id.nav_editor) {
        factory<ExoPlayer> {
            SimpleExoPlayer.Builder(androidApplication()).build()
        }
    }
}
