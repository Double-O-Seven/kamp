package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.ExplosionType
import ch.leadrian.samp.kamp.core.api.constants.Weather
import ch.leadrian.samp.kamp.core.api.data.sphereOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class WorldServiceTest {

    private lateinit var worldService: WorldService

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        worldService = WorldService(nativeFunctionExecutor)
    }

    @Test
    fun shouldSetTime() {
        every { nativeFunctionExecutor.setWorldTime(any()) } returns true

        worldService.setTime(13)

        verify { nativeFunctionExecutor.setWorldTime(13) }
    }

    @Nested
    inner class SetWeatherTests {

        @Test
        fun shouldSetWeather() {
            every { nativeFunctionExecutor.setWeather(any()) } returns true

            worldService.setWeather(69)

            verify { nativeFunctionExecutor.setWeather(69) }
        }

        @Test
        fun shouldSetWeatherWithEnum() {
            every { nativeFunctionExecutor.setWeather(any()) } returns true

            worldService.setWeather(Weather.CLOUDY_VEGAS)

            verify { nativeFunctionExecutor.setWeather(Weather.CLOUDY_VEGAS.value) }
        }

    }

    @Test
    fun shouldSetGravity() {
        every { nativeFunctionExecutor.setGravity(any()) } returns true

        worldService.setGravity(0.08f)

        verify { nativeFunctionExecutor.setGravity(0.08f) }
    }

    @Test
    fun shouldReturnGravity() {
        every { nativeFunctionExecutor.getGravity() } returns 13.37f

        val gravity = worldService.getGravity()

        assertThat(gravity)
                .isEqualTo(gravity)
    }

    @Nested
    inner class CreateExplosionTests {

        @Test
        fun givenSphereItShouldCreateExplosion() {
            every { nativeFunctionExecutor.createExplosion(any(), any(), any(), any(), any()) } returns true

            worldService.createExplosion(ExplosionType.NORMAL_3, sphereOf(x = 1f, y = 2f, z = 3f, radius = 4f))

            verify {
                nativeFunctionExecutor.createExplosion(x = 1f, y = 2f, z = 3f, type = ExplosionType.NORMAL_3.value, radius = 4f)
            }
        }

        @Test
        fun givenCoordinatesAndRadiusItShouldCreateExplosion() {
            every { nativeFunctionExecutor.createExplosion(any(), any(), any(), any(), any()) } returns true

            worldService.createExplosion(ExplosionType.NORMAL_3, vector3DOf(x = 1f, y = 2f, z = 3f), radius = 4f)

            verify {
                nativeFunctionExecutor.createExplosion(x = 1f, y = 2f, z = 3f, type = ExplosionType.NORMAL_3.value, radius = 4f)
            }
        }

    }

    @Test
    fun shouldDisableInteriorEnterExists() {
        every { nativeFunctionExecutor.disableInteriorEnterExits() } returns true

        worldService.disableInteriorEnterExits()

        verify { nativeFunctionExecutor.disableInteriorEnterExits() }
    }

}