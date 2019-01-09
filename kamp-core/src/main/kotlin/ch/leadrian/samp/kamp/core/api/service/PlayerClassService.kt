package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.SpawnInfo
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.WeaponData
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.spawnInfoOf
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerClassId
import ch.leadrian.samp.kamp.core.api.entity.id.TeamId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerClassFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerClassRegistry
import javax.inject.Inject

class PlayerClassService
@Inject
internal constructor(
        private val playerClassFactory: PlayerClassFactory,
        private val playerClassRegistry: PlayerClassRegistry
) {

    fun addPlayerClass(spawnInfo: SpawnInfo): PlayerClass = playerClassFactory.create(spawnInfo)

    fun addPlayerClass(
            skinModel: SkinModel,
            coordinates: Vector3D,
            angle: Float,
            weapon1: WeaponData,
            weapon2: WeaponData,
            weapon3: WeaponData
    ): PlayerClass = playerClassFactory.create(
            spawnInfoOf(
                    skinModel = skinModel,
                    position = positionOf(coordinates, angle),
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3
            )
    )

    fun addPlayerClass(
            skinModel: SkinModel,
            position: Position,
            weapon1: WeaponData,
            weapon2: WeaponData,
            weapon3: WeaponData
    ): PlayerClass = playerClassFactory.create(
            spawnInfoOf(
                    skinModel = skinModel,
                    position = position,
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3
            )
    )

    fun addPlayerClass(
            teamId: TeamId,
            skinModel: SkinModel,
            position: Position,
            weapon1: WeaponData,
            weapon2: WeaponData,
            weapon3: WeaponData
    ): PlayerClass = playerClassFactory.create(
            spawnInfoOf(
                    skinModel = skinModel,
                    position = position,
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3,
                    teamId = teamId
            )
    )

    fun addPlayerClass(
            teamId: TeamId,
            skinModel: SkinModel,
            coordinates: Vector3D,
            angle: Float,
            weapon1: WeaponData,
            weapon2: WeaponData,
            weapon3: WeaponData
    ): PlayerClass = playerClassFactory.create(
            spawnInfoOf(
                    skinModel = skinModel,
                    position = positionOf(coordinates, angle),
                    weapon1 = weapon1,
                    weapon2 = weapon2,
                    weapon3 = weapon3,
                    teamId = teamId
            )
    )

    fun isValidPlayerClass(playerClassId: PlayerClassId): Boolean = playerClassRegistry[playerClassId] != null

    fun getPlayerClass(playerClassId: PlayerClassId): PlayerClass =
            playerClassRegistry[playerClassId]
                    ?: throw NoSuchEntityException("No player class with ID ${playerClassId.value}")

    fun getAllPlayerClasses(): List<PlayerClass> = playerClassRegistry.getAll()

}