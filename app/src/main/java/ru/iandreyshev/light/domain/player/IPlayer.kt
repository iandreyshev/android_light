package ru.iandreyshev.light.domain.player

import io.reactivex.Observable

interface IPlayer {
    fun prepare(): Observable<LoadResult>
    fun playWhenReady()
    fun nextItem()
    fun previousItem()
    fun reset()
}
