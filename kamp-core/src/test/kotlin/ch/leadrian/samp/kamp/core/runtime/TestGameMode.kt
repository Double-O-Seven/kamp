package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.GameMode
import ch.leadrian.samp.kamp.core.api.Plugin
import com.google.inject.Module
import com.netflix.governator.annotations.Configuration
import javax.inject.Inject

@Suppress("unused")
class TestGameMode : GameMode() {

    @Configuration("test.value.foo")
    var foo: String? = null

    @Inject
    var fooService: FooService? = null

    override fun getModules(): List<Module> = emptyList()

    override fun getPlugins(): List<Plugin> = listOf(FooPlugin(), BarPlugin())

}