package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.Plugin
import com.google.inject.Module
import com.netflix.governator.annotations.Configuration

@Suppress("unused")
class FooPlugin : Plugin() {

    @Configuration("test.value.bar")
    var bar: Int = 0

    override fun getModules(): List<Module> = emptyList()

}