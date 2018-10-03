package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.runtime.SAMPCallbacks
import com.netflix.governator.lifecycle.LifecycleManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CallbackProcessor
@Inject
constructor(private val lifecycleManager: LifecycleManager) : SAMPCallbacks {

    override fun onProcessTick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGameModeInit(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGameModeExit(): Boolean {
        // TODO call listeners
        lifecycleManager.close()
        return true
    }

    override fun onPlayerConnect(playerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerDisconnect(playerid: Int, reason: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerSpawn(playerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerDeath(playerid: Int, killerid: Int, reason: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehicleSpawn(vehicleid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehicleDeath(vehicleid: Int, killerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerText(playerid: Int, text: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerCommandText(playerid: Int, cmdtext: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerRequestClass(playerid: Int, classid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerEnterVehicle(playerid: Int, vehicleid: Int, ispassenger: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerExitVehicle(playerid: Int, vehicleid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerStateChange(playerid: Int, newstate: Int, oldstate: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerEnterCheckpoint(playerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerLeaveCheckpoint(playerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerEnterRaceCheckpoint(playerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerLeaveRaceCheckpoint(playerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRconCommand(cmd: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerRequestSpawn(playerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onObjectMoved(objectid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerObjectMoved(playerid: Int, objectid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerPickUpPickup(playerid: Int, pickupid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehicleMod(playerid: Int, vehicleid: Int, componentid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEnterExitModShop(playerid: Int, enterexit: Boolean, interiorid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehiclePaintjob(playerid: Int, vehicleid: Int, paintjobid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehicleRespray(playerid: Int, vehicleid: Int, color1: Int, color2: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehicleDamageStatusUpdate(vehicleid: Int, playerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUnoccupiedVehicleUpdate(vehicleid: Int, playerid: Int, passenger_seat: Int, new_x: Float, new_y: Float, new_z: Float, vel_x: Float, vel_y: Float, vel_z: Float): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerSelectedMenuRow(playerid: Int, row: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerExitedMenu(playerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerInteriorChange(playerid: Int, newinteriorid: Int, oldinteriorid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerKeyStateChange(playerid: Int, newkeys: Int, oldkeys: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRconLoginAttempt(ip: String, password: String, success: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerUpdate(playerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerStreamIn(playerid: Int, forplayerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerStreamOut(playerid: Int, forplayerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehicleStreamIn(vehicleid: Int, forplayerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehicleStreamOut(vehicleid: Int, forplayerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActorStreamIn(actorid: Int, forplayerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActorStreamOut(actorid: Int, forplayerid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDialogResponse(playerid: Int, dialogid: Int, response: Int, listitem: Int, inputtext: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerTakeDamage(playerid: Int, issuerid: Int, amount: Float, weaponid: Int, bodypart: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerGiveDamage(playerid: Int, damagedid: Int, amount: Float, weaponid: Int, bodypart: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerGiveDamageActor(playerid: Int, damaged_actorid: Int, amount: Float, weaponid: Int, bodypart: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerClickMap(playerid: Int, fX: Float, fY: Float, fZ: Float): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerClickTextDraw(playerid: Int, clickedid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerClickPlayerTextDraw(playerid: Int, playertextid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onIncomingConnection(playerid: Int, ip_address: String, port: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTrailerUpdate(playerid: Int, vehicleid: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehicleSirenStateChange(playerid: Int, vehicleid: Int, newstate: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerClickPlayer(playerid: Int, clickedplayerid: Int, source: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerEditObject(playerid: Int, playerobject: Boolean, objectid: Int, response: Int, fX: Float, fY: Float, fZ: Float, fRotX: Float, fRotY: Float, fRotZ: Float): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerEditAttachedObject(playerid: Int, response: Int, index: Int, modelid: Int, boneid: Int, fOffsetX: Float, fOffsetY: Float, fOffsetZ: Float, fRotX: Float, fRotY: Float, fRotZ: Float, fScaleX: Float, fScaleY: Float, fScaleZ: Float): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerSelectObject(playerid: Int, type: Int, objectid: Int, modelid: Int, fX: Float, fY: Float, fZ: Float): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerWeaponShot(playerid: Int, weaponid: Int, hittype: Int, hitid: Int, fX: Float, fY: Float, fZ: Float): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}