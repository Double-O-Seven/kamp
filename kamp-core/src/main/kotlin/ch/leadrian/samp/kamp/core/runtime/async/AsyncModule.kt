package ch.leadrian.samp.kamp.core.runtime.async

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.core.api.util.ExecutorServiceFactory

internal class AsyncModule : KampModule() {

    override fun configure() {
        bind(ExecutorServiceFactory::class.java)
        bind(AsyncExecutor::class.java).to(AsyncExecutorImpl::class.java)
    }

}