package ch.leadrian.samp.kamp.examples.hooks

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSpawnListener
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import java.util.Random
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerSpawner
@Inject
constructor(private val callbackListenerManager: CallbackListenerManager) : OnPlayerSpawnListener {

    private val random = Random(System.currentTimeMillis())

    private val randomSpawns: Array<Vector3D> = arrayOf(
            vector3DOf(1958.3783f, 1343.1572f, 15.3746f),
            vector3DOf(2199.6531f, 1393.3678f, 10.8203f),
            vector3DOf(2483.5977f, 1222.0825f, 10.8203f),
            vector3DOf(2637.2712f, 1129.2743f, 11.1797f),
            vector3DOf(2000.0106f, 1521.1111f, 17.0625f),
            vector3DOf(2024.8190f, 1917.9425f, 12.3386f),
            vector3DOf(2261.9048f, 2035.9547f, 10.8203f),
            vector3DOf(2262.0986f, 2398.6572f, 10.8203f),
            vector3DOf(2244.2566f, 2523.7280f, 10.8203f),
            vector3DOf(2335.3228f, 2786.4478f, 10.8203f),
            vector3DOf(2150.0186f, 2734.2297f, 11.1763f),
            vector3DOf(2158.0811f, 2797.5488f, 10.8203f),
            vector3DOf(1969.8301f, 2722.8564f, 10.8203f),
            vector3DOf(1652.0555f, 2709.4072f, 10.8265f),
            vector3DOf(1564.0052f, 2756.9463f, 10.8203f),
            vector3DOf(1271.5452f, 2554.0227f, 10.8203f),
            vector3DOf(1441.5894f, 2567.9099f, 10.8203f),
            vector3DOf(1480.6473f, 2213.5718f, 11.0234f),
            vector3DOf(1400.5906f, 2225.6960f, 11.0234f),
            vector3DOf(1598.8419f, 2221.5676f, 11.0625f),
            vector3DOf(1318.7759f, 1251.3580f, 10.8203f),
            vector3DOf(1558.0731f, 1007.8292f, 10.8125f),
            vector3DOf(-857.0551f, 1536.6832f, 22.5870f),
            vector3DOf(817.3494f, 856.5039f, 12.7891f),
            vector3DOf(116.9315f, 1110.1823f, 13.6094f),
            vector3DOf(-18.8529f, 1176.0159f, 19.5634f),
            vector3DOf(-315.0575f, 1774.0636f, 43.6406f),
            vector3DOf(1705.2347f, 1025.6808f, 10.8203f)
    )

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerSpawn(player: Player) {
        player.coordinates = randomSpawns[random.nextInt(randomSpawns.size)]
        player.interiorId = 0
    }

}