package ch.leadrian.samp.kamp.core.api.entity.extension

interface Extendable<T : Any> {

    val extensions: EntityExtensionContainer<T>
}