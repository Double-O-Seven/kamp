package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class QuaternionTest {

    @Test
    fun shouldCreateQuaternion() {
        val quaternion = quaternionOf(x = 1f, y = 2f, z = 3f, w = 4f)

        assertAll(
                { assertThat(quaternion.x).isEqualTo(1f) },
                { assertThat(quaternion.y).isEqualTo(2f) },
                { assertThat(quaternion.z).isEqualTo(3f) },
                { assertThat(quaternion.w).isEqualTo(4f) }
        )
    }

    @Test
    fun toQuaternionShouldReturnThis() {
        val quaternion = quaternionOf(x = 1f, y = 2f, z = 3f, w = 4f)

        val otherQuaternion = quaternion.toQuaternion()

        assertThat(otherQuaternion)
                .isSameAs(quaternion)
    }

    @Test
    fun toMutableQuaternionShouldInstanceWithSameValues() {
        val quaternion = quaternionOf(x = 1f, y = 2f, z = 3f, w = 4f)

        val mutableQuaternion: MutableQuaternion = quaternion.toMutableQuaternion()

        assertAll(
                { assertThat(mutableQuaternion).isNotSameAs(quaternion) },
                { assertThat(mutableQuaternion.x).isEqualTo(1f) },
                { assertThat(mutableQuaternion.y).isEqualTo(2f) },
                { assertThat(mutableQuaternion.z).isEqualTo(3f) },
                { assertThat(mutableQuaternion.w).isEqualTo(4f) }
        )
    }

}