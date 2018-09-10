package ch.leadrian.samp.kamp.core.api.constants

enum class MapIconStyle(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    LOCAL(SAMPConstants.MAPICON_LOCAL),
    GLOBAL(SAMPConstants.MAPICON_GLOBAL),
    LOCAL_CHECKPOINT(SAMPConstants.MAPICON_LOCAL_CHECKPOINT),
    GLOBAL_CHECKPOINT(SAMPConstants.MAPICON_GLOBAL_CHECKPOINT);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, MapIconStyle>(*MapIconStyle.values())

}
