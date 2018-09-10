package ch.leadrian.samp.kamp.core.api.constants

enum class SelectObjectMode(override val value: Int) : ConstantValue<Int> {
    GLOBAL_OBJECT(SAMPConstants.SELECT_OBJECT_GLOBAL_OBJECT),
    PLAYER_OBJECT(SAMPConstants.SELECT_OBJECT_PLAYER_OBJECT);

    companion object : ConstantValueRegistry<Int, SelectObjectMode>(*SelectObjectMode.values())

}
