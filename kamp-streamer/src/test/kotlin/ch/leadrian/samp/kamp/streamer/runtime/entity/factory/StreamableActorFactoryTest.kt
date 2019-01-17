package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.service.ActorService
import ch.leadrian.samp.kamp.streamer.runtime.ActorStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerDamageStreamableActorHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamOutHandler
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class StreamableActorFactoryTest {

    private lateinit var streamableActorFactory: StreamableActorFactory

    private val actorStreamer: ActorStreamer = mockk()
    private val actorService: ActorService = mockk()
    private val onStreamableActorStreamInHandler: OnStreamableActorStreamInHandler = mockk()
    private val onStreamableActorStreamOutHandler: OnStreamableActorStreamOutHandler = mockk()
    private val onPlayerDamageStreamableActorHandler: OnPlayerDamageStreamableActorHandler = mockk()

    @BeforeEach
    fun setUp() {
        streamableActorFactory = StreamableActorFactory(
                actorService,
                onStreamableActorStreamInHandler,
                onStreamableActorStreamOutHandler,
                onPlayerDamageStreamableActorHandler
        )
    }

    @Test
    fun shouldCreateActor() {
        val actor = streamableActorFactory.create(
                model = SkinModel.ARMY,
                coordinates = mutableVector3DOf(1f, 2f, 3f),
                angle = 69f,
                isInvulnerable = true,
                virtualWorldId = 13,
                interiorIds = mutableSetOf(187),
                streamDistance = 13.37f,
                priority = 13,
                actorStreamer = actorStreamer
        )

        assertAll(
                { assertThat(actor.model).isEqualTo(SkinModel.ARMY) },
                { assertThat(actor.coordinates).isEqualTo(vector3DOf(1f, 2f, 3f)) },
                { assertThat(actor.angle).isEqualTo(69f) },
                { assertThat(actor.isInvulnerable).isTrue() },
                { assertThat(actor.virtualWorldId).isEqualTo(13) },
                { assertThat(actor.interiorIds).containsExactlyInAnyOrder(187) },
                { assertThat(actor.streamDistance).isEqualTo(13.37f) },
                { assertThat(actor.priority).isEqualTo(13) }
        )
    }

}