package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PickupRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PickupFactoryTest {

    private lateinit var pickupFactory: PickupFactory

    private val pickupRegistry = mockk<PickupRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { nativeFunctionExecutor.createPickup(any(), any(), any(), any(), any(), any()) } returns 0
        every { pickupRegistry.register(any()) } just Runs
        every { pickupRegistry.unregister(any()) } just Runs
        pickupFactory = PickupFactory(pickupRegistry, nativeFunctionExecutor)
    }

    @Test
    fun shouldCreatePickup() {
        pickupFactory.create(
                modelId = 1337,
                type = 69,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                virtualWorldId = 10
        )

        verify {
            nativeFunctionExecutor.createPickup(
                    model = 1337,
                    type = 69,
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    virtualworld = 10
            )
        }
    }

    @Test
    fun shouldRegisterPickup() {
        val pickup = pickupFactory.create(
                modelId = 1337,
                type = 69,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                virtualWorldId = 10
        )

        verify { pickupRegistry.register(pickup) }
    }

    @Test
    fun shouldUnregisterPickupOnDestroy() {
        every { nativeFunctionExecutor.destroyPickup(any()) } returns true
        val pickup = pickupFactory.create(
                modelId = 1337,
                type = 69,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                virtualWorldId = 10
        )
        val onDestroy = mockk<Pickup.() -> Unit>(relaxed = true)
        pickup.onDestroy(onDestroy)

        pickup.destroy()

        verify { onDestroy.invoke(pickup) }
    }

}