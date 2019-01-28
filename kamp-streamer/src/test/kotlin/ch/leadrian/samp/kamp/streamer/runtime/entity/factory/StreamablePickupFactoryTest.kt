package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.service.PickupService
import ch.leadrian.samp.kamp.streamer.runtime.PickupStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerPickUpStreamablePickupHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamOutHandler
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class StreamablePickupFactoryTest {

    private lateinit var streamablePickupFactory: StreamablePickupFactory

    private val pickupStreamer: PickupStreamer = mockk()
    private val pickupService: PickupService = mockk()
    private val onStreamablePickupStreamInHandler: OnStreamablePickupStreamInHandler = mockk()
    private val onStreamablePickupStreamOutHandler: OnStreamablePickupStreamOutHandler = mockk()
    private val onPlayerPickUpStreamablePickupHandler: OnPlayerPickUpStreamablePickupHandler = mockk()

    @BeforeEach
    fun setUp() {
        streamablePickupFactory = StreamablePickupFactory(
                pickupService,
                onStreamablePickupStreamInHandler,
                onStreamablePickupStreamOutHandler,
                onPlayerPickUpStreamablePickupHandler
        )
    }

    @Test
    fun shouldCreatePickup() {
        val pickup = streamablePickupFactory.create(
                modelId = 1337,
                coordinates = mutableVector3DOf(1f, 2f, 3f),
                type = 69,
                virtualWorldId = 13,
                interiorIds = mutableSetOf(187),
                streamDistance = 13.37f,
                priority = 187,
                pickupStreamer = pickupStreamer
        )

        assertAll(
                { assertThat(pickup.modelId).isEqualTo(1337) },
                { assertThat(pickup.coordinates).isEqualTo(vector3DOf(1f, 2f, 3f)) },
                { assertThat(pickup.type).isEqualTo(69) },
                { assertThat(pickup.virtualWorldId).isEqualTo(13) },
                { assertThat(pickup.interiorIds).containsExactlyInAnyOrder(187) },
                { assertThat(pickup.streamDistance).isEqualTo(13.37f) },
                { assertThat(pickup.priority).isEqualTo(187) }
        )
    }

}