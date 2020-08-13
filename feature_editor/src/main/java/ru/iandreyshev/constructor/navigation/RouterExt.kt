package ru.iandreyshev.constructor.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

internal val Fragment.router: IRouter
    get() = NavComponentRouter(findNavController())
