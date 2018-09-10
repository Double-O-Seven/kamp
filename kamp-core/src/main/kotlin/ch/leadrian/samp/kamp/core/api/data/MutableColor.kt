package ch.leadrian.samp.kamp.core.api.data

interface MutableColor : Color {

    override var value: Int

    override var r: Int

    override var g: Int

    override var b: Int

    override var a: Int

    override var rgb: Int
}