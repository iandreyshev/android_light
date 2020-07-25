package ru.iandreyshev.light.ui.editor

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_editor.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router

class EditorFragment : BaseFragment(R.layout.fragment_editor) {

    private val mViewModel: EditorViewModel by viewModel {
        parametersOf(getScope(R.id.nav_editor))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            mViewModel.onExit()
        }

        bottomAppBar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.addQuiz -> {
                    mViewModel.onOpenQuizMaker()
                    true
                }
                R.id.addImage -> {
                    mViewModel.onOpenImageMaker()
                    true
                }
                R.id.addVideo -> {
                    mViewModel.onOpenVideoMaker()
                    true
                }
                else -> false
            }
        }

        mViewModel.eventBackFromEditor {
            router().back()
        }
        mViewModel.eventOpenImageMaker {
            router().openImageMaker()
        }
        mViewModel.eventOpenVideoMaker {
            router().openVideoMaker()
        }
        mViewModel.eventOpenQuizMaker {
            router().openQuizMaker()
        }
    }

}
