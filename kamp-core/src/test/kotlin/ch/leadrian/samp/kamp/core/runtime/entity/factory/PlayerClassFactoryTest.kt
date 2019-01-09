package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.spawnInfoOf
import ch.leadrian.samp.kamp.core.api.data.weaponDataOf
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerClassId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerClassRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PlayerClassFactoryTest {

    @Test
    fun shouldCreatePlayerClass() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every {
                addPlayerClass(
                        modelid = SkinModel.ARMY.value,
                        spawn_x = 1f,
                        spawn_y = 2f,
                        spawn_z = 3f,
                        z_angle = 90f,
                        weapon1 = WeaponModel.AK47.value,
                        weapon1_ammo = 450,
                        weapon2 = WeaponModel.M4.value,
                        weapon2_ammo = 300,
                        weapon3 = WeaponModel.TEC9.value,
                        weapon3_ammo = 200
                )
            } returns 127
        }
        val playerClassRegistry = mockk<PlayerClassRegistry> {
            every { register(any()) } just Runs
            every { get(any<PlayerClassId>()) } returns null
        }
        val playerClassFactory = PlayerClassFactory(nativeFunctionExecutor, playerClassRegistry)
        val playerClass = playerClassFactory.create(spawnInfoOf(
                skinModel = SkinModel.ARMY,
                position = positionOf(1f, 2f, 3f, 90f),
                weapon1 = weaponDataOf(WeaponModel.AK47, 450),
                weapon2 = weaponDataOf(WeaponModel.M4, 300),
                weapon3 = weaponDataOf(WeaponModel.TEC9, 200)
        ))

        assertThat(playerClass.id.value)
                .isEqualTo(127)
    }

    @Test
    fun shouldRegisterPlayerClass() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every {
                addPlayerClass(
                        modelid = SkinModel.ARMY.value,
                        spawn_x = 1f,
                        spawn_y = 2f,
                        spawn_z = 3f,
                        z_angle = 90f,
                        weapon1 = WeaponModel.AK47.value,
                        weapon1_ammo = 450,
                        weapon2 = WeaponModel.M4.value,
                        weapon2_ammo = 300,
                        weapon3 = WeaponModel.TEC9.value,
                        weapon3_ammo = 200
                )
            } returns 127
        }
        val playerClassRegistry = mockk<PlayerClassRegistry> {
            every { register(any()) } just Runs
            every { get(any<PlayerClassId>()) } returns null
        }
        val playerClassFactory = PlayerClassFactory(nativeFunctionExecutor, playerClassRegistry)

        val playerClass = playerClassFactory.create(spawnInfoOf(
                skinModel = SkinModel.ARMY,
                position = positionOf(1f, 2f, 3f, 90f),
                weapon1 = weaponDataOf(WeaponModel.AK47, 450),
                weapon2 = weaponDataOf(WeaponModel.M4, 300),
                weapon3 = weaponDataOf(WeaponModel.TEC9, 200)
        ))

        verify { playerClassRegistry.register(playerClass) }
    }

    @Test
    fun givenPlayerClassWithSameIdItShouldUnregisterIt() {
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
            every {
                addPlayerClass(
                        modelid = SkinModel.ARMY.value,
                        spawn_x = 1f,
                        spawn_y = 2f,
                        spawn_z = 3f,
                        z_angle = 90f,
                        weapon1 = WeaponModel.AK47.value,
                        weapon1_ammo = 450,
                        weapon2 = WeaponModel.M4.value,
                        weapon2_ammo = 300,
                        weapon3 = WeaponModel.TEC9.value,
                        weapon3_ammo = 200
                )
            } returns 319
        }
        val existingPlayerClass = mockk<PlayerClass>()
        val playerClassRegistry = mockk<PlayerClassRegistry> {
            every { register(any()) } just Runs
            every { unregister(any()) } just Runs
            every { get(PlayerClassId.valueOf(319)) } returns existingPlayerClass
        }
        val playerClassFactory = PlayerClassFactory(nativeFunctionExecutor, playerClassRegistry)

        playerClassFactory.create(spawnInfoOf(
                skinModel = SkinModel.ARMY,
                position = positionOf(1f, 2f, 3f, 90f),
                weapon1 = weaponDataOf(WeaponModel.AK47, 450),
                weapon2 = weaponDataOf(WeaponModel.M4, 300),
                weapon3 = weaponDataOf(WeaponModel.TEC9, 200)
        ))

        verify { playerClassRegistry.unregister(existingPlayerClass) }
    }

}