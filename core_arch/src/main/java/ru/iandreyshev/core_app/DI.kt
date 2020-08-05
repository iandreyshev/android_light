package ru.iandreyshev.core_app

import androidx.annotation.IdRes
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.ScopeID
import org.koin.dsl.ScopeDSL

fun Module.navGraphScope(@IdRes id: Int, scopeSet: ScopeDSL.() -> Unit = {}) =
    scope(flowQualifier(id), scopeSet)

val Int.flowQualifier: Qualifier
    get() = named("FlowQualifier (NavGraphId@$this)")

fun flowQualifier(navGraphId: Int) =
    navGraphId.flowQualifier

val Int.flowScopeId: ScopeID
    get() = "ScopeId (NavGraphId@${this})"
