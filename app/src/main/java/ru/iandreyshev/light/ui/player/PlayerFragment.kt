package ru.iandreyshev.light.ui.player

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.ui.player.mvi.State
import ru.iandreyshev.light.utill.exhaustive

class PlayerFragment : BaseFragment(R.layout.fragment_player) {

    private val mViewModel by viewModel<PlayerViewModel> {
        parametersOf(getScope(R.id.nav_player))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.state.viewObserveWith(::render)
    }

    private fun render(state: State) {
        when (state) {
            State.Preloading -> {
            }
            is State.PlayingQuiz -> {
            }
            is State.PlayingImage -> {
            }
        }.exhaustive
    }

}