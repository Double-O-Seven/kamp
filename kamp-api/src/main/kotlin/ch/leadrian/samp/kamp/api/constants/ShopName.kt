package ch.leadrian.samp.kamp.api.constants

import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.data.vector3DOf

enum class ShopName(
        val description: String,
        val coordinates: Vector3D,
        val interiorId: Int
) : ConstantValue<String> {
    NONE("", vector3DOf(0f, 0f, 0f), 0),
    FDPIZA("Pizza Stack", vector3DOf(x = 374.0000f, y = -119.6410f, z = 1001.4922f), 5),
    FDBURG("Burger Shot", vector3DOf(x = 375.5660f, y = -68.2220f, z = 1001.5151f), 10),
    FDCHICK("Cluckin' Bell", vector3DOf(x = 368.7890f, y = -6.8570f, z = 1001.8516f), 9),
    AMMUN1("AmmuNation 1", vector3DOf(x = 296.5395f, y = -38.2739f, z = 1001.515f), 1),
    AMMUN2("AmmuNation 2", vector3DOf(x = 295.7359f, y = -80.6865f, z = 1001.5156f), 4),
    AMMUN3("AmmuNation 3", vector3DOf(x = 290.2011f, y = -109.5698f, z = 1001.5156f), 6),
    AMMUN4("AmmuNation 4", vector3DOf(x = 308.1619f, y = -141.2549f, z = 999.6016f), 7),
    AMMUN5("AmmuNation 5", vector3DOf(x = 312.7883f, y = -166.0069f, z = 999.6010f), 6);


    override val value: String = name

    companion object : ConstantValueRegistry<String, ShopName>(*ShopName.values())
}