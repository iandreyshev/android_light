package ru.iandreyshev.constructor.ui.quizMaker

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_quiz_maker.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.constructor.R
import ru.iandreyshev.constructor.navigation.router
import ru.iandreyshev.core_app.BaseFragment
import ru.iandreyshev.core_ui.withItemListeners

class QuizMakerFragment : BaseFragment(R.layout.fragment_quiz_maker) {

    private val mViewModel by viewModel<QuizMakerViewModel> {
        val navArgs by navArgs<QuizMakerFragmentArgs>()
        parametersOf(getScope(R.id.nav_editor), navArgs.quizMakerArgs)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initVariantsList()
    }

    private fun initMenu() {
        toolbar.setNavigationOnClickListener { router().back() }
        toolbar.withItemListeners {
            R.id.actionQuizMakerSave { mViewModel.onSave() }
        }

        mViewModel.eventExit {
            router().back()
        }
    }

    private fun initVariantsList() {
        variantList.adapter = mViewModel.adapter
    }

}
