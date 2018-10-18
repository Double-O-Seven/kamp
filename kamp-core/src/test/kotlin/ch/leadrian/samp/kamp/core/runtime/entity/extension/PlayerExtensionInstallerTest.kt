package ch.leadrian.samp.kamp.core.runtime.entity.extension

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionFactory
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class PlayerExtensionInstallerTest {

    @Test
    fun shouldRegisterOnInitialize() {
        val callbackListenerManager = mockk<CallbackListenerManager>()
        every { callbackListenerManager.register(any()) } just Runs
        val playerExtensionInstaller = PlayerExtensionInstaller(emptySet(), callbackListenerManager)

        playerExtensionInstaller.initialize()

        verify { callbackListenerManager.register(playerExtensionInstaller) }
    }

    @Test
    fun shouldInstallAllFactories() {
        val fooEntityExtensionFactory = mockk<EntityExtensionFactory<Player, FooExtension>>()
        val barEntityExtensionFactory = mockk<EntityExtensionFactory<Player, BarExtension>>()
        val playerExtensionContainer = mockk<EntityExtensionContainer<Player>> {
            every { install<Any>(any()) } just Runs
        }
        val player = mockk<Player> {
            every { this@mockk.extensions } returns playerExtensionContainer
        }
        val callbackListenerManager = mockk<CallbackListenerManager>()
        val playerExtensionInstaller = PlayerExtensionInstaller(
                setOf(fooEntityExtensionFactory, barEntityExtensionFactory),
                callbackListenerManager
        )

        playerExtensionInstaller.onPlayerConnect(player)

        verify {
            playerExtensionContainer.install(fooEntityExtensionFactory)
            playerExtensionContainer.install(barEntityExtensionFactory)
        }
    }

    private class FooExtension

    private class BarExtension

}