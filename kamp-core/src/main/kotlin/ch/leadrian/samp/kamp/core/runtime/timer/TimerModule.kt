package ch.leadrian.samp.kamp.core.runtime.timer

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor

internal class TimerModule : KampModule() {

    override fun configure() {
        bind(TimerExecutor::class.java).to(TimerExecutorImpl::class.java)
    }
}