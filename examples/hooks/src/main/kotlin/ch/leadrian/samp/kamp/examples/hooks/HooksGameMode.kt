package ch.leadrian.samp.kamp.examples.hooks

import ch.leadrian.samp.kamp.core.api.GameMode
import ch.leadrian.samp.kamp.core.api.Plugin
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnGameModeInitListener
import ch.leadrian.samp.kamp.core.api.constants.PlayerMarkersMode
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.WeaponData
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.data.weaponDataOf
import ch.leadrian.samp.kamp.core.api.service.PickupService
import ch.leadrian.samp.kamp.core.api.service.PlayerClassService
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.service.ServerService
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import com.google.inject.Module
import javax.annotation.PostConstruct
import javax.inject.Inject

class HooksGameMode : GameMode(), OnGameModeInitListener {

    @Inject
    private lateinit var vehicleService: VehicleService
    @Inject
    private lateinit var pickupService: PickupService
    @Inject
    private lateinit var playerClassService: PlayerClassService
    @Inject
    private lateinit var playerService: PlayerService
    @Inject
    private lateinit var callbackListenerManager: CallbackListenerManager
    @Inject
    private lateinit var serverService: ServerService

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onGameModeInit() {
        serverService.setGameModeText("Native Function Hook Example")
        playerService.apply {
            showMarkers(PlayerMarkersMode.STREAMED)
            showNameTags()
        }
        SkinModel.values().forEach {
            playerClassService.addPlayerClass(
                    skinModel = it,
                    position = positionOf(1958.3783f, 1343.1572f, 15.3746f, 270.1425f),
                    weapon1 = WeaponData.FISTS,
                    weapon2 = weaponDataOf(WeaponModel.DESERT_EAGLE, 300),
                    weapon3 = WeaponData.FISTS
            )
        }
        vehicleService.apply {
            createVehicle(
                    VehicleModel[451],
                    vehicleColorsOf(16, 16),
                    positionOf(2040.052f, 1319.2799f, 10.3779f, 183.2439f),
                    300
            )
            createVehicle(
                    VehicleModel[429],
                    vehicleColorsOf(13, 13),
                    positionOf(2040.5247f, 1359.2783f, 10.3516f, 177.1306f),
                    300
            )
            createVehicle(
                    VehicleModel[421],
                    vehicleColorsOf(13, 13),
                    positionOf(2110.4102f, 1398.3672f, 10.7552f, 359.5964f),
                    300
            )
            createVehicle(
                    VehicleModel[411],
                    vehicleColorsOf(64, 64),
                    positionOf(2074.9624f, 1479.212f, 10.399f, 359.6861f),
                    300
            )
            createVehicle(
                    VehicleModel[477],
                    vehicleColorsOf(94, 94),
                    positionOf(2075.6038f, 1666.975f, 10.4252f, 359.7507f),
                    300
            )
            createVehicle(
                    VehicleModel[541],
                    vehicleColorsOf(22, 22),
                    positionOf(2119.5845f, 1938.5969f, 10.2967f, 181.9064f),
                    300
            )
            createVehicle(
                    VehicleModel[541],
                    vehicleColorsOf(60, 1),
                    positionOf(1843.7881f, 1216.0122f, 10.4556f, 270.8793f),
                    300
            )
            createVehicle(
                    VehicleModel[402],
                    vehicleColorsOf(30, 30),
                    positionOf(1944.1003f, 1344.7717f, 8.9411f, 0.8168f),
                    300
            )
            createVehicle(
                    VehicleModel[402],
                    vehicleColorsOf(90, 90),
                    positionOf(1679.2278f, 1316.6287f, 10.652f, 180.415f),
                    300
            )
            createVehicle(
                    VehicleModel[415],
                    vehicleColorsOf(25, 1),
                    positionOf(1685.4872f, 1751.9667f, 10.599f, 268.1183f),
                    300
            )
            createVehicle(
                    VehicleModel[411],
                    vehicleColorsOf(123, 1),
                    positionOf(2034.5016f, 1912.5874f, 11.9048f, 0.2909f),
                    300
            )
            createVehicle(
                    VehicleModel[411],
                    vehicleColorsOf(116, 1),
                    positionOf(2172.1682f, 1988.8643f, 10.5474f, 89.9151f),
                    300
            )
            createVehicle(
                    VehicleModel[429],
                    vehicleColorsOf(14, 14),
                    positionOf(2245.5759f, 2042.4166f, 10.5f, 270.735f),
                    300
            )
            createVehicle(
                    VehicleModel[477],
                    vehicleColorsOf(101, 1),
                    positionOf(2361.1538f, 1993.9761f, 10.426f, 178.3929f),
                    300
            )
            createVehicle(
                    VehicleModel[550],
                    vehicleColorsOf(53, 53),
                    positionOf(2221.9946f, 1998.7787f, 9.6815f, 92.6188f),
                    300
            )
            createVehicle(
                    VehicleModel[558],
                    vehicleColorsOf(116, 1),
                    positionOf(2243.3833f, 1952.4221f, 14.9761f, 359.4796f),
                    300
            )
            createVehicle(
                    VehicleModel[587],
                    vehicleColorsOf(40, 1),
                    positionOf(2276.7085f, 1938.7263f, 31.5046f, 359.2321f),
                    300
            )
            createVehicle(
                    VehicleModel[587],
                    vehicleColorsOf(43, 1),
                    positionOf(2602.7769f, 1853.0667f, 10.5468f, 91.4813f),
                    300
            )
            createVehicle(
                    VehicleModel[603],
                    vehicleColorsOf(69, 1),
                    positionOf(2610.76f, 1694.2588f, 10.6585f, 89.3303f),
                    300
            )
            createVehicle(
                    VehicleModel[587],
                    vehicleColorsOf(53, 1),
                    positionOf(2635.2419f, 1075.7726f, 10.5472f, 89.9571f),
                    300
            )
            createVehicle(
                    VehicleModel[437],
                    vehicleColorsOf(35, 1),
                    positionOf(2577.2354f, 1038.8063f, 10.4777f, 181.7069f),
                    300
            )
            createVehicle(
                    VehicleModel[535],
                    vehicleColorsOf(123, 1),
                    positionOf(2039.1257f, 1545.0879f, 10.3481f, 359.669f),
                    300
            )
            createVehicle(
                    VehicleModel[535],
                    vehicleColorsOf(66, 1),
                    positionOf(2009.8782f, 2411.7524f, 10.5828f, 178.9618f),
                    300
            )
            createVehicle(
                    VehicleModel[429],
                    vehicleColorsOf(1, 2),
                    positionOf(2010.0841f, 2489.551f, 10.5003f, 268.772f),
                    300
            )
            createVehicle(
                    VehicleModel[415],
                    vehicleColorsOf(36, 1),
                    positionOf(2076.4033f, 2468.7947f, 10.5923f, 359.9186f),
                    300
            )
            createVehicle(
                    VehicleModel[487],
                    vehicleColorsOf(26, 57),
                    positionOf(2093.2754f, 2414.9421f, 74.7556f, 89.0247f),
                    300
            )
            createVehicle(
                    VehicleModel[506],
                    vehicleColorsOf(7, 7),
                    positionOf(2352.9026f, 2577.9768f, 10.5201f, 0.4091f),
                    300
            )
            createVehicle(
                    VehicleModel[506],
                    vehicleColorsOf(52, 52),
                    positionOf(2166.6963f, 2741.0413f, 10.5245f, 89.7816f),
                    300
            )
            createVehicle(
                    VehicleModel[411],
                    vehicleColorsOf(112, 1),
                    positionOf(1960.9989f, 2754.9072f, 10.5473f, 200.4316f),
                    300
            )
            createVehicle(
                    VehicleModel[429],
                    vehicleColorsOf(2, 1),
                    positionOf(1919.5863f, 2760.7595f, 10.5079f, 100.0753f),
                    300
            )
            createVehicle(
                    VehicleModel[415],
                    vehicleColorsOf(40, 1),
                    positionOf(1673.8038f, 2693.8044f, 10.5912f, 359.7903f),
                    300
            )
            createVehicle(
                    VehicleModel[402],
                    vehicleColorsOf(30, 30),
                    positionOf(1591.0482f, 2746.3982f, 10.6519f, 172.5125f),
                    300
            )
            createVehicle(
                    VehicleModel[603],
                    vehicleColorsOf(75, 77),
                    positionOf(1580.4537f, 2838.2886f, 10.6614f, 181.4573f),
                    300
            )
            createVehicle(
                    VehicleModel[550],
                    vehicleColorsOf(62, 62),
                    positionOf(1555.2734f, 2750.5261f, 10.6388f, 91.7773f),
                    300
            )
            createVehicle(
                    VehicleModel[535],
                    vehicleColorsOf(118, 1),
                    positionOf(1455.9305f, 2878.5288f, 10.5837f, 181.0987f),
                    300
            )
            createVehicle(
                    VehicleModel[477],
                    vehicleColorsOf(121, 1),
                    positionOf(1537.8425f, 2578.0525f, 10.5662f, 0.065f),
                    300
            )
            createVehicle(
                    VehicleModel[451],
                    vehicleColorsOf(16, 16),
                    positionOf(1433.1594f, 2607.3762f, 10.3781f, 88.0013f),
                    300
            )
            createVehicle(
                    VehicleModel[603],
                    vehicleColorsOf(18, 1),
                    positionOf(2223.5898f, 1288.1464f, 10.5104f, 182.0297f),
                    300
            )
            createVehicle(
                    VehicleModel[558],
                    vehicleColorsOf(24, 1),
                    positionOf(2451.6707f, 1207.1179f, 10.451f, 179.896f),
                    300
            )
            createVehicle(
                    VehicleModel[550],
                    vehicleColorsOf(62, 62),
                    positionOf(2461.7253f, 1357.9705f, 10.6389f, 180.2927f),
                    300
            )
            createVehicle(
                    VehicleModel[558],
                    vehicleColorsOf(117, 1),
                    positionOf(2461.8162f, 1629.2268f, 10.4496f, 181.4625f),
                    300
            )
            createVehicle(
                    VehicleModel[477],
                    vehicleColorsOf(0, 1),
                    positionOf(2395.7554f, 1658.9591f, 10.574f, 359.7374f),
                    300
            )
            createVehicle(
                    VehicleModel[404],
                    vehicleColorsOf(119, 50),
                    positionOf(1553.3696f, 1020.2884f, 10.5532f, 270.6825f),
                    300
            )
            createVehicle(
                    VehicleModel[400],
                    vehicleColorsOf(123, 1),
                    positionOf(1380.8304f, 1159.1782f, 10.9128f, 355.7117f),
                    300
            )
            createVehicle(
                    VehicleModel[418],
                    vehicleColorsOf(117, 227),
                    positionOf(1383.463f, 1035.042f, 10.9131f, 91.2515f),
                    300
            )
            createVehicle(
                    VehicleModel[404],
                    vehicleColorsOf(109, 100),
                    positionOf(1445.4526f, 974.2831f, 10.5534f, 1.6213f),
                    300
            )
            createVehicle(
                    VehicleModel[400],
                    vehicleColorsOf(113, 1),
                    positionOf(1704.2365f, 940.149f, 10.9127f, 91.9048f),
                    300
            )
            createVehicle(
                    VehicleModel[404],
                    vehicleColorsOf(101, 101),
                    positionOf(1658.5463f, 1028.5432f, 10.5533f, 359.8419f),
                    300
            )
            createVehicle(
                    VehicleModel[581],
                    vehicleColorsOf(58, 1),
                    positionOf(1677.6628f, 1040.193f, 10.4136f, 178.7038f),
                    300
            )
            createVehicle(
                    VehicleModel[581],
                    vehicleColorsOf(66, 1),
                    positionOf(1383.6959f, 1042.2114f, 10.4121f, 85.7269f),
                    300
            )
            createVehicle(
                    VehicleModel[581],
                    vehicleColorsOf(72, 1),
                    positionOf(1064.2332f, 1215.4158f, 10.4157f, 177.2942f),
                    300
            )
            createVehicle(
                    VehicleModel[581],
                    vehicleColorsOf(72, 1),
                    positionOf(1111.4536f, 1788.3893f, 10.4158f, 92.4627f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(3, 8),
                    positionOf(953.2818f, 1806.1392f, 8.2188f, 235.0706f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(3, 8),
                    positionOf(995.5328f, 1886.6055f, 10.5359f, 90.1048f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(75, 13),
                    positionOf(993.7083f, 2267.4133f, 11.0315f, 1.561f),
                    300
            )
            createVehicle(
                    VehicleModel[535],
                    vehicleColorsOf(66, 1),
                    positionOf(1439.5662f, 1999.9822f, 10.5843f, 0.4194f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(6, 25),
                    positionOf(1430.2354f, 1999.0144f, 10.3896f, 352.0951f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(6, 25),
                    positionOf(2156.354f, 2188.6572f, 10.2414f, 22.6504f),
                    300
            )
            createVehicle(
                    VehicleModel[598],
                    vehicleColorsOf(0, 1),
                    positionOf(2277.6846f, 2477.1096f, 10.5652f, 180.109f),
                    300
            )
            createVehicle(
                    VehicleModel[598],
                    vehicleColorsOf(0, 1),
                    positionOf(2268.9888f, 2443.1697f, 10.5662f, 181.8062f),
                    300
            )
            createVehicle(
                    VehicleModel[598],
                    vehicleColorsOf(0, 1),
                    positionOf(2256.2891f, 2458.511f, 10.568f, 358.7335f),
                    300
            )
            createVehicle(
                    VehicleModel[598],
                    vehicleColorsOf(0, 1),
                    positionOf(2251.6921f, 2477.0205f, 10.5671f, 179.5244f),
                    300
            )
            createVehicle(
                    VehicleModel[523],
                    vehicleColorsOf(0, 0),
                    positionOf(2294.7305f, 2441.2651f, 10.386f, 9.3764f),
                    300
            )
            createVehicle(
                    VehicleModel[523],
                    vehicleColorsOf(0, 0),
                    positionOf(2290.7268f, 2441.3323f, 10.3944f, 16.4594f),
                    300
            )
            createVehicle(
                    VehicleModel[523],
                    vehicleColorsOf(0, 0),
                    positionOf(2295.5503f, 2455.9656f, 2.8444f, 272.6913f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(8, 82),
                    positionOf(2476.79f, 2532.2222f, 21.4416f, 0.5081f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(8, 82),
                    positionOf(2580.532f, 2267.9595f, 10.3917f, 271.2372f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(36, 105),
                    positionOf(2814.4331f, 2364.6641f, 10.3907f, 89.6752f),
                    300
            )
            createVehicle(
                    VehicleModel[535],
                    vehicleColorsOf(97, 1),
                    positionOf(2827.4143f, 2345.6953f, 10.5768f, 270.0668f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(87, 118),
                    positionOf(1670.1089f, 1297.8322f, 10.3864f, 359.4936f),
                    300
            )
            createVehicle(
                    VehicleModel[487],
                    vehicleColorsOf(58, 8),
                    positionOf(1614.7153f, 1548.7513f, 11.2749f, 347.1516f),
                    300
            )
            createVehicle(
                    VehicleModel[487],
                    vehicleColorsOf(0, 8),
                    positionOf(1647.7902f, 1538.9934f, 11.2433f, 51.8071f),
                    300
            )
            createVehicle(
                    VehicleModel[487],
                    vehicleColorsOf(58, 8),
                    positionOf(1608.3851f, 1630.7268f, 11.284f, 174.5517f),
                    300
            )
            createVehicle(
                    VehicleModel[476],
                    vehicleColorsOf(7, 6),
                    positionOf(1283.0006f, 1324.8849f, 9.5332f, 275.0468f),
                    300
            )
            createVehicle(
                    VehicleModel[476],
                    vehicleColorsOf(1, 6),
                    positionOf(1283.5107f, 1361.3171f, 9.5382f, 271.1684f),
                    300
            )
            createVehicle(
                    VehicleModel[476],
                    vehicleColorsOf(89, 91),
                    positionOf(1283.6847f, 1386.5137f, 11.53f, 272.1003f),
                    300
            )
            createVehicle(
                    VehicleModel[476],
                    vehicleColorsOf(119, 117),
                    positionOf(1288.0499f, 1403.6605f, 11.5295f, 243.5028f),
                    300
            )
            createVehicle(
                    VehicleModel[415],
                    vehicleColorsOf(62, 1),
                    positionOf(1319.1038f, 1279.1791f, 10.5931f, 0.9661f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(92, 3),
                    positionOf(1710.5763f, 1805.9275f, 10.3911f, 176.5028f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(92, 3),
                    positionOf(2805.165f, 2027.0028f, 10.392f, 357.5978f),
                    300
            )
            createVehicle(
                    VehicleModel[535],
                    vehicleColorsOf(123, 1),
                    positionOf(2822.3628f, 2240.3594f, 10.5812f, 89.754f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(115, 118),
                    positionOf(2876.8013f, 2326.8418f, 10.3914f, 267.8946f),
                    300
            )
            createVehicle(
                    VehicleModel[429],
                    vehicleColorsOf(1, 3),
                    positionOf(2842.0554f, 2637.0105f, 10.5f, 182.2949f),
                    300
            )
            createVehicle(
                    VehicleModel[549],
                    vehicleColorsOf(72, 39),
                    positionOf(2494.4214f, 2813.9348f, 10.5172f, 316.9462f),
                    300
            )
            createVehicle(
                    VehicleModel[549],
                    vehicleColorsOf(75, 39),
                    positionOf(2327.6484f, 2787.7327f, 10.5174f, 179.5639f),
                    300
            )
            createVehicle(
                    VehicleModel[549],
                    vehicleColorsOf(79, 39),
                    positionOf(2142.697f, 2806.6758f, 10.5176f, 89.897f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(25, 118),
                    positionOf(2139.7012f, 2799.2114f, 10.3917f, 229.6327f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(36, 0),
                    positionOf(2104.9446f, 2658.1331f, 10.3834f, 82.27f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(36, 0),
                    positionOf(1914.2322f, 2148.259f, 10.3906f, 267.7297f),
                    300
            )
            createVehicle(
                    VehicleModel[549],
                    vehicleColorsOf(83, 36),
                    positionOf(1904.7527f, 2157.4312f, 10.5175f, 183.7728f),
                    300
            )
            createVehicle(
                    VehicleModel[549],
                    vehicleColorsOf(84, 36),
                    positionOf(1532.6139f, 2258.0173f, 10.5176f, 359.1516f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(118, 118),
                    positionOf(1534.3204f, 2202.897f, 10.3644f, 4.9108f),
                    300
            )
            createVehicle(
                    VehicleModel[549],
                    vehicleColorsOf(89, 35),
                    positionOf(1613.1553f, 2200.2664f, 10.5176f, 89.6204f),
                    300
            )
            createVehicle(
                    VehicleModel[400],
                    vehicleColorsOf(101, 1),
                    positionOf(1552.1292f, 2341.7854f, 10.9126f, 274.0815f),
                    300
            )
            createVehicle(
                    VehicleModel[404],
                    vehicleColorsOf(101, 101),
                    positionOf(1637.6285f, 2329.8774f, 10.5538f, 89.6408f),
                    300
            )
            createVehicle(
                    VehicleModel[400],
                    vehicleColorsOf(62, 1),
                    positionOf(1357.4165f, 2259.7158f, 10.9126f, 269.5567f),
                    300
            )
            createVehicle(
                    VehicleModel[411],
                    vehicleColorsOf(106, 1),
                    positionOf(1281.7458f, 2571.6719f, 10.5472f, 270.6128f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(3, 8),
                    positionOf(1305.5295f, 2528.3076f, 10.3955f, 88.7249f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(74, 74),
                    positionOf(993.902f, 2159.4194f, 10.3905f, 88.8805f),
                    300
            )
            createVehicle(
                    VehicleModel[415],
                    vehicleColorsOf(75, 1),
                    positionOf(1512.7134f, 787.6931f, 10.5921f, 359.5796f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(3, 8),
                    positionOf(2299.5872f, 1469.791f, 10.3815f, 258.4984f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(3, 8),
                    positionOf(2133.6428f, 1012.8537f, 10.3789f, 87.129f),
                    300
            )
            createVehicle(
                    VehicleModel[415],
                    vehicleColorsOf(0, 1),
                    positionOf(2266.7336f, 648.4756f, 11.0053f, 177.8517f),
                    300
            )
            createVehicle(
                    VehicleModel[461],
                    vehicleColorsOf(53, 1),
                    positionOf(2404.6636f, 647.9255f, 10.7919f, 183.7688f),
                    300
            )
            createVehicle(
                    VehicleModel[506],
                    vehicleColorsOf(3, 3),
                    positionOf(2628.1047f, 746.8704f, 10.5246f, 352.7574f),
                    300
            )
            createVehicle(
                    VehicleModel[549],
                    vehicleColorsOf(72, 39),
                    positionOf(2817.6445f, 928.3469f, 10.447f, 359.5235f),
                    300
            )
            createVehicle(
                    VehicleModel[562],
                    vehicleColorsOf(11, 1),
                    positionOf(1919.8829f, 947.1886f, 10.4715f, 359.4453f),
                    300
            )
            createVehicle(
                    VehicleModel[562],
                    vehicleColorsOf(11, 1),
                    positionOf(1881.6346f, 1006.7653f, 10.4783f, 86.9967f),
                    300
            )
            createVehicle(
                    VehicleModel[562],
                    vehicleColorsOf(11, 1),
                    positionOf(2038.1044f, 1006.4022f, 10.404f, 179.2641f),
                    300
            )
            createVehicle(
                    VehicleModel[562],
                    vehicleColorsOf(11, 1),
                    positionOf(2038.1614f, 1014.8566f, 10.4057f, 179.8665f),
                    300
            )
            createVehicle(
                    VehicleModel[562],
                    vehicleColorsOf(11, 1),
                    positionOf(2038.0966f, 1026.7987f, 10.404f, 180.6107f),
                    300
            )
            createVehicle(
                    VehicleModel[422],
                    vehicleColorsOf(101, 25),
                    positionOf(9.1065f, 1165.5066f, 19.5855f, 2.1281f),
                    300
            )
            createVehicle(
                    VehicleModel[463],
                    vehicleColorsOf(11, 11),
                    positionOf(19.8059f, 1163.7103f, 19.1504f, 346.3326f),
                    300
            )
            createVehicle(
                    VehicleModel[463],
                    vehicleColorsOf(22, 22),
                    positionOf(12.574f, 1232.2848f, 18.8822f, 121.867f),
                    300
            )
            createVehicle(
                    VehicleModel[434],
                    vehicleColorsOf(2, 2),
                    positionOf(-110.8473f, 1133.7113f, 19.7091f, 359.7f),
                    300
            )
            createVehicle(
                    VehicleModel[586],
                    vehicleColorsOf(10, 1),
                    positionOf(69.4633f, 1217.0189f, 18.3304f, 158.9345f),
                    300
            )
            createVehicle(
                    VehicleModel[586],
                    vehicleColorsOf(25, 1),
                    positionOf(-199.4185f, 1223.0405f, 19.2624f, 176.7001f),
                    300
            )
            createVehicle(
                    VehicleModel[605],
                    vehicleColorsOf(43, 8),
                    positionOf(-340.2598f, 1177.4846f, 19.5565f, 182.6176f),
                    300
            )
            createVehicle(
                    VehicleModel[476],
                    vehicleColorsOf(71, 77),
                    positionOf(325.4121f, 2538.5999f, 17.5184f, 181.2964f),
                    300
            )
            createVehicle(
                    VehicleModel[476],
                    vehicleColorsOf(7, 6),
                    positionOf(291.0975f, 2540.041f, 17.5276f, 182.7206f),
                    300
            )
            createVehicle(
                    VehicleModel[576],
                    vehicleColorsOf(72, 1),
                    positionOf(384.2365f, 2602.1763f, 16.0926f, 192.4858f),
                    300
            )
            createVehicle(
                    VehicleModel[586],
                    vehicleColorsOf(10, 1),
                    positionOf(423.8012f, 2541.687f, 15.9708f, 338.2426f),
                    300
            )
            createVehicle(
                    VehicleModel[586],
                    vehicleColorsOf(10, 1),
                    positionOf(-244.0047f, 2724.5439f, 62.2077f, 51.5825f),
                    300
            )
            createVehicle(
                    VehicleModel[586],
                    vehicleColorsOf(27, 1),
                    positionOf(-311.1414f, 2659.4329f, 62.4513f, 310.9601f),
                    300
            )
            createVehicle(
                    VehicleModel[406],
                    vehicleColorsOf(1, 1),
                    positionOf(547.4633f, 843.0204f, -39.8406f, 285.2948f),
                    300
            )
            createVehicle(
                    VehicleModel[406],
                    vehicleColorsOf(1, 1),
                    positionOf(625.1979f, 828.9873f, -41.4497f, 71.336f),
                    300
            )
            createVehicle(
                    VehicleModel[486],
                    vehicleColorsOf(1, 1),
                    positionOf(680.7997f, 919.051f, -40.4735f, 105.9145f),
                    300
            )
            createVehicle(
                    VehicleModel[486],
                    vehicleColorsOf(1, 1),
                    positionOf(674.3994f, 927.7518f, -40.6087f, 128.6116f),
                    300
            )
            createVehicle(
                    VehicleModel[543],
                    vehicleColorsOf(67, 8),
                    positionOf(596.8064f, 866.2578f, -43.2617f, 186.8359f),
                    300
            )
            createVehicle(
                    VehicleModel[543],
                    vehicleColorsOf(8, 90),
                    positionOf(835.0838f, 836.837f, 11.8739f, 14.892f),
                    300
            )
            createVehicle(
                    VehicleModel[549],
                    vehicleColorsOf(79, 39),
                    positionOf(843.1893f, 838.8093f, 12.5177f, 18.2348f),
                    300
            )
            createVehicle(
                    VehicleModel[605],
                    vehicleColorsOf(8, 90),
                    positionOf(319.3803f, 740.2404f, 6.7814f, 271.2593f),
                    300
            )
            createVehicle(
                    VehicleModel[400],
                    vehicleColorsOf(75, 1),
                    positionOf(-235.9767f, 1045.8623f, 19.8158f, 180.0806f),
                    300
            )
            createVehicle(
                    VehicleModel[599],
                    vehicleColorsOf(0, 1),
                    positionOf(-211.594f, 998.9857f, 19.8437f, 265.4935f),
                    300
            )
            createVehicle(
                    VehicleModel[422],
                    vehicleColorsOf(96, 25),
                    positionOf(-304.062f, 1024.1111f, 19.5714f, 94.1812f),
                    300
            )
            createVehicle(
                    VehicleModel[588],
                    vehicleColorsOf(1, 1),
                    positionOf(-290.2229f, 1317.0276f, 54.1871f, 81.7529f),
                    300
            )
            createVehicle(
                    VehicleModel[424],
                    vehicleColorsOf(2, 2),
                    positionOf(-330.2399f, 1514.3022f, 75.1388f, 179.1514f),
                    300
            )
            createVehicle(
                    VehicleModel[451],
                    vehicleColorsOf(61, 61),
                    positionOf(-290.3145f, 1567.1534f, 75.0654f, 133.1694f),
                    300
            )
            createVehicle(
                    VehicleModel[470],
                    vehicleColorsOf(43, 0),
                    positionOf(280.4914f, 1945.6143f, 17.6317f, 310.3278f),
                    300
            )
            createVehicle(
                    VehicleModel[470],
                    vehicleColorsOf(43, 0),
                    positionOf(272.2862f, 1949.4713f, 17.6367f, 285.9714f),
                    300
            )
            createVehicle(
                    VehicleModel[470],
                    vehicleColorsOf(43, 0),
                    positionOf(271.6122f, 1961.2386f, 17.6373f, 251.9081f),
                    300
            )
            createVehicle(
                    VehicleModel[470],
                    vehicleColorsOf(43, 0),
                    positionOf(279.8705f, 1966.2362f, 17.6436f, 228.4709f),
                    300
            )
            createVehicle(
                    VehicleModel[548],
                    vehicleColorsOf(1, 1),
                    positionOf(292.2317f, 1923.644f, 19.2898f, 235.3379f),
                    300
            )
            createVehicle(
                    VehicleModel[433],
                    vehicleColorsOf(43, 0),
                    positionOf(277.6437f, 1985.7559f, 18.0772f, 270.4079f),
                    300
            )
            createVehicle(
                    VehicleModel[433],
                    vehicleColorsOf(43, 0),
                    positionOf(277.4477f, 1994.8329f, 18.0773f, 267.7378f),
                    300
            )
            createVehicle(
                    VehicleModel[432],
                    vehicleColorsOf(43, 0),
                    positionOf(275.9634f, 2024.3629f, 17.6516f, 270.6823f),
                    300
            )
            createVehicle(
                    VehicleModel[568],
                    vehicleColorsOf(41, 29),
                    positionOf(-441.3438f, 2215.7026f, 42.2489f, 191.7953f),
                    300
            )
            createVehicle(
                    VehicleModel[568],
                    vehicleColorsOf(41, 29),
                    positionOf(-422.2956f, 2225.2612f, 42.2465f, 0.0616f),
                    300
            )
            createVehicle(
                    VehicleModel[568],
                    vehicleColorsOf(41, 29),
                    positionOf(-371.7973f, 2234.5527f, 42.3497f, 285.9481f),
                    300
            )
            createVehicle(
                    VehicleModel[568],
                    vehicleColorsOf(41, 29),
                    positionOf(-360.1159f, 2203.4272f, 42.3039f, 113.6446f),
                    300
            )
            createVehicle(
                    VehicleModel[468],
                    vehicleColorsOf(6, 6),
                    positionOf(-660.7385f, 2315.2642f, 138.3866f, 358.7643f),
                    300
            )
            createVehicle(
                    VehicleModel[460],
                    vehicleColorsOf(1, 3),
                    positionOf(-1029.2648f, 2237.2217f, 42.2679f, 260.5732f),
                    300
            )
            createVehicle(
                    VehicleModel[419],
                    vehicleColorsOf(13, 76),
                    positionOf(95.0568f, 1056.553f, 13.4068f, 192.1461f),
                    300
            )
            createVehicle(
                    VehicleModel[429],
                    vehicleColorsOf(1, 2),
                    positionOf(114.7416f, 1048.3517f, 13.289f, 174.9752f),
                    300
            )
            createVehicle(
                    VehicleModel[466],
                    vehicleColorsOf(78, 76),
                    positionOf(124.248f, 1075.1835f, 13.3512f, 174.5334f),
                    300
            )
            createVehicle(
                    VehicleModel[411],
                    vehicleColorsOf(116, 1),
                    positionOf(-290.0065f, 1759.4958f, 42.4154f, 89.7571f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(6, 25),
                    positionOf(-302.5649f, 1777.7349f, 42.2514f, 238.5039f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(8, 82),
                    positionOf(-302.965f, 1776.1152f, 42.2588f, 239.9874f),
                    300
            )
            createVehicle(
                    VehicleModel[533],
                    vehicleColorsOf(75, 1),
                    positionOf(-301.0404f, 1750.8517f, 42.3966f, 268.7585f),
                    300
            )
            createVehicle(
                    VehicleModel[535],
                    vehicleColorsOf(31, 1),
                    positionOf(-866.1774f, 1557.27f, 23.8319f, 269.3263f),
                    300
            )
            createVehicle(
                    VehicleModel[550],
                    vehicleColorsOf(53, 53),
                    positionOf(-799.3062f, 1518.1556f, 26.7488f, 88.5295f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(92, 3),
                    positionOf(-749.973f, 1589.8435f, 26.5311f, 125.6508f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(3, 3),
                    positionOf(-867.8612f, 1544.5282f, 22.5419f, 296.0923f),
                    300
            )
            createVehicle(
                    VehicleModel[554],
                    vehicleColorsOf(34, 30),
                    positionOf(-904.2978f, 1553.8269f, 25.9229f, 266.6985f),
                    300
            )
            createVehicle(
                    VehicleModel[521],
                    vehicleColorsOf(92, 3),
                    positionOf(-944.2642f, 1424.1603f, 29.6783f, 148.5582f),
                    300
            )
            createVehicle(
                    VehicleModel[429],
                    vehicleColorsOf(1, 2),
                    positionOf(-237.7157f, 2594.8804f, 62.3828f, 178.6802f),
                    300
            )
            createVehicle(
                    VehicleModel[431],
                    vehicleColorsOf(47, 74),
                    positionOf(-160.5815f, 2693.7185f, 62.2031f, 89.4133f),
                    300
            )
            createVehicle(
                    VehicleModel[463],
                    vehicleColorsOf(22, 22),
                    positionOf(-196.3012f, 2774.4395f, 61.4775f, 303.8402f),
                    300
            )
            createVehicle(
                    VehicleModel[483],
                    vehicleColorsOf(1, 5),
                    positionOf(-204.1827f, 2608.7368f, 62.6956f, 179.9914f),
                    300
            )
            createVehicle(
                    VehicleModel[490],
                    vehicleColorsOf(0, 0),
                    positionOf(-295.4756f, 2674.9141f, 62.7434f, 359.3378f),
                    300
            )
            createVehicle(
                    VehicleModel[500],
                    vehicleColorsOf(28, 119),
                    positionOf(-301.5293f, 2687.6013f, 62.7723f, 87.9509f),
                    300
            )
            createVehicle(
                    VehicleModel[500],
                    vehicleColorsOf(13, 119),
                    positionOf(-301.6699f, 2680.3293f, 62.7393f, 89.7925f),
                    300
            )
            createVehicle(
                    VehicleModel[519],
                    vehicleColorsOf(1, 1),
                    positionOf(-1341.1079f, -254.3787f, 15.0701f, 321.6338f),
                    300
            )
            createVehicle(
                    VehicleModel[519],
                    vehicleColorsOf(1, 1),
                    positionOf(-1371.1775f, -232.3967f, 15.0676f, 315.6091f),
                    300
            )
            createVehicle(
                    VehicleModel[552],
                    vehicleColorsOf(56, 56),
                    positionOf(-1396.2028f, -196.8298f, 13.8434f, 286.272f),
                    300
            )
            createVehicle(
                    VehicleModel[552],
                    vehicleColorsOf(56, 56),
                    positionOf(-1312.4509f, -284.4692f, 13.8417f, 354.3546f),
                    300
            )
            createVehicle(
                    VehicleModel[552],
                    vehicleColorsOf(56, 56),
                    positionOf(-1393.5995f, -521.077f, 13.8441f, 187.1324f),
                    300
            )
            createVehicle(
                    VehicleModel[513],
                    vehicleColorsOf(48, 18),
                    positionOf(-1355.6632f, -488.9562f, 14.7157f, 191.2547f),
                    300
            )
            createVehicle(
                    VehicleModel[513],
                    vehicleColorsOf(54, 34),
                    positionOf(-1374.458f, -499.1462f, 14.7482f, 220.4057f),
                    300
            )
            createVehicle(
                    VehicleModel[553],
                    vehicleColorsOf(91, 87),
                    positionOf(-1197.8773f, -489.6715f, 15.4841f, 0.4029f),
                    300
            )
            createVehicle(
                    VehicleModel[553],
                    vehicleColorsOf(102, 119),
                    positionOf(1852.9989f, -2385.4009f, 14.8827f, 200.0707f),
                    300
            )
            createVehicle(
                    VehicleModel[583],
                    vehicleColorsOf(1, 1),
                    positionOf(1879.9594f, -2349.1919f, 13.0875f, 11.0992f),
                    300
            )
            createVehicle(
                    VehicleModel[583],
                    vehicleColorsOf(1, 1),
                    positionOf(1620.9697f, -2431.0752f, 13.0951f, 126.3341f),
                    300
            )
            createVehicle(
                    VehicleModel[583],
                    vehicleColorsOf(1, 1),
                    positionOf(1545.1564f, -2409.2114f, 13.0953f, 23.5581f),
                    300
            )
            createVehicle(
                    VehicleModel[583],
                    vehicleColorsOf(1, 1),
                    positionOf(1656.3702f, -2651.7913f, 13.0874f, 352.7619f),
                    300
            )
            createVehicle(
                    VehicleModel[519],
                    vehicleColorsOf(1, 1),
                    positionOf(1642.985f, -2425.2063f, 14.4744f, 159.8745f),
                    300
            )
            createVehicle(
                    VehicleModel[519],
                    vehicleColorsOf(1, 1),
                    positionOf(1734.1311f, -2426.7563f, 14.4734f, 172.2036f),
                    300
            )
            createVehicle(
                    VehicleModel[415],
                    vehicleColorsOf(36, 1),
                    positionOf(-680.9882f, 955.4495f, 11.9032f, 84.2754f),
                    300
            )
            createVehicle(
                    VehicleModel[460],
                    vehicleColorsOf(1, 3),
                    positionOf(-816.3951f, 2222.7375f, 43.0045f, 268.1861f),
                    300
            )
            createVehicle(
                    VehicleModel[460],
                    vehicleColorsOf(1, 3),
                    positionOf(-94.6885f, 455.4018f, 1.5719f, 250.5473f),
                    300
            )
            createVehicle(
                    VehicleModel[460],
                    vehicleColorsOf(1, 3),
                    positionOf(1624.5901f, 565.8568f, 1.7817f, 200.5292f),
                    300
            )
            createVehicle(
                    VehicleModel[460],
                    vehicleColorsOf(1, 3),
                    positionOf(1639.3567f, 572.272f, 1.5311f, 206.616f),
                    300
            )
            createVehicle(
                    VehicleModel[460],
                    vehicleColorsOf(1, 3),
                    positionOf(2293.4219f, 517.5514f, 1.7537f, 270.7889f),
                    300
            )
            createVehicle(
                    VehicleModel[460],
                    vehicleColorsOf(1, 3),
                    positionOf(2354.469f, 518.5284f, 1.745f, 270.2214f),
                    300
            )
            createVehicle(
                    VehicleModel[460],
                    vehicleColorsOf(1, 3),
                    positionOf(772.4293f, 2912.5579f, 1.0753f, 69.6706f),
                    300
            )
            createVehicle(
                    VehicleModel[560],
                    vehicleColorsOf(9, 39),
                    positionOf(2133.0769f, 1019.2366f, 10.5259f, 90.5265f),
                    300
            )
            createVehicle(
                    VehicleModel[560],
                    vehicleColorsOf(17, 1),
                    positionOf(2142.4023f, 1408.5675f, 10.5258f, 0.366f),
                    300
            )
            createVehicle(
                    VehicleModel[560],
                    vehicleColorsOf(21, 1),
                    positionOf(2196.334f, 1856.8469f, 10.5257f, 179.807f),
                    300
            )
            createVehicle(
                    VehicleModel[560],
                    vehicleColorsOf(33, 0),
                    positionOf(2103.4146f, 2069.1514f, 10.5249f, 270.1451f),
                    300
            )
            createVehicle(
                    VehicleModel[560],
                    vehicleColorsOf(37, 0),
                    positionOf(2361.8042f, 2210.9951f, 10.3848f, 178.7366f),
                    300
            )
            createVehicle(
                    VehicleModel[560],
                    vehicleColorsOf(41, 29),
                    positionOf(-1993.2465f, 241.5329f, 34.8774f, 310.0117f),
                    300
            )
            createVehicle(
                    VehicleModel[559],
                    vehicleColorsOf(58, 8),
                    positionOf(-1989.3235f, 270.1447f, 34.8321f, 88.6822f),
                    300
            )
            createVehicle(
                    VehicleModel[559],
                    vehicleColorsOf(60, 1),
                    positionOf(-1946.2416f, 273.2482f, 35.1302f, 126.42f),
                    300
            )
            createVehicle(
                    VehicleModel[558],
                    vehicleColorsOf(24, 1),
                    positionOf(-1956.8257f, 271.4941f, 35.0984f, 71.7499f),
                    300
            )
            createVehicle(
                    VehicleModel[562],
                    vehicleColorsOf(17, 1),
                    positionOf(-1952.8894f, 258.8604f, 40.7082f, 51.7172f),
                    300
            )
            createVehicle(
                    VehicleModel[411],
                    vehicleColorsOf(112, 1),
                    positionOf(-1949.8689f, 266.5759f, 40.7776f, 216.4882f),
                    300
            )
            createVehicle(
                    VehicleModel[429],
                    vehicleColorsOf(2, 1),
                    positionOf(-1988.0347f, 305.4242f, 34.8553f, 87.0725f),
                    300
            )
            createVehicle(
                    VehicleModel[559],
                    vehicleColorsOf(13, 8),
                    positionOf(-1657.666f, 1213.6195f, 6.9062f, 282.6953f),
                    300
            )
            createVehicle(
                    VehicleModel[560],
                    vehicleColorsOf(52, 39),
                    positionOf(-1658.3722f, 1213.2236f, 13.3806f, 37.9052f),
                    300
            )
            createVehicle(
                    VehicleModel[558],
                    vehicleColorsOf(36, 1),
                    positionOf(-1660.8994f, 1210.7589f, 20.7875f, 317.6098f),
                    300
            )
            createVehicle(
                    VehicleModel[550],
                    vehicleColorsOf(7, 7),
                    positionOf(-1645.2401f, 1303.9883f, 6.8482f, 133.6013f),
                    300
            )
            createVehicle(
                    VehicleModel[460],
                    vehicleColorsOf(46, 32),
                    positionOf(-1333.196f, 903.766f, 1.5568f, 0.5095f),
                    300
            )
            createVehicle(
                    VehicleModel[411],
                    vehicleColorsOf(116, 1),
                    positionOf(113.8611f, 1068.6182f, 13.3395f, 177.133f),
                    300
            )
            createVehicle(
                    VehicleModel[429],
                    vehicleColorsOf(1, 2),
                    positionOf(159.5199f, 1185.116f, 14.7324f, 85.5769f),
                    300
            )
            createVehicle(
                    VehicleModel[411],
                    vehicleColorsOf(75, 1),
                    positionOf(612.4678f, 1694.4126f, 6.7192f, 302.5539f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(6, 25),
                    positionOf(661.7609f, 1720.9894f, 6.5641f, 19.1231f),
                    300
            )
            createVehicle(
                    VehicleModel[522],
                    vehicleColorsOf(8, 82),
                    positionOf(660.0554f, 1719.1187f, 6.5642f, 12.7699f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(90, 96),
                    positionOf(711.4207f, 1947.5208f, 5.4056f, 179.381f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(97, 96),
                    positionOf(1031.8435f, 1920.3726f, 11.3369f, 89.4978f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(102, 114),
                    positionOf(1112.3754f, 1747.8737f, 10.6923f, 270.9278f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(97, 96),
                    positionOf(1641.6802f, 1299.2113f, 10.6869f, 271.4891f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(90, 96),
                    positionOf(2135.8757f, 1408.4512f, 10.6867f, 180.4562f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(99, 81),
                    positionOf(2262.2639f, 1469.2202f, 14.9177f, 91.1919f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(114, 1),
                    positionOf(2461.738f, 1345.5385f, 10.6975f, 0.9317f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(88, 64),
                    positionOf(2804.4365f, 1332.5348f, 10.6283f, 271.7682f),
                    300
            )
            createVehicle(
                    VehicleModel[560],
                    vehicleColorsOf(17, 1),
                    positionOf(2805.1685f, 1361.4004f, 10.4548f, 270.234f),
                    300
            )
            createVehicle(
                    VehicleModel[506],
                    vehicleColorsOf(7, 7),
                    positionOf(2853.5378f, 1361.4677f, 10.5149f, 269.6648f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(93, 64),
                    positionOf(2633.9832f, 2205.7061f, 10.6868f, 180.0076f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(93, 64),
                    positionOf(2119.9751f, 2049.3127f, 10.5423f, 180.1963f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(93, 64),
                    positionOf(2785.0261f, -1835.0374f, 9.6874f, 226.9852f),
                    300
            )
            createVehicle(
                    VehicleModel[567],
                    vehicleColorsOf(99, 81),
                    positionOf(2787.8975f, -1876.2583f, 9.6966f, 0.5804f),
                    300
            )
            createVehicle(
                    VehicleModel[411],
                    vehicleColorsOf(116, 1),
                    positionOf(2771.2993f, -1841.562f, 9.487f, 20.7678f),
                    300
            )
            createVehicle(
                    VehicleModel[420],
                    vehicleColorsOf(6, 1),
                    positionOf(1713.9319f, 1467.8354f, 10.5219f, 342.8006f),
                    300
            )
        }
    }

    override fun getModules(): List<Module> = listOf(HooksModule())

    override fun getPlugins(): List<Plugin> = emptyList()
}