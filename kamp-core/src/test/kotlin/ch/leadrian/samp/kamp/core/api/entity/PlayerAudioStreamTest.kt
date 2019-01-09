package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.sphereOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerAudioStreamTest {

    private lateinit var playerAudioStream: PlayerAudioStream

    private val playerId = PlayerId.valueOf(69)
    private val player = mockk<Player>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerAudioStream = PlayerAudioStream(player, nativeFunctionExecutor)
    }

    @Nested
    inner class PlayAudioStreamTests {

        @Test
        fun shouldPlayAudioStream() {
            every {
                nativeFunctionExecutor.playAudioStreamForPlayer(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } returns true

            playerAudioStream.play(
                    url = "http://localhost:8080/song/1",
                    position = sphereOf(x = 1f, y = 2f, z = 3f, radius = 4f),
                    usePosition = true
            )

            verify {
                nativeFunctionExecutor.playAudioStreamForPlayer(
                        playerid = playerId.value,
                        url = "http://localhost:8080/song/1",
                        posX = 1f,
                        posY = 2f,
                        posZ = 3f,
                        distance = 4f,
                        usepos = true
                )
            }
        }

        @Test
        fun shouldPlayAudioStreamWithoutPosition() {
            every {
                nativeFunctionExecutor.playAudioStreamForPlayer(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } returns true

            playerAudioStream.play("http://localhost:8080/song/1")

            verify {
                nativeFunctionExecutor.playAudioStreamForPlayer(
                        playerid = playerId.value,
                        url = "http://localhost:8080/song/1",
                        posX = 0f,
                        posY = 0f,
                        posZ = 0f,
                        distance = 0f,
                        usepos = false
                )
            }
        }
    }

    @Test
    fun shouldStopAudioStream() {
        every { nativeFunctionExecutor.stopAudioStreamForPlayer(any()) } returns true

        playerAudioStream.stop()

        verify { nativeFunctionExecutor.stopAudioStreamForPlayer(playerId.value) }
    }

}