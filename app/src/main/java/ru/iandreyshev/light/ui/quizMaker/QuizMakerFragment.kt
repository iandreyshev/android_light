package ru.iandreyshev.light.ui.quizMaker

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_quiz_maker.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.utill.withItemListeners

class QuizMakerFragment : BaseFragment(R.layout.fragment_quiz_maker) {

    private val mViewModel by viewModel<QuizMakerViewModel> {
        parametersOf(getScope(R.id.nav_editor))
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
