package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.MutableVector3D
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.PickupId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerPickUpPickupReceiverDelegate
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PickupTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructPickup() {
            val pickupId = PickupId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createPickup(
                            model = 1024,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            type = 13,
                            virtualworld = 187
                    )
                } returns pickupId.value
            }

            val pickup = Pickup(
                    modelId = 1024,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    type = 13,
                    virtualWorldId = 187,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            assertThat(pickup.id)
                    .isEqualTo(pickupId)
        }

        @Test
        fun givenNoVirtualWorldItShouldConstructPickup() {
            val pickupId = PickupId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createPickup(
                            model = 1024,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            type = 13,
                            virtualworld = -1
                    )
                } returns pickupId.value
            }

            val pickup = Pickup(
                    modelId = 1024,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    type = 13,
                    virtualWorldId = null,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            assertThat(pickup.id)
                    .isEqualTo(pickupId)
        }

        @Test
        fun givenCreatePickupReturnsInvalidPickupIdItShouldThrowCreationFailedException() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createPickup(
                            model = 1024,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            type = 13,
                            virtualworld = 187
                    )
                } returns PickupId.INVALID.value
            }

            val caughtThrowable = catchThrowable {
                Pickup(
                        modelId = 1024,
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        type = 13,
                        virtualWorldId = 187,
                        nativeFunctionExecutor = nativeFunctionExecutor
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val pickupId = PickupId.valueOf(123)
        private lateinit var pickup: Pickup
        private val onPlayerPickUpPickupReceiver = mockk<OnPlayerPickUpPickupReceiverDelegate>()

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

        @BeforeEach
        fun setUp() {
            every {
                nativeFunctionExecutor.createPickup(any(), any(), any(), any(), any(), any())
            } returns pickupId.value
            pickup = Pickup(
                    modelId = 1024,
                    coordinates = mutableVector3DOf(x = 1f, y = 2f, z = 3f),
                    type = 13,
                    virtualWorldId = 187,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    onPlayerPickUpPickupReceiver = onPlayerPickUpPickupReceiver
            )
        }

        @Test
        fun shouldGetCoordinates() {
            val coordinates = pickup.coordinates

            assertThat(coordinates)
                    .isNotInstanceOf(MutableVector3D::class.java)
                    .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
        }

        @Test
        fun shouldCallOnPlayerPickUpPickupReceiverDelegate() {
            val player = mockk<Player>()
            every { onPlayerPickUpPickupReceiver.onPlayerPickUpPickup(any(), any()) } just Runs

            pickup.onPickUp(player)

            verify { onPlayerPickUpPickupReceiver.onPlayerPickUpPickup(player, pickup) }
        }

        @Nested
        inner class DestroyTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.destroyPickup(any()) } returns true
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = pickup.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun shouldDestroyPickup() {
                val onDestroy = mockk<Pickup.() -> Unit>(relaxed = true)
                pickup.onDestroy(onDestroy)

                pickup.destroy()

                verifyOrder {
                    onDestroy.invoke(pickup)
                    nativeFunctionExecutor.destroyPickup(pickupId.value)
                }
                assertThat(pickup.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                val onDestroy = mockk<Pickup.() -> Unit>(relaxed = true)
                pickup.onDestroy(onDestroy)

                pickup.destroy()
                pickup.destroy()

                verify(exactly = 1) {
                    onDestroy.invoke(pickup)
                    nativeFunctionExecutor.destroyPickup(pickupId.value)
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                pickup.destroy()

                val caughtThrowable = catchThrowable { pickup.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }
}