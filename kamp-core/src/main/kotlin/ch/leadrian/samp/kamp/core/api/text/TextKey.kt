package ch.leadrian.samp.kamp.core.api.text

data class TextKey(val name: String) {

    companion object {

        const val SEPARATOR: String = "."

    }

    fun resolve(vararg segments: String): TextKey {
        return when {
            segments.isEmpty() -> this
            segments.size == 1 -> TextKey(name + SEPARATOR + segments[0])
            else -> TextKey(name + SEPARATOR + segments.joinToString(SEPARATOR))
        }
    }

    @JvmOverloads
    fun <T : Enum<T>> resolve(value: T, toLowerCase: Boolean = true, removeUnderscores: Boolean = true): TextKey {
        var segment = value.name
        if (toLowerCase) {
            segment = segment.toLowerCase()
        }
        if (removeUnderscores) {
            segment = segment.replace("_", "")
        }
        return resolve(segment)
    }

}
