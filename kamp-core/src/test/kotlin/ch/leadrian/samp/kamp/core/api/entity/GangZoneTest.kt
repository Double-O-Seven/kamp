package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.mutableRectangleOf
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.entity.id.GangZoneId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GangZoneTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructGangZone() {
            val gangZoneId = GangZoneId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { gangZoneCreate(minx = 1f, maxx = 2f, miny = 3f, maxy = 4f) } returns gangZoneId.value
            }

            val gangZone = GangZone(
                    area = rectangleOf(minX = 1f, maxX = 2f, minY = 3f, maxY = 4f),
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            assertThat(gangZone.id)
                    .isEqualTo(gangZoneId)
        }

        @Test
        fun givenCreateGangZoneReturnsInvalidGangZoneIdItShouldThrowCreationFailedException() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    gangZoneCreate(
                            minx = 1f,
                            maxx = 2f,
                            miny = 3f,
                            maxy = 4f
                    )
                } returns SAMPConstants.INVALID_GANG_ZONE
            }

            val caughtThrowable = catchThrowable {
                GangZone(
                        area = rectangleOf(minX = 1f, maxX = 2f, minY = 3f, maxY = 4f),
                        nativeFunctionExecutor = nativeFunctionExecutor
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
        private lateinit var gangZone: GangZone

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

        @BeforeEach
        fun setUp() {
            every { nativeFunctionExecutor.gangZoneCreate(any(), any(), any(), any()) } returns gangZoneId.value
            gangZone = GangZone(
                    area = mutableRectangleOf(minX = 1f, maxX = 2f, minY = 3f, maxY = 4f),
                    nativeFunctionExecutor = nativeFunctionExecutor
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
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = gangZone.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun shouldDestroyGangZone() {
                val onDestroy = mockk<GangZone.() -> Unit>(relaxed = true)
                gangZone.onDestroy(onDestroy)

                gangZone.destroy()

                verifyOrder {
                    nativeFunctionExecutor.gangZoneDestroy(gangZoneId.value)
                    onDestroy.invoke(gangZone)
                }
                assertThat(gangZone.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                val onDestroy = mockk<GangZone.() -> Unit>(relaxed = true)
                gangZone.onDestroy(onDestroy)

                gangZone.destroy()
                gangZone.destroy()

                verify(exactly = 1) {
                    onDestroy.invoke(gangZone)
                    nativeFunctionExecutor.gangZoneDestroy(gangZoneId.value)
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                gangZone.destroy()

                val caughtThrowable = catchThrowable { gangZone.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }

}
