package ru.iandreyshev.light

import android.widget.PopupMenu
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navGraphViewModels
import org.koin.core.scope.Scope
import ru.iandreyshev.light.system.NavGraphScopeHolder
import ru.iandreyshev.light.utill.Event
import ru.iandreyshev.light.utill.dismissOnDestroy

abstract class BaseFragment(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId) {

    protected val alerts = mutableListOf<AlertDialog>()
    protected val popupMenus = mutableListOf<PopupMenu>()

    override fun onDestroy() {
        super.onDestroy()
        alerts.forEach { it.dismissOnDestroy() }
        popupMenus.forEach { it.dismissOnDestroy() }
    }

    protected fun getScope(@IdRes navGraphId: Int): Scope? {
        val holder by navGraphViewModels<NavGraphScopeHolder>(navGraphId) {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    NavGraphScopeHolder(navGraphId) as T
            }
        }

        return holder.scope
    }

    protected fun <T> LiveData<T>.observeWith(action: (T) -> Unit) {
        this.observe(this@BaseFragment, Observer {
            it?.apply(action)
        })
    }

    protected fun <T> LiveData<T>.viewObserveWith(action: (T) -> Unit) {
        this.observe(viewLifecycleOwner, Observer {
            it?.apply(action)
        })
    }

    protected fun <T> LiveData<T>.viewObserveNullableWith(action: (T?) -> Unit) {
        this.observe(viewLifecycleOwner, Observer {
            it.apply(action)
        })
    }

    protected fun <T> LiveData<T>.observeNullable(action: (T?) -> Unit) {
        this.observe(this@BaseFragment, Observer {
            action(it)
        })
    }

    protected fun <T> LiveData<T>.observeEvent(action: () -> Unit) {
        this.observe(this@BaseFragment, Observer {
            action()
        })
    }

    protected fun <T> LiveData<T>.viewObserveEvent(action: () -> Unit) {
        this.observe(this@BaseFragment, Observer {
            action()
        })
    }

    protected operator fun LiveData<Event<Unit>>.invoke(listener: () -> Unit) {
        observeWith {
            it.consume {
                listener()
            }
        }
    }

    protected operator fun <T> LiveData<Event<T>>.invoke(listener: (T) -> Unit) {
        observeWith { it.consume(listener) }
    }

}
