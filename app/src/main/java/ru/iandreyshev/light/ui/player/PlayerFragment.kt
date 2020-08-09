package ru.iandreyshev.light.ui.player

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_player.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.core_app.BaseFragment
import ru.iandreyshev.core_ui.setFullScreen
import ru.iandreyshev.core_ui.setOrientationPortrait
import ru.iandreyshev.core_ui.setOrientationUnspecified
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.player_core.player.News
import ru.iandreyshev.player_core.player.Wish

class PlayerFragment : BaseFragment(R.layout.fragment_player) {

    private val mArgs: PlayerFragmentArgs by navArgs()
    private val mViewModel by viewModel<PlayerViewModel> {
        parametersOf(getScope(R.id.nav_player), mArgs.playerArgs)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.liveData.subscribe(viewLifecycleOwner, playerView)
        mViewModel.liveData.eventShowNews(::onNews)

        playerView.subscribe(mViewModel.wishListener)
        playerView.setLifecycle(viewLifecycleOwner)
        playerView.onExitClick(router()::back)

        setFullScreen()
        setOrientationPortrait()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setOrientationUnspecified()
        setFullScreen(false)
    }

    private fun onNews(news: News) =
        when (news) {
            is News.ToastNews ->
                Toast.makeText(requireContext(), news.text, Toast.LENGTH_SHORT).show()
            is News.ShowQuiz -> Unit
        }

}
