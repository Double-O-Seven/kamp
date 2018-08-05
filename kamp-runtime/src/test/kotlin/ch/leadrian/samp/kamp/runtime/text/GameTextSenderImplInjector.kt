package ch.leadrian.samp.kamp.runtime.text

import ch.leadrian.samp.kamp.api.text.TextFormatter
import ch.leadrian.samp.kamp.api.text.TextProvider
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.registry.PlayerRegistry
import io.mockk.mockk

internal object GameTextSenderImplInjector {

    fun inject(
            nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk(),
            textProvider: TextProvider = mockk(),
            playerRegistry: PlayerRegistry = mockk(),
            textFormatter: TextFormatter = mockk()
    ) = GameTextSenderImpl(
            nativeFunctionExecutor = nativeFunctionExecutor,
            textProvider = textProvider,
            playerRegistry = playerRegistry,
            textFormatter = textFormatter
    )

}