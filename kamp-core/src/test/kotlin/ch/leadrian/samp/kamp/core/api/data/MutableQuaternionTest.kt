package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class MutableQuaternionTest {

    @Test
    fun shouldCreateMutableQuaternion() {
        val mutableQuaternion = mutableQuaternionOf(x = 1f, y = 2f, z = 3f, w = 4f)

        assertAll(
                { assertThat(mutableQuaternion.x).isEqualTo(1f) },
                { assertThat(mutableQuaternion.y).isEqualTo(2f) },
                { assertThat(mutableQuaternion.z).isEqualTo(3f) },
                { assertThat(mutableQuaternion.w).isEqualTo(4f) }
        )
    }

    @Test
    fun toMutableQuaternionShouldReturnThis() {
        val mutableQuaternion = mutableQuaternionOf(x = 1f, y = 2f, z = 3f, w = 4f)

        val otherMutableQuaternion = mutableQuaternion.toMutableQuaternion()

        assertThat(otherMutableQuaternion)
                .isSameAs(mutableQuaternion)
    }

    @Test
    fun toQuaternionShouldInstanceWithSameValues() {
        val mutableQuaternion = mutableQuaternionOf(x = 1f, y = 2f, z = 3f, w = 4f)

        val quaternion: Quaternion = mutableQuaternion.toQuaternion()

        assertAll(
                { assertThat(quaternion).isNotSameAs(mutableQuaternion) },
                { assertThat(quaternion.x).isEqualTo(1f) },
                { assertThat(quaternion.y).isEqualTo(2f) },
                { assertThat(quaternion.z).isEqualTo(3f) },
                { assertThat(quaternion.w).isEqualTo(4f) }
        )
    }

}