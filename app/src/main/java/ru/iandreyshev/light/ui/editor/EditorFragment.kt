package ru.iandreyshev.light.ui.editor

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_editor.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.utill.withItemListeners

class EditorFragment : BaseFragment(R.layout.fragment_editor) {

    private val mViewModel: EditorViewModel by viewModel {
        parametersOf(getScope(R.id.nav_editor))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
    }

    private fun initMenu() {
        toolbar.setNavigationOnClickListener { mViewModel.onExit() }

        bottomAppBar.withItemListeners {
            R.id.addQuiz { mViewModel.onOpenQuizMaker() }
            R.id.addImage { mViewModel.onOpenImageMaker() }
            R.id.addVideo { mViewModel.onOpenVideoMaker() }
        }

        mViewModel.eventBackFromEditor { router().back() }
        mViewModel.eventOpenImageMaker { router().openImageMaker() }
        mViewModel.eventOpenVideoMaker { router().openVideoMaker() }
        mViewModel.eventOpenQuizMaker { router().openQuizMaker() }
    }

}
