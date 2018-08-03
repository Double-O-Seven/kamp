package ch.leadrian.samp.kamp.api.constants

enum class MenuColumn(override val value: Int) : ConstantValue<Int> {
    FIRST(0),
    SECOND(1);

    companion object : ConstantValueRegistry<Int, MenuColumn>(*MenuColumn.values())

}