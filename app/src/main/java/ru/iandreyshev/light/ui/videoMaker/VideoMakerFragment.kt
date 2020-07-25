package ru.iandreyshev.light.ui.videoMaker

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_video_maker.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.utill.withItemListeners

class VideoMakerFragment : BaseFragment(R.layout.fragment_video_maker) {

    private val mViewModel by viewModel<VideoMakerViewModel> {
        parametersOf(getScope(R.id.nav_editor))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
    }

    private fun initMenu() {
        toolbar.setNavigationOnClickListener { router().back() }
        toolbar.withItemListeners {
            R.id.actionVideoMakerSave { mViewModel.onSave() }
        }

        mViewModel.eventExit { router().back() }
    }

}
