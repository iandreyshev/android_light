package ru.iandreyshev.light.system

import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraph
import org.koin.android.ext.android.getKoin
import org.koin.core.Koin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.ext.getOrCreateScope
import org.koin.ext.getScopeId
import org.koin.ext.scope
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