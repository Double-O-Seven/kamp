package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NPCServiceTest {

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private lateinit var npcService: NPCService

    @BeforeEach
    fun setUp() {
        npcService = NPCService(nativeFunctionExecutor)
    }

    @Test
    fun shouldCallNativeConnectNPCFunction() {
        val name = "Hans_Wurst"
        val script = "having_fun"
        every { nativeFunctionExecutor.connectNPC(any(), any()) } returns true

        npcService.connectNPC(name = name, script = script)

        verify { nativeFunctionExecutor.connectNPC(name = name, script = script) }
    }

}