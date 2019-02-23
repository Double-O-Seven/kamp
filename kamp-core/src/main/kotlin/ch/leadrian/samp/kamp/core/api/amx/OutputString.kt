package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.StringEncoding
import ch.leadrian.samp.kamp.core.runtime.amx.nullTerminated
import ch.leadrian.samp.kamp.core.runtime.amx.removeTrailingZeroes

/**
 * Wrapper for [String] output parameters passed to sampgdk_InvokeNative.
 *
 * Example usage in GetPlayerName:
 * ```kotlin
 * val GetPlayerName by AmxNativeFunction3<Int, OutputString, Int>()
 * ```
 *
 * @see [AmxNativeFunction]
 */
class OutputString(internal val bytes: ByteArray) {

    constructor(value: String) : this(value.toByteArray(StringEncoding.getCharset()).nullTerminated())

    constructor(size: Int) : this(ByteArray(size))

    /**
     * @returns the [String] value represented by the wrapped [ByteArray]. Any trailing zeroes are removed.
     */
    val value: String
        get() = String(bytes.removeTrailingZeroes(), StringEncoding.getCharset())

    /**
     * @returns the maximum number of available bytes to represent an output [String].
     */
    val size: Int = bytes.size

}