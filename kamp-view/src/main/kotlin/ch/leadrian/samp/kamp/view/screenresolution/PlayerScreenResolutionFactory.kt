package ch.leadrian.samp.kamp.view.screenresolution

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionFactory
import javax.inject.Inject
import kotlin.reflect.KClass

internal class PlayerScreenResolutionFactory
@Inject
constructor(
        private val screenResolutionDialogProvider: ScreenResolutionDialogProvider
) : EntityExtensionFactory<Player, PlayerScreenResolution> {

    override val extensionClass: KClass<PlayerScreenResolution> = PlayerScreenResolution::class

    override fun create(entity: Player): PlayerScreenResolution = PlayerScreenResolution(
            entity,
            screenResolutionDialogProvider
    )

}