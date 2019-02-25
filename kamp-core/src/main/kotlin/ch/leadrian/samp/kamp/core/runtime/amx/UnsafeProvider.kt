package ch.leadrian.samp.kamp.core.runtime.amx

import sun.misc.Unsafe

object UnsafeProvider {

    val instance: Unsafe by lazy {
        with(Unsafe::class.java.getDeclaredField("theUnsafe")) {
            isAccessible = true
            val theUnsafe = get(null) as Unsafe
            isAccessible = false
            theUnsafe
        }
    }

}