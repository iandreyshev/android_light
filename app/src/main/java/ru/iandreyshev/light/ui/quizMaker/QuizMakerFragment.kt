package ru.iandreyshev.light.ui.quizMaker

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import timber.log.Timber

class QuizMakerFragment : BaseFragment(R.layout.fragment_quiz_maker) {

    private val mViewModel by viewModel<QuizMakerViewModel> {
        parametersOf(getScope(R.id.nav_editor))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("QuizMakerViewModel $mViewModel initialized")
    }

}
