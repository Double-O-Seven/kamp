package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.exception.InvalidPlayerNameException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerNameChangeHandler
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class PlayerNamePropertyTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()
    private val onPlayerNameChangeHandler: OnPlayerNameChangeHandler = mockk()

    private lateinit var playerNameProperty: PlayerNameProperty

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerNameProperty = PlayerNameProperty(nativeFunctionExecutor, onPlayerNameChangeHandler)
        every { onPlayerNameChangeHandler.onPlayerNameChange(any(), any(), any()) } just Runs
    }

    @Test
    fun givenWasNotYetSetItShouldGetPlayerName() {
        every {
            nativeFunctionExecutor.getPlayerName(
                    playerId.value,
                    any(),
                    SAMPConstants.MAX_PLAYER_NAME
            )
        } answers {
            secondArg<ReferenceString>().value = "hans.wurst"
            0
        }

        val name = playerNameProperty.getValue(player, property)

        assertThat(name)
                .isEqualTo("hans.wurst")
    }

    @Test
    fun givenResultingNameIsNullItShouldThrowException() {
        every {
            nativeFunctionExecutor.getPlayerName(
                    playerId.value,
                    any(),
                    SAMPConstants.MAX_PLAYER_NAME
            )
        } returns 0

        val caughtThrowable = catchThrowable { playerNameProperty.getValue(player, property) }

        assertThat(caughtThrowable)
                .isInstanceOf(UninitializedPropertyAccessException::class.java)
    }

    @Test
    fun givenNameWasSetItShouldReturnCachedName() {
        every {
            nativeFunctionExecutor.getPlayerName(
                    playerId.value,
                    any(),
                    SAMPConstants.MAX_PLAYER_NAME
            )
        } answers {
            secondArg<ReferenceString>().value = "John.Sausage"
            0
        }
        every { nativeFunctionExecutor.setPlayerName(playerId.value, any()) } returns 0
        playerNameProperty.setValue(player, property, "hans.wurst")

        val name = playerNameProperty.getValue(player, property)

        assertThat(name)
                .isEqualTo("hans.wurst")
        verify(exactly = 1) { nativeFunctionExecutor.getPlayerName(any(), any(), any()) }
        verifyOrder {
            nativeFunctionExecutor.getPlayerName(any(), any(), any())
            nativeFunctionExecutor.setPlayerName(any(), any())
        }
    }

    @Test
    fun shouldSetPlayerName() {
        every {
            nativeFunctionExecutor.getPlayerName(
                    playerId.value,
                    any(),
                    SAMPConstants.MAX_PLAYER_NAME
            )
        } answers {
            secondArg<ReferenceString>().value = "John.Sausage"
            0
        }
        every { nativeFunctionExecutor.setPlayerName(any(), any()) } returns 0

        playerNameProperty.setValue(player, property, "hans.wurst")

        verify { nativeFunctionExecutor.setPlayerName(playerid = playerId.value, name = "hans.wurst") }
    }

    @Test
    fun shouldCallOnPlayerNameChangeHandler() {
        every { nativeFunctionExecutor.setPlayerName(any(), any()) } returns 0
        every {
            nativeFunctionExecutor.getPlayerName(
                    playerId.value,
                    any(),
                    SAMPConstants.MAX_PLAYER_NAME
            )
        } answers {
            secondArg<ReferenceString>().value = "hans.wurst"
            0
        }

        playerNameProperty.setValue(player, property, "John.Sausage")

        verify { onPlayerNameChangeHandler.onPlayerNameChange(player, "hans.wurst", "John.Sausage") }
    }

    @Test
    fun givenEmptyNameIsSetItShouldThrowAnException() {
        val caughtThrowable = catchThrowable { playerNameProperty.setValue(player, property, "") }

        assertThat(caughtThrowable)
                .isInstanceOf(InvalidPlayerNameException::class.java)
    }

    @Test
    fun givenInvalidNameIsSetItShouldThrowAnException() {
        every {
            nativeFunctionExecutor.getPlayerName(
                    playerId.value,
                    any(),
                    SAMPConstants.MAX_PLAYER_NAME
            )
        } answers {
            secondArg<ReferenceString>().value = "hans.wurst"
            0
        }
        every { nativeFunctionExecutor.setPlayerName(playerId.value, "???") } returns -1
        val caughtThrowable = catchThrowable { playerNameProperty.setValue(player, property, "???") }

        assertThat(caughtThrowable)
                .isInstanceOf(InvalidPlayerNameException::class.java)
    }

}