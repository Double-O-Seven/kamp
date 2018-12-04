package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.Plugin
import com.google.inject.Module

class ViewPlugin : Plugin() {

    override fun getModules(): List<Module> = listOf(ViewModule())

}