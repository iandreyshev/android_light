package ru.iandreyshev.light.ui.player

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R

class PlayerFragment : BaseFragment(R.layout.fragment_player) {

    private val mViewModel by viewModel<PlayerViewModel> {
        parametersOf(getScope(R.id.nav_player))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}