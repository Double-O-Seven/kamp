package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.GangZoneRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GangZoneFactoryTest {

    private lateinit var gangZoneFactory: GangZoneFactory

    private val gangZoneRegistry = mockk<GangZoneRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { nativeFunctionExecutor.gangZoneCreate(any(), any(), any(), any()) } returns 0
        every { gangZoneRegistry.register(any()) } just Runs
        gangZoneFactory = GangZoneFactory(gangZoneRegistry, nativeFunctionExecutor)
    }

    @Test
    fun shouldCreateGangZone() {
        gangZoneFactory.create(rectangleOf(minX = 1f, maxX = 2f, minY = 3f, maxY = 4f))

        verify { nativeFunctionExecutor.gangZoneCreate(minx = 1f, maxx = 2f, miny = 3f, maxy = 4f) }
    }

    @Test
    fun shouldRegisterGangZone() {
        val gangZone = gangZoneFactory.create(rectangleOf(minX = 1f, maxX = 2f, minY = 3f, maxY = 4f))

        verify { gangZoneRegistry.register(gangZone) }
    }

    @Test
    fun shouldUnregisterGangZoneOnDestroy() {
        every { gangZoneRegistry.unregister(any()) } just Runs
        every { nativeFunctionExecutor.gangZoneDestroy(any()) } returns true
        val gangZone = gangZoneFactory.create(rectangleOf(minX = 1f, maxX = 2f, minY = 3f, maxY = 4f))

        gangZone.destroy()

        verify { gangZoneRegistry.unregister(gangZone) }
    }

}