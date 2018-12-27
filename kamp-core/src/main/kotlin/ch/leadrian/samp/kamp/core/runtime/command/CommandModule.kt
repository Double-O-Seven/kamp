package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.DefaultCommandAccessDeniedHandler
import ch.leadrian.samp.kamp.core.api.command.DefaultCommandErrorHandler
import ch.leadrian.samp.kamp.core.api.command.DefaultInvalidCommandParameterValueHandler
import ch.leadrian.samp.kamp.core.api.command.DefaultUnknownCommandHandler
import ch.leadrian.samp.kamp.core.api.inject.KampModule

internal class CommandModule : KampModule() {

    override fun configure() {
        bind(CommandProcessor::class.java).asEagerSingleton()
        bind(CommandParser::class.java)
        bind(CommandRegistry::class.java)
        bind(CommandAccessCheckExecutor::class.java)
        bind(CommandParametersResolver::class.java)
        bind(CommandMethodInvoker::class.java)
        bind(CommandParameterResolverRegistry::class.java)
        bind(CommandParameterResolverFactory::class.java)
        bind(CommandDefinitionLoader::class.java)
        bind(DefaultUnknownCommandHandler::class.java)
        bind(DefaultCommandErrorHandler::class.java)
        bind(DefaultCommandAccessDeniedHandler::class.java)
        bind(DefaultInvalidCommandParameterValueHandler::class.java)
        newCommandsSetBinder() // required in order to have at least an empty binding
        newCommandParameterResolverSetBinder().apply {
            addBinding().to(ActorCommandParameterResolver::class.java)
            addBinding().to(DoubleCommandParameterResolver::class.java)
            addBinding().to(FloatCommandParameterResolver::class.java)
            addBinding().to(GangZoneCommandParameterResolver::class.java)
            addBinding().to(IntCommandParameterResolver::class.java)
            addBinding().to(LongCommandParameterResolver::class.java)
            addBinding().to(MapObjectCommandParameterResolver::class.java)
            addBinding().to(MenuCommandParameterResolver::class.java)
            addBinding().to(PickupCommandParameterResolver::class.java)
            addBinding().to(PlayerCommandParameterResolver::class.java)
            addBinding().to(PrimitiveDoubleCommandParameterResolver::class.java)
            addBinding().to(PrimitiveFloatCommandParameterResolver::class.java)
            addBinding().to(PrimitiveIntCommandParameterResolver::class.java)
            addBinding().to(PrimitiveLongCommandParameterResolver::class.java)
            addBinding().to(StringCommandParameterResolver::class.java)
            addBinding().to(TextDrawCommandParameterResolver::class.java)
            addBinding().to(TextLabelCommandParameterResolver::class.java)
            addBinding().to(VehicleCommandParameterResolver::class.java)
            addBinding().to(VehicleModelCommandParameterResolver::class.java)
            addBinding().to(WeaponModelCommandParameterResolver::class.java)
            addBinding().to(SkinModelCommandParameterResolver::class.java)
        }
    }

}