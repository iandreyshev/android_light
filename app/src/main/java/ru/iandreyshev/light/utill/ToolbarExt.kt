package ru.iandreyshev.light.utill

import androidx.appcompat.widget.Toolbar

fun Toolbar.withItemListeners(buildAction: ItemClickListenersBuilder.() -> Unit) {
    val builder = ItemClickListenersBuilder()
    builder.apply(buildAction)

    setOnMenuItemClickListener { item ->
        when (val action = builder.actions[item.itemId]) {
            null -> false
            else -> {
                action()
                true
            }
        }
    }
}

class ItemClickListenersBuilder {

    val actions: Map<Int, () -> Unit>
        get() = mActions

    private val mActions = mutableMapOf<Int, () -> Unit>()

    infix operator fun Int.invoke(action: () -> Unit) {
        mActions[this] = action
    }

}