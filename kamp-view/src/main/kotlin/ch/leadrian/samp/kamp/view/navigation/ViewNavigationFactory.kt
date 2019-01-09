package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionFactory
import javax.inject.Inject
import kotlin.reflect.KClass

internal class ViewNavigationFactory
@Inject
constructor(private val viewNavigationElementFactory: ViewNavigationElementFactory) :
        EntityExtensionFactory<Player, ViewNavigation> {

    override val extensionClass: KClass<ViewNavigation> = ViewNavigation::class

    override fun create(entity: Player): ViewNavigation = ViewNavigation(viewNavigationElementFactory)

}