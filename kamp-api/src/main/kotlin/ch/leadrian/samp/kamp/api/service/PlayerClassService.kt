package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.constants.SkinModel
import ch.leadrian.samp.kamp.api.data.Position
import ch.leadrian.samp.kamp.api.data.SpawnInfo
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.data.WeaponData
import ch.leadrian.samp.kamp.api.entity.PlayerClass
import ch.leadrian.samp.kamp.api.entity.id.PlayerClassId
import ch.leadrian.samp.kamp.api.entity.id.TeamId

interface PlayerClassService {

    fun addPlayerClass(spawnInfo: SpawnInfo): PlayerClass

    fun addPlayerClass(
            skinModel: SkinModel,
            coordinates: Vector3D,
            angle: Float,
            weapon1: WeaponData,
            weapon2: WeaponData,
            weapon3: WeaponData
    ): PlayerClass

    fun addPlayerClass(
            skinModel: SkinModel,
            position: Position,
            weapon1: WeaponData,
            weapon2: WeaponData,
            weapon3: WeaponData
    ): PlayerClass

    fun addPlayerClass(
            teamId: TeamId,
            skinModel: SkinModel,
            position: Position,
            weapon1: WeaponData,
            weapon2: WeaponData,
            weapon3: WeaponData
    ): PlayerClass

    fun addPlayerClass(
            teamId: TeamId,
            skinModel: SkinModel,
            coordinates: Vector3D,
            angle: Float,
            weapon1: WeaponData,
            weapon2: WeaponData,
            weapon3: WeaponData
    ): PlayerClass

    fun exists(playerClassId: PlayerClassId): Boolean

    fun getPlayerClass(playerClassId: PlayerClassId): PlayerClass

    fun getAllPlayerClasses(): List<PlayerClass>

}