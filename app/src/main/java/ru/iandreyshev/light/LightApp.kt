package ru.iandreyshev.light

import android.app.Application
import ru.iandreyshev.light.di.initDI
import timber.log.Timber

class LightApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
        initTimber()

        Timber.d("NavGraphId R.id.nav_main ${R.id.nav_main}")
        Timber.d("NavGraphId R.id.nav_editor ${R.id.nav_editor}")
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
        Timber.d("Timber initialized")
    }

}
