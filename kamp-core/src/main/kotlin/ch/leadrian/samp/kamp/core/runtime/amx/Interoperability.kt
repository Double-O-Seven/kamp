package ch.leadrian.samp.kamp.core.runtime.amx

internal fun ByteArray.nullTerminated(): ByteArray = copyOf(size + 1).also { it[size] = 0 }

internal fun ByteArray.removeTrailingZeroes(): ByteArray {
    val nullIndex = indexOfFirst { it == 0.toByte() }
    return when {
        nullIndex >= 0 -> copyOf(nullIndex)
        else -> this
    }
}
