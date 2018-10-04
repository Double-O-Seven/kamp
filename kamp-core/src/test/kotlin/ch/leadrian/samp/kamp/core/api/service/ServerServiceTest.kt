package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Animation
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ServerServiceTest {

    private lateinit var serverService: ServerService

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        serverService = ServerService(nativeFunctionExecutor)
    }

    @Test
    fun shouldSetGameModeText() {
        every { nativeFunctionExecutor.setGameModeText(any()) } returns true

        serverService.setGameModeText("LVDM")

        verify { nativeFunctionExecutor.setGameModeText("LVDM") }
    }

    @Test
    fun shouldSetTeamCount() {
        every { nativeFunctionExecutor.setTeamCount(any()) } returns true

        serverService.setTeamCount(69)

        verify { nativeFunctionExecutor.setTeamCount(69) }
    }

    @Test
    fun shouldGetServerVars() {
        val serverVars = serverService.getServerVars()

        assertThat(serverVars)
                .isNotNull
    }

    @Test
    fun shouldGetConsoleVars() {
        val consoleVars = serverService.getConsoleVars()

        assertThat(consoleVars)
                .isNotNull
    }

    @Test
    fun shouldTriggerGameModeExit() {
        every { nativeFunctionExecutor.gameModeExit() } returns true

        serverService.triggerGameModeExit()

        verify { nativeFunctionExecutor.gameModeExit() }
    }

    @Test
    fun shouldSendRconCommand() {
        every { nativeFunctionExecutor.sendRconCommand(any()) } returns true

        serverService.sendRconCommand("ban 69")

        verify { nativeFunctionExecutor.sendRconCommand("ban 69") }
    }

    @Nested
    inner class GetNetworkStatisticsTests {

        @Test
        fun shouldReturnNetworkStatistics() {
            every { nativeFunctionExecutor.getNetworkStats(any(), 32) } answers {
                firstArg<ReferenceString>().value = "Ping: 69"
                true
            }

            val networkStatistics = serverService.getNetworkStatistics(resultLength = 32)

            assertThat(networkStatistics)
                    .isEqualTo("Ping: 69")
        }

        @Test
        fun givenReferenceStringValueIsNotSetItShouldReturnEmptyString() {
            every { nativeFunctionExecutor.getNetworkStats(any(), any()) } returns true

            val networkStatistics = serverService.getNetworkStatistics(resultLength = 32)

            assertThat(networkStatistics)
                    .isEmpty()
        }

    }

    @Test
    fun shouldBlockIpAddress() {
        every { nativeFunctionExecutor.blockIpAddress(any(), any()) } returns true

        serverService.blockIpAddress("127.0.0.1", 60000)

        verify { nativeFunctionExecutor.blockIpAddress("127.0.0.1", 60000) }
    }

    @Test
    fun shouldUnblockIpAddress() {
        every { nativeFunctionExecutor.unBlockIpAddress(any()) } returns true

        serverService.unblockIpAddress("127.0.0.1")

        verify { nativeFunctionExecutor.unBlockIpAddress("127.0.0.1") }
    }

    @Nested
    inner class GetAnimationNameTests {

        @Test
        fun shouldReturnAnimation() {
            every {
                nativeFunctionExecutor.getAnimationName(1337, any(), 32, any(), 32)
            } answers {
                secondArg<ReferenceString>().value = "TEST_LIB"
                arg<ReferenceString>(3).value = "test_anim"
                true
            }

            val animation = serverService.getAnimationName(1337)

            assertThat(animation)
                    .isEqualTo(Animation.valueOf(library = "TEST_LIB", name = "test_anim"))
        }

        @Test
        fun givenNoReferenceStringValueIsSetItShouldThrowException() {
            every {
                nativeFunctionExecutor.getAnimationName(any(), any(), any(), any(), any())
            } returns true

            val caughtThrowable = catchThrowable { serverService.getAnimationName(1337) }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid animation index: 1337")
        }

        @Test
        fun givenLibraryNameIsNotSetItShouldThrowException() {
            every {
                nativeFunctionExecutor.getAnimationName(1337, any(), 32, any(), 32)
            } answers {
                arg<ReferenceString>(3).value = "test_anim"
                true
            }

            val caughtThrowable = catchThrowable { serverService.getAnimationName(1337) }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid animation index: 1337")
        }

        @Test
        fun givenAnimationNameIsNotSetItShouldThrowException() {
            every {
                nativeFunctionExecutor.getAnimationName(1337, any(), 32, any(), 32)
            } answers {
                secondArg<ReferenceString>().value = "TEST_LIB"
                true
            }

            val caughtThrowable = catchThrowable { serverService.getAnimationName(1337) }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid animation index: 1337")
        }

    }

}