package ru.iandreyshev.light

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
