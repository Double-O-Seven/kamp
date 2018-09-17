package ch.leadrian.samp.kamp.core.api.inject

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import com.google.inject.multibindings.Multibinder
import com.google.inject.name.Names

abstract class KampModule : AbstractModule() {

    protected fun newCommandParameterResolverSetBinder(): Multibinder<CommandParameterResolver<*>> =
            Multibinder.newSetBinder(binder(), object : TypeLiteral<CommandParameterResolver<*>>() {})

    protected fun newTextProviderResourceBundlePackagesSetBinder(): Multibinder<String> =
            Multibinder.newSetBinder(binder(), String::class.java, Names.named(TextProvider.RESOURCE_BUNDLE_PACKAGES_NAME))

    protected fun newCommandsSetBinder(): Multibinder<Commands> =
            Multibinder.newSetBinder(binder(), Commands::class.java)

}