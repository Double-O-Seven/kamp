package ch.leadrian.samp.kamp.core.runtime.inject

import com.google.inject.Injector
import com.google.inject.Module
import com.netflix.governator.guice.BootstrapModule
import com.netflix.governator.guice.LifecycleInjector

internal object InjectorFactory {

    fun createInjector(basePackages: Set<String>, bootstrapModule: BootstrapModule, vararg modules: Module): Injector =
            LifecycleInjector
                    .builder()
                    .withModules(*modules)
                    .usingBasePackages(basePackages)
                    .withBootstrapModule(bootstrapModule)
                    .build()
                    .createInjector()

}