package ch.leadrian.samp.kamp.core.runtime.async

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

internal class ExecutorServiceFactory
@Inject
constructor() {

    fun createSingleThreadExecutor(): ExecutorService = Executors.newSingleThreadExecutor()

}