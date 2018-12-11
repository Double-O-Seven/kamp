package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionFactory
import javax.inject.Inject
import kotlin.reflect.KClass

internal class ScreenResolutionFactory
@Inject
constructor() : EntityExtensionFactory<Player, ScreenResolution> {

    override val extensionClass: KClass<ScreenResolution> = ScreenResolution::class

    override fun create(entity: Player): ScreenResolution = ScreenResolution()

}