package ch.leadrian.samp.kamp.core.runtime.entity.delegate

import ch.leadrian.samp.kamp.core.api.data.LastShotVectors
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class PlayerLastShotVectorsDelegateTest {

    private val playerId: PlayerId = PlayerId.valueOf(50)
    private val player: Player = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var playerLastShotVectorsDelegate: PlayerLastShotVectorsDelegate

    @BeforeEach
    fun setUp() {
        every { player.id } returns playerId
        playerLastShotVectorsDelegate = PlayerLastShotVectorsDelegate(nativeFunctionExecutor)
    }

    @Test
    fun shouldLastShotVectors() {
        every {
            nativeFunctionExecutor.getPlayerLastShotVectors(
                    playerId.value,
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
            )
        } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            arg<ReferenceFloat>(4).value = 4f
            arg<ReferenceFloat>(5).value = 5f
            arg<ReferenceFloat>(6).value = 6f
            true
        }

        val lastShotVectors = playerLastShotVectorsDelegate.getValue(player, property)

        Assertions.assertThat(lastShotVectors)
                .isEqualTo(
                        LastShotVectors(
                                origin = vector3DOf(x = 1f, y = 2f, z = 3f),
                                hitPosition = vector3DOf(x = 4f, y = 5f, z = 6f)
                        )
                )
    }

}