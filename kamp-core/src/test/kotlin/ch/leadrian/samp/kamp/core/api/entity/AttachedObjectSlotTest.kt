package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.Bone
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class AttachedObjectSlotTest {

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun isUsedShouldReturnExpectedResult(expectedResult: Boolean) {
        val playerId = 50
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val index = 5
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { isPlayerAttachedObjectSlotUsed(playerid = playerId, index = index) } returns expectedResult
        }
        val attachedObjectSlot = AttachedObjectSlot(
                player = player,
                index = index,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        val result = attachedObjectSlot.isUsed

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Test
    fun shouldRemoveAttachedObject() {
        val playerId = 50
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val index = 5
        val attachedObject = ch.leadrian.samp.kamp.core.api.data.AttachedObject(
                modelId = 15,
                bone = Bone.CALF_LEFT
        )
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setPlayerAttachedObject(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns true
            every { removePlayerAttachedObject(any(), any()) } returns true
        }
        val attachedObjectSlot = AttachedObjectSlot(
                player = player,
                index = index,
                nativeFunctionExecutor = nativeFunctionExecutor
        )
        attachedObjectSlot.attach(attachedObject)

        attachedObjectSlot.remove()

        verify { nativeFunctionExecutor.removePlayerAttachedObject(playerid = playerId, index = index) }
        assertThat(attachedObjectSlot.attachedObject)
                .isNull()
    }

    @Test
    fun shouldEditAttachedObject() {
        val playerId = 50
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val index = 5
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { editAttachedObject(any(), any()) } returns true
        }
        val attachedObjectSlot = AttachedObjectSlot(
                player = player,
                index = index,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        attachedObjectSlot.edit()

        verify { nativeFunctionExecutor.editAttachedObject(playerid = playerId, index = index) }
    }

    @Test
    fun shouldAttachObject() {
        val playerId = 50
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val index = 5
        val attachedObject = ch.leadrian.samp.kamp.core.api.data.AttachedObject(
                modelId = 15,
                bone = Bone.CALF_LEFT,
                offset = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                scale = vector3DOf(x = 7f, y = 8f, z = 9f),
                materialColor1 = Colors.RED,
                materialColor2 = Colors.BLUE
        )
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every { setPlayerAttachedObject(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns true
        }
        val attachedObjectSlot = AttachedObjectSlot(
                player = player,
                index = index,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        attachedObjectSlot.attach(attachedObject)

        verify {
            nativeFunctionExecutor.setPlayerAttachedObject(
                    playerid = playerId,
                    index = index,
                    modelid = 15,
                    bone = Bone.CALF_LEFT.value,
                    fOffsetX = 1f,
                    fOffsetY = 2f,
                    fOffsetZ = 3f,
                    fRotX = 4f,
                    fRotY = 5f,
                    fRotZ = 6f,
                    fScaleX = 7f,
                    fScaleY = 8f,
                    fScaleZ = 9f,
                    materialcolor1 = Colors.RED.value,
                    materialcolor2 = Colors.BLUE.value
            )
        }
        assertThat(attachedObjectSlot.attachedObject)
                .isEqualTo(attachedObject)
    }

    @Test
    fun givenSetPlayerAttachedObjectFailsItShouldSetAttachedObjectToNull() {
        val playerId = 50
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val index = 5
        val attachedObject = ch.leadrian.samp.kamp.core.api.data.AttachedObject(
                modelId = 15,
                bone = Bone.CALF_LEFT,
                offset = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                scale = vector3DOf(x = 7f, y = 8f, z = 9f),
                materialColor1 = Colors.RED,
                materialColor2 = Colors.BLUE
        )
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every {
                setPlayerAttachedObject(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
            } returns false
        }
        val attachedObjectSlot = AttachedObjectSlot(
                player = player,
                index = index,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        attachedObjectSlot.attach(attachedObject)

        assertThat(attachedObjectSlot.attachedObject)
                .isNull()
    }

    @ParameterizedTest
    @EnumSource(AttachedObjectEditResponse::class)
    fun shouldCallOnEditHandler(response: AttachedObjectEditResponse) {
        val playerId = 50
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
            every { isConnected } returns true
        }
        val index = 5
        val offset = vector3DOf(x = 1f, y = 2f, z = 3f)
        val rotation = vector3DOf(x = 4f, y = 5f, z = 6f)
        val scale = vector3DOf(x = 7f, y = 8f, z = 9f)
        val onEdit = mockk<AttachedObjectSlot.(AttachedObjectEditResponse, Int, Bone, Vector3D, Vector3D, Vector3D) -> Boolean>(relaxed = true)
        val attachedObjectSlot = AttachedObjectSlot(
                player = player,
                index = index,
                nativeFunctionExecutor = mockk()
        )
        attachedObjectSlot.onEdit(onEdit)

        attachedObjectSlot.onEdit(
                response = response,
                modelId = 15,
                bone = Bone.CALF_LEFT,
                offset = offset,
                rotation = rotation,
                scale = scale
        )

        verify { onEdit.invoke(attachedObjectSlot, response, 15, Bone.CALF_LEFT, offset, rotation, scale) }
    }

}