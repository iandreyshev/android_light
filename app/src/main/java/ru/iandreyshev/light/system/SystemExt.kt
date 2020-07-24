package ru.iandreyshev.light.system

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.ScopeID

val Int.flowQualifier: Qualifier
    get() = named("FlowQualifier (NavGraphId@$this)")

fun flowQualifier(navGraphId: Int) =
    navGraphId.flowQualifier

val Int.flowScopeId: ScopeID
    get() = "ScopeId (NavGraphId@${this})"

fun flowScopeId(navGraphId: Int) =
    navGraphId.flowScopeId
