package ru.iandreyshev.constructor.ui.quizMaker

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_quiz_maker.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.constructor.R
import ru.iandreyshev.constructor.navigation.router
import ru.iandreyshev.core_app.BaseFragment
import ru.iandreyshev.core_ui.withItemListeners
import ru.iandreyshev.core_utils.uiLazy

class QuizMakerFragment : BaseFragment(R.layout.fragment_quiz_maker) {

    private val mViewModel by viewModel<QuizMakerViewModel> {
        val navArgs by navArgs<QuizMakerFragmentArgs>()
        parametersOf(getScope(R.id.nav_editor), navArgs.quizMakerArgs)
    }
    private val mAdapter by uiLazy { GroupAdapter<GroupieViewHolder>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initVariantsList()

        mViewModel.state.viewObserveWith(::render)
    }

    private fun initMenu() {
        toolbar.setNavigationOnClickListener { router().back() }
        toolbar.withItemListeners {
            R.id.actionQuizMakerSave { mViewModel.onSave() }
        }

        mViewModel.eventExit(router()::back)
    }

    private fun initVariantsList() {
        variantList.adapter = mAdapter
    }

    private fun render(state: QuizMakerViewState) {
        if (state.items.isEmpty()) {
            progressBar.isVisible = true
            variantList.isVisible = false
            return
        }

        progressBar.isVisible = false
        variantList.isVisible = true

        mAdapter.update(state.items)
    }

}
