package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.StringEncoding

class OutputString(internal val bytes: ByteArray) {

    constructor(value: String) : this(value.toByteArray(StringEncoding.getCharset()))

    constructor(size: Int) : this(ByteArray(size))

    val value: String
        get() = String(bytes, StringEncoding.getCharset())

    val size: Int = bytes.size

}