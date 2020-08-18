package ru.iandreyshev.light.ui.player

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import dev.chrisbanes.insetter.applySystemGestureInsetsToPadding
import kotlinx.android.synthetic.main.fragment_player.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.core_app.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.player_core.player.News

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

        playerViewWrap.applySystemGestureInsetsToPadding(top = true, bottom = true)
    }

    override fun onPause() {
        super.onPause()
        playerView.pause()
    }

    private fun onNews(news: News) =
        when (news) {
            is News.ToastNews ->
                Toast.makeText(requireContext(), news.text, Toast.LENGTH_SHORT).show()
            is News.ShowQuiz -> Unit
            News.Exit -> router.back()
        }

}
