package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.api.constants.MapIconType
import ch.leadrian.samp.kamp.api.data.Colors
import ch.leadrian.samp.kamp.api.data.MutableColor
import ch.leadrian.samp.kamp.api.data.MutableVector3D
import ch.leadrian.samp.kamp.api.data.colorOf
import ch.leadrian.samp.kamp.api.data.mutableColorOf
import ch.leadrian.samp.kamp.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.api.data.vector3DOf
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerMapIconImplTest {

    @Test
    fun shouldSetMapIconWhenMapIconIsCreated() {
        val player = mockk<PlayerImpl> {
            every { id } returns PlayerId.valueOf(99)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any()) } returns true
        }

        val playerMapIcon = PlayerMapIconImpl(
                player = player,
                id = PlayerMapIconId.valueOf(13),
                type = MapIconType.BALLAS,
                color = Colors.RED,
                style = MapIconStyle.GLOBAL,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        verify {
            nativeFunctionExecutor.setPlayerMapIcon(
                    playerid = 99,
                    style = MapIconStyle.GLOBAL.value,
                    color = Colors.RED.value,
                    markertype = MapIconType.BALLAS.value,
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    iconid = 13
            )
        }
        assertThat(playerMapIcon.isDestroyed)
                .isFalse()
    }

    @Nested
    inner class CoordinatesTests {

        @Test
        fun settingCoordinatesShouldUpdateMapIcon() {
            val player = mockk<PlayerImpl> {
                every { id } returns PlayerId.valueOf(99)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any()) } returns true
            }
            val playerMapIcon = PlayerMapIconImpl(
                    player = player,
                    id = PlayerMapIconId.valueOf(13),
                    type = MapIconType.BALLAS,
                    color = Colors.RED,
                    style = MapIconStyle.GLOBAL,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            playerMapIcon.coordinates = mutableVector3DOf(x = 4f, y = 5f, z = 6f)

            verify {
                nativeFunctionExecutor.setPlayerMapIcon(
                        playerid = 99,
                        style = MapIconStyle.GLOBAL.value,
                        color = Colors.RED.value,
                        markertype = MapIconType.BALLAS.value,
                        x = 4f,
                        y = 5f,
                        z = 6f,
                        iconid = 13
                )
            }
        }

        @Test
        fun settingCoordinatesShouldStoreImmutableVector3D() {
            val player = mockk<PlayerImpl> {
                every { id } returns PlayerId.valueOf(99)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any()) } returns true
            }
            val playerMapIcon = PlayerMapIconImpl(
                    player = player,
                    id = PlayerMapIconId.valueOf(13),
                    type = MapIconType.BALLAS,
                    color = Colors.RED,
                    style = MapIconStyle.GLOBAL,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            playerMapIcon.coordinates = mutableVector3DOf(x = 4f, y = 5f, z = 6f)

            assertThat(playerMapIcon.coordinates)
                    .isNotInstanceOf(MutableVector3D::class.java)
                    .isEqualTo(vector3DOf(x = 4f, y = 5f, z = 6f))
        }
    }

    @Test
    fun settingTypeShouldUpdateMapIcon() {
        val player = mockk<PlayerImpl> {
            every { id } returns PlayerId.valueOf(99)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any()) } returns true
        }
        val playerMapIcon = PlayerMapIconImpl(
                player = player,
                id = PlayerMapIconId.valueOf(13),
                type = MapIconType.BALLAS,
                color = Colors.RED,
                style = MapIconStyle.GLOBAL,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        playerMapIcon.type = MapIconType.AIR_YARD

        verify {
            nativeFunctionExecutor.setPlayerMapIcon(
                    playerid = 99,
                    style = MapIconStyle.GLOBAL.value,
                    color = Colors.RED.value,
                    markertype = MapIconType.AIR_YARD.value,
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    iconid = 13
            )
        }
    }

    @Nested
    inner class ColorTests {

        @Test
        fun settingColorShouldUpdateMapIcon() {
            val player = mockk<PlayerImpl> {
                every { id } returns PlayerId.valueOf(99)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any()) } returns true
            }
            val playerMapIcon = PlayerMapIconImpl(
                    player = player,
                    id = PlayerMapIconId.valueOf(13),
                    type = MapIconType.BALLAS,
                    color = Colors.RED,
                    style = MapIconStyle.GLOBAL,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            playerMapIcon.color = mutableColorOf(0x11BBCCFF)

            verify {
                nativeFunctionExecutor.setPlayerMapIcon(
                        playerid = 99,
                        style = MapIconStyle.GLOBAL.value,
                        color = 0x11BBCCFF,
                        markertype = MapIconType.BALLAS.value,
                        x = 1f,
                        y = 2f,
                        z = 3f,
                        iconid = 13
                )
            }
        }

        @Test
        fun settingColorShouldStoreImmutableColor() {
            val player = mockk<PlayerImpl> {
                every { id } returns PlayerId.valueOf(99)
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any()) } returns true
            }
            val playerMapIcon = PlayerMapIconImpl(
                    player = player,
                    id = PlayerMapIconId.valueOf(13),
                    type = MapIconType.BALLAS,
                    color = Colors.RED,
                    style = MapIconStyle.GLOBAL,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            playerMapIcon.color = mutableColorOf(0x11BBCCFF)

            assertThat(playerMapIcon.color)
                    .isNotInstanceOf(MutableColor::class.java)
                    .isEqualTo(colorOf(0x11BBCCFF))
        }
    }

    @Test
    fun settingStyleShouldUpdateMapIcon() {
        val player = mockk<PlayerImpl> {
            every { id } returns PlayerId.valueOf(99)
        }
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any()) } returns true
        }
        val playerMapIcon = PlayerMapIconImpl(
                player = player,
                id = PlayerMapIconId.valueOf(13),
                type = MapIconType.BALLAS,
                color = Colors.RED,
                style = MapIconStyle.GLOBAL,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        playerMapIcon.style = MapIconStyle.LOCAL_CHECKPOINT

        verify {
            nativeFunctionExecutor.setPlayerMapIcon(
                    playerid = 99,
                    style = MapIconStyle.LOCAL_CHECKPOINT.value,
                    color = Colors.RED.value,
                    markertype = MapIconType.BALLAS.value,
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    iconid = 13
            )
        }
    }

    @Nested
    inner class DestroyTests {

        @Test
        fun givenPlayerIsNotOnlineItShouldDestroyPlayerMapIcon() {
            val player = mockk<PlayerImpl> {
                every { id } returns PlayerId.valueOf(99)
                every { isOnline } returns false
                every { unregisterMapIcon(any()) } just Runs
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any()) } returns true
            }
            val playerMapIcon = PlayerMapIconImpl(
                    player = player,
                    id = PlayerMapIconId.valueOf(13),
                    type = MapIconType.BALLAS,
                    color = Colors.RED,
                    style = MapIconStyle.GLOBAL,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            playerMapIcon.destroy()

            assertThat(playerMapIcon.isDestroyed)
                    .isTrue()
            verify { player.unregisterMapIcon(playerMapIcon) }
            verify(exactly = 0) { nativeFunctionExecutor.removePlayerMapIcon(any(), any()) }
        }

        @Test
        fun givenPlayerIsOnlineItShouldDestroyPlayerMapIcon() {
            val player = mockk<PlayerImpl> {
                every { id } returns PlayerId.valueOf(99)
                every { isOnline } returns true
                every { unregisterMapIcon(any()) } just Runs
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any()) } returns true
                every { removePlayerMapIcon(any(), any()) } returns true
            }
            val playerMapIcon = PlayerMapIconImpl(
                    player = player,
                    id = PlayerMapIconId.valueOf(13),
                    type = MapIconType.BALLAS,
                    color = Colors.RED,
                    style = MapIconStyle.GLOBAL,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            playerMapIcon.destroy()

            assertThat(playerMapIcon.isDestroyed)
                    .isTrue()
            verify { player.unregisterMapIcon(playerMapIcon) }
            verify(exactly = 1) { nativeFunctionExecutor.removePlayerMapIcon(playerid = 99, iconid = 13) }
        }

        @Test
        fun shouldNotExecuteTwice() {
            val player = mockk<PlayerImpl> {
                every { id } returns PlayerId.valueOf(99)
                every { isOnline } returns true
                every { unregisterMapIcon(any()) } just Runs
            }
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { setPlayerMapIcon(any(), any(), any(), any(), any(), any(), any(), any()) } returns true
                every { removePlayerMapIcon(any(), any()) } returns true
            }
            val playerMapIcon = PlayerMapIconImpl(
                    player = player,
                    id = PlayerMapIconId.valueOf(13),
                    type = MapIconType.BALLAS,
                    color = Colors.RED,
                    style = MapIconStyle.GLOBAL,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            playerMapIcon.destroy()
            playerMapIcon.destroy()

            verify(exactly = 1) {
                player.unregisterMapIcon(playerMapIcon)
                nativeFunctionExecutor.removePlayerMapIcon(any(), any())
            }
        }
    }

}