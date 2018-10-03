package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.Plugin
import com.google.inject.Module
import javax.inject.Inject

@Suppress("unused")
class BarPlugin : Plugin() {

    @Inject
    var barService: BarService? = null

    override fun getModules(): List<Module> = emptyList()

}