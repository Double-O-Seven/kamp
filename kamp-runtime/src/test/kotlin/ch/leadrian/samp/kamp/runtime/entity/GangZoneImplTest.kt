package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.data.Colors
import ch.leadrian.samp.kamp.api.data.mutableRectangleOf
import ch.leadrian.samp.kamp.api.data.rectangleOf
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.id.GangZoneId
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.registry.GangZoneRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GangZoneImplTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructGangZone() {
            val gangZoneId = GangZoneId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gangZoneCreate(minx = 1f, maxx = 2f, miny = 3f, maxy = 4f) } returns gangZoneId.value
            }

            val gangZone = GangZoneImpl(
                    area = rectangleOf(minX = 1f, maxX = 2f, minY = 3f, maxY = 4f),
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    gangZoneRegistry = mockk()
            )

            assertThat(gangZone.id)
                    .isEqualTo(gangZoneId)
        }

        @Test
        fun givenCreateGangZoneReturnsInvalidGangZoneIdItShouldThrowCreationFailedException() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gangZoneCreate(minx = 1f, maxx = 2f, miny = 3f, maxy = 4f) } returns SAMPConstants.INVALID_GANG_ZONE
            }

            val caughtThrowable = catchThrowable {
                GangZoneImpl(
                        area = rectangleOf(minX = 1f, maxX = 2f, minY = 3f, maxY = 4f),
                        nativeFunctionExecutor = nativeFunctionExecutor,
                        gangZoneRegistry = mockk()
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val playerId = PlayerId.valueOf(50)
        private val player = mockk<Player> {
            every { id } returns playerId
        }

        private val gangZoneId = GangZoneId.valueOf(10)
        private lateinit var gangZone: GangZoneImpl

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
        private val gangZoneRegistry = mockk<GangZoneRegistry>()

        @BeforeEach
        fun setUp() {
            every { nativeFunctionExecutor.gangZoneCreate(any(), any(), any(), any()) } returns gangZoneId.value
            gangZone = GangZoneImpl(
                    area = mutableRectangleOf(minX = 1f, maxX = 2f, minY = 3f, maxY = 4f),
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    gangZoneRegistry = gangZoneRegistry
            )
        }

        @Test
        fun shouldSetArea() {
            val area = gangZone.area

            assertThat(area)
                    .isEqualTo(rectangleOf(minX = 1f, maxX = 2f, minY = 3f, maxY = 4f))
        }

        @Test
        fun shouldShowForPlayer() {
            every { nativeFunctionExecutor.gangZoneShowForPlayer(any(), any(), any()) } returns true

            gangZone.show(player, Colors.RED)

            verify {
                nativeFunctionExecutor.gangZoneShowForPlayer(
                        playerid = playerId.value,
                        zone = gangZoneId.value,
                        color = Colors.RED.value
                )
            }
        }

        @Test
        fun shouldShowForAll() {
            every { nativeFunctionExecutor.gangZoneShowForAll(any(), any()) } returns true

            gangZone.showForAll(Colors.RED)

            verify {
                nativeFunctionExecutor.gangZoneShowForAll(
                        zone = gangZoneId.value,
                        color = Colors.RED.value
                )
            }
        }

        @Test
        fun shouldHideForPlayer() {
            every { nativeFunctionExecutor.gangZoneHideForPlayer(any(), any()) } returns true

            gangZone.hide(player)

            verify {
                nativeFunctionExecutor.gangZoneHideForPlayer(
                        playerid = playerId.value,
                        zone = gangZoneId.value
                )
            }
        }

        @Test
        fun shouldHideForAll() {
            every { nativeFunctionExecutor.gangZoneHideForAll(any()) } returns true

            gangZone.hideForAll()

            verify { nativeFunctionExecutor.gangZoneHideForAll(gangZoneId.value) }
        }

        @Test
        fun shouldFlashForPlayer() {
            every { nativeFunctionExecutor.gangZoneFlashForPlayer(any(), any(), any()) } returns true

            gangZone.flash(player, Colors.RED)

            verify {
                nativeFunctionExecutor.gangZoneFlashForPlayer(
                        playerid = playerId.value,
                        zone = gangZoneId.value,
                        flashcolor = Colors.RED.value
                )
            }
        }

        @Test
        fun shouldFlashForAll() {
            every { nativeFunctionExecutor.gangZoneFlashForAll(any(), any()) } returns true

            gangZone.flashForAll(Colors.RED)

            verify {
                nativeFunctionExecutor.gangZoneFlashForAll(
                        zone = gangZoneId.value,
                        flashcolor = Colors.RED.value
                )
            }
        }

        @Test
        fun shouldStopFlashForPlayer() {
            every { nativeFunctionExecutor.gangZoneStopFlashForPlayer(any(), any()) } returns true

            gangZone.stopFlash(player)

            verify {
                nativeFunctionExecutor.gangZoneStopFlashForPlayer(
                        playerid = playerId.value,
                        zone = gangZoneId.value
                )
            }
        }

        @Test
        fun shouldStopFlashForAll() {
            every { nativeFunctionExecutor.gangZoneStopFlashForAll(any()) } returns true

            gangZone.stopFlashForAll()

            verify { nativeFunctionExecutor.gangZoneStopFlashForAll(gangZoneId.value) }
        }

        @Nested
        inner class DestroyTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.gangZoneDestroy(any()) } returns true
                every { gangZoneRegistry.unregister(gangZone) } just Runs
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = gangZone.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun shouldDestroyGangZone() {

                gangZone.destroy()

                verify {
                    nativeFunctionExecutor.gangZoneDestroy(gangZoneId.value)
                    gangZoneRegistry.unregister(gangZone)
                }
                assertThat(gangZone.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                every { nativeFunctionExecutor.gangZoneDestroy(any()) } returns true

                gangZone.destroy()
                gangZone.destroy()

                verify(exactly = 1) {
                    nativeFunctionExecutor.gangZoneDestroy(gangZoneId.value)
                    gangZoneRegistry.unregister(gangZone)
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                every { nativeFunctionExecutor.gangZoneDestroy(any()) } returns true
                gangZone.destroy()

                val caughtThrowable = catchThrowable { gangZone.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }

}
