package ch.leadrian.samp.kamp.core.runtime

import com.google.inject.AbstractModule
import com.netflix.governator.lifecycle.LifecycleManager
import io.mockk.every
import io.mockk.mockk

internal class TestModule(private val maxPlayers: Int = 50) : AbstractModule() {

    override fun configure() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { getMaxPlayers() } returns maxPlayers
        }
        bind(SAMPNativeFunctionExecutor::class.java).toInstance(nativeFunctionExecutor)
        bind(LifecycleManager::class.java).toInstance(mockk())
        bind(Server::class.java).toInstance(mockk())
    }

}