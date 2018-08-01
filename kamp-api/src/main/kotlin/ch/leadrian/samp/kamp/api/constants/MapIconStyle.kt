package ch.leadrian.samp.kamp.api.constants

enum class MapIconStyle(override val value: Int) : ConstantValue<Int> {
    LOCAL(SAMPConstants.MAPICON_LOCAL),
    GLOBAL(SAMPConstants.MAPICON_GLOBAL),
    LOCAL_CHECKPOINT(SAMPConstants.MAPICON_LOCAL_CHECKPOINT),
    GLOBAL_CHECKPOINT(SAMPConstants.MAPICON_GLOBAL_CHECKPOINT);

    companion object : ConstantValueRegistry<Int, MapIconStyle>(*MapIconStyle.values())

}
