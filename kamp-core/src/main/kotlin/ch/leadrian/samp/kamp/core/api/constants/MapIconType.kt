package ch.leadrian.samp.kamp.core.api.constants

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import java.util.Collections.unmodifiableList

/**
 * Default coordinates taken from https://forum.sa-mp.com/showthread.php?t=197489.
 */
@Suppress("SpellCheckingInspection", "unused")
enum class MapIconType(override val value: Int, vararg defaultCoordinates: Vector3D) : ConstantValue<Int> {

    COLORED(0),
    WHITE_SQUARE(1),
    PLAYER_POSITION(2),
    PLAYER_MENU_MAP(3),
    NORTH(4),
    AIR_YARD(5),
    AMMU_NATION(
            6,
            vector3DOf(1372.9f, -1278.8f, 12.5f),
            vector3DOf(-2626.6f, 209.4f, 4.9f),
            vector3DOf(2535.9f, 2083.5f, 10.8f),
            vector3DOf(2156.5f, 943.2f, 10.8f),
            vector3DOf(779.7f, 1874.3f, 4.9f),
            vector3DOf(-2092.7f, -2463.8f, 30.6f),
            vector3DOf(240.0f, -178.2f, 2.0f),
            vector3DOf(-1509.4f, 2611.8f, 58.5f),
            vector3DOf(-315.67f, 829.87f, 13.43f),
            vector3DOf(2332.9f, 63.6f, 31.0f)
    ),
    BARBER(
            7,
            vector3DOf(822.6f, -1590.3f, 13.5f),
            vector3DOf(-2570.1f, 245.4f, 10.3f),
            vector3DOf(2726.6f, -2026.4f, 17.5f),
            vector3DOf(2080.3f, 2119.0f, 10.8f),
            vector3DOf(675.7f, -496.6f, 16.8f)
    ),
    BIG_SMOKE(8),
    BOAT_YARD(9),
    BURGER_SHOT(
            10,
            vector3DOf(812.9f, -1616.1f, 13.6f),
            vector3DOf(1199.1f, -924.0f, 43.3f),
            vector3DOf(2362.2f, 2069.9f, 10.8f),
            vector3DOf(2469.5f, 2033.8f, 10.8f),
            vector3DOf(2172.9f, 2795.7f, 10.8f),
            vector3DOf(1875.3f, 2072.0f, 10.8f),
            vector3DOf(1161.5f, 2072.0f, 10.8f),
            vector3DOf(-2356.0f, 1009.0f, 49.0f),
            vector3DOf(-1913.3f, 826.2f, 36.9f),
            vector3DOf(-2335.6f, -165.6f, 39.5f)
    ),
    QUARRY(11),
    CATALINA(12),
    CESAR(13),
    CLUCKIN_BELL(
            14,
            vector3DOf(2397.8f, -1895.6f, 13.7f),
            vector3DOf(2421.6f, -1509.6f, 24.1f),
            vector3DOf(-2671.6f, 257.4f, 4.6f),
            vector3DOf(2392.4f, 2046.5f, 10.8f),
            vector3DOf(2844.5f, 2401.1f, 11.0f),
            vector3DOf(2635.5f, 1674.3f, 11.0f),
            vector3DOf(2105.7f, 2228.7f, 11.0f),
            vector3DOf(-2154.0f, -2461.2f, 30.8f),
            vector3DOf(-1816.2f, 620.8f, 37.5f),
            vector3DOf(-1216.0f, 1831.4f, 45.3f),
            vector3DOf(172.73f, 1176.76f, 13.7f),
            vector3DOf(932.0f, -1353.0f, 14.0f)
    ),
    CARL_JOHNSON(15),
    CRASH(16),
    DINER(17),
    EMMET(18, vector3DOf(2447.364f, -1974.496f, 12.5469f)),
    ENEMY_ATTACK(19),
    FIRE_DEPT(20),
    GIRLFRIEND(21),
    HOSPITAL(22),
    LOCO(23),
    MADD_DOGG(24),
    CALIGULAS(25),
    OG_LOC(26),
    MOD_GARAGE(
            27,
            vector3DOf(2644.252f, -2028.246f, 12.5547f),
            vector3DOf(1043.4f, -1025.3f, 34.4f)
    ),
    OG_LOC2(28),
    WELL_STACKED_PIZZA(
            29,
            vector3DOf(-1805.7f, 943.2f, 24.8f),
            vector3DOf(2750.9f, 2470.9f, 11.0f),
            vector3DOf(2351.8f, 2529.0f, 10.8f),
            vector3DOf(2635.5f, 1847.4f, 11.0f),
            vector3DOf(2083.4f, 2221.0f, 11.0f),
            vector3DOf(-1719.1f, 1359.4f, 8.6f),
            vector3DOf(2330.2f, 75.2f, 31.0f),
            vector3DOf(203.2f, -200.4f, 6.5f)
    ),
    POLICE(30),
    PROPERTY_GREEN(31),
    PROPERTY_RED(32),
    RACE_MEDAL(33),
    RYDER(34),
    SAVE_GAME(35),
    SCHOOL(36),
    QUESTION_MARK(37),
    SWEET(38),
    TATTOO(
            39,
            vector3DOf(1971.7f, -2036.6f, 13.5f),
            vector3DOf(2071.6f, -1779.9f, 13.5f),
            vector3DOf(2094.6f, 2119.0f, 10.8f),
            vector3DOf(-2490.5f, -40.1f, 39.3f)
    ),
    THE_TRUTH(40),
    WAYPOINT(41),
    TORENO(42),
    TRIADS(43),
    TRIADS_CASINO(44),
    CLOTHES(
            45,
            vector3DOf(-2376.4f, 909.2f, 45.4f),
            vector3DOf(1654.0f, 1733.4f, 11.0f),
            vector3DOf(2105.7f, 2257.4f, 11.0f),
            vector3DOf(-2371.1f, 910.2f, 47.2f),
            vector3DOf(501.7f, -1358.5f, 16.4f),
            vector3DOf(2818.6f, 2401.5f, 11.0f),
            vector3DOf(2112.8f, -1214.7f, 23.9f),
            vector3DOf(2772.0f, 2447.6f, 11.0f),
            vector3DOf(-2489.0f, -26.9f, 32.6f)
    ),
    WOOZIE(46),
    ZERO(47),
    CLUB(48),
    BAR(49),
    RESTAURANT(50),
    TRUCK(51),
    DOLLAR_SIGN(52),
    RACE_FLAG(53),
    GYM(54),
    CAR(55),
    LIGHT(56),
    CLOSEST_AIRPORT(57),
    VARRIOS_LOS_AZTECAS(58),
    BALLAS(59),
    LOS_SANTOS_VAGOS(60),
    SAN_FIERRO_RIFA(61),
    GROVE_STREET(62),
    PAY_N_SPRAY(
            63,
            vector3DOf(2067.4f, -1831.2f, 13.5f),
            vector3DOf(488.0f, -1734.0f, 34.4f),
            vector3DOf(720.016f, -454.625f, 15.328f),
            vector3DOf(-1420.547f, 2583.945f, 58.031f),
            vector3DOf(1966.532f, 2162.65f, 10.995f),
            vector3DOf(-2425.46f, 1020.83f, 49.39f),
            vector3DOf(1021.8f, -1018.7f, 30.9f),
            vector3DOf(-1908.9f, 292.3f, 40.0f),
            vector3DOf(-103.6f, 1112.4f, 18.7f)
    );

    val defaultCoordinates: List<Vector3D> = unmodifiableList(defaultCoordinates.toList())

    companion object : ConstantValueRegistry<Int, MapIconType>(MapIconType.values())
}