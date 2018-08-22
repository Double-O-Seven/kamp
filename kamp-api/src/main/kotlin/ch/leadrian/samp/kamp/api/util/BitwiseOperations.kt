@file:kotlin.jvm.JvmName("BitwiseOperations")

package ch.leadrian.samp.kamp.api.util

fun Int.getByte(position: Int): Int = (this shr (position * 4)) and 0x0F

fun Int.setByte(position: Int, value: Int): Int =
        (this and (0x0F shl (position * 4)).inv()) or ((value and 0x0F) shl (position * 4))

fun Int.getBit(position: Int): Int = (this shr position) and 1

fun Int.setBit(position: Int, value: Int): Int =
        (this and (1 shl position).inv()) or ((value and 1) shl position)