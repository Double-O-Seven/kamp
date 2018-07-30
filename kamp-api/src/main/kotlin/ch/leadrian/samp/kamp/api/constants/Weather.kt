package ch.leadrian.samp.kamp.api.constants

/**
 * Taken from https://github.com/Shoebill/shoebill-api
 */
enum class Weather(override val value: Int) : ConstantValue<Int> {
    EXTRA_SUNNY_LA(0),
    SUNNY_LA(1),
    EXTRA_SUNNY_SMOG_LA(2),
    SUNNY_SMOG_LA(3),
    CLOUDY_LA(4),
    SUNNY_SF(5),
    EXTRA_SUNNY_SF(6),
    CLOUDY_SF(7),
    RAINY_SF(8),
    FOGGY_SF(9),
    SUNNY_VEGAS(10),
    EXTRA_SUNNY_VEGAS(11),
    CLOUDY_VEGAS(12),
    EXTRA_SUNNY_COUNTRYSIDE(13),
    SUNNY_COUNTRYSIDE(14),
    CLOUDY_COUNTRYSIDE(15),
    RAINY_COUNTRYSIDE(16),
    EXTRA_SUNNY_DESERT(17),
    SUNNY_DESERT(18),
    SANDSTORM_DESERT(19),
    UNDERWATER(20);

    companion object : ConstantValueRegistry<Int, Weather>(*Weather.values())
}