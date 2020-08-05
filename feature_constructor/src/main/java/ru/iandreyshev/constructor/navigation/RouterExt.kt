package ru.iandreyshev.constructor.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

internal fun Fragment.router(): IRouter =
    NavComponentRouter(findNavController())
