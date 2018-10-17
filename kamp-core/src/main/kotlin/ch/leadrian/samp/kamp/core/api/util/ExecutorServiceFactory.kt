package ch.leadrian.samp.kamp.core.api.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class ExecutorServiceFactory
@Inject
constructor() {

    fun createSingleThreadExecutor(): ExecutorService = Executors.newSingleThreadExecutor()

    fun createSingleThreadScheduledExecutor() = Executors.newSingleThreadScheduledExecutor()

}