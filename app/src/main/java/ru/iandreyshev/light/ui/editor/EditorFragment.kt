package ru.iandreyshev.light.ui.editor

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_editor.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.utill.uiLazy
import ru.iandreyshev.light.utill.withItemListeners

class EditorFragment : BaseFragment(R.layout.fragment_editor) {

    private val mViewModel: EditorViewModel by viewModel {
        parametersOf(getScope(R.id.nav_editor))
    }
    private val mTimelineAdapter by uiLazy { TimelineAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.onCreate()

        initMenu()
        initTimeline()
    }

    private fun initMenu() {
        toolbar.setNavigationOnClickListener { mViewModel.onExit() }
        toolbar.withItemListeners {
            R.id.actionEditorSave { mViewModel.onSave() }
        }

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

    private fun initTimeline() {
        timeline.adapter = mTimelineAdapter
        buildItemTouchHelper()
            .attachToRecyclerView(timeline)
        mViewModel.timelineItems.viewObserveWith(mTimelineAdapter::submitList)
    }

    private fun buildItemTouchHelper() =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(UP or DOWN, 0) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                mViewModel.onMove(from, to)
                recyclerView.adapter?.notifyItemMoved(from, to)

                return true
            }
        })

}
