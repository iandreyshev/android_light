package ru.iandreyshev.core_app

import androidx.lifecycle.ViewModel
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent.getKoin
import timber.log.Timber

class NavGraphScopeHolder(
    private val navGraphId: Int
) : ViewModel() {

    val scope: Scope by lazy {
        getKoin().getOrCreateScope(
            navGraphId.flowScopeId,
            navGraphId.flowQualifier
        )
    }

    override fun onCleared() {
        Timber.d("Close scope: $scope")
        scope.close()
    }

}