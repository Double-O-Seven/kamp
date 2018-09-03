package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ActorIdTest {

    @ParameterizedTest
    @ValueSource(ints = [
        -1,
        0,
        SAMPConstants.MAX_ACTORS - 1,
        SAMPConstants.MAX_ACTORS,
        Int.MAX_VALUE,
        SAMPConstants.INVALID_ACTOR_ID
    ]
    )
    fun shouldReturnActorId(value: Int) {
        val actorId = ActorId.valueOf(value)

        assertThat(actorId.value)
                .isEqualTo(value)
    }

}