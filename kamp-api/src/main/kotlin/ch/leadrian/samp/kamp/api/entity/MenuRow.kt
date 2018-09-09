package ch.leadrian.samp.kamp.api.entity

interface MenuRow {

    val menu: Menu

    val index: Int

    fun disable()

    fun getText(column: Int): String?

    fun onSelected(onSelected: MenuRow.(Player) -> Unit)

}