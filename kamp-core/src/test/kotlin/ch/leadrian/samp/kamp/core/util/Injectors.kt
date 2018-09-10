package ch.leadrian.samp.kamp.core.util

import com.google.inject.Injector

inline fun <reified T : Any> Injector.getInstance(): T = this.getInstance(T::class.java)
