package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.view.factory.DefaultViewFactory
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.navigation.ViewNavigationFactory
import ch.leadrian.samp.kamp.view.navigation.ViewNavigator
import ch.leadrian.samp.kamp.view.screenresolution.PlayerScreenResolutionFactory

internal class ViewModule : KampModule() {

    override fun configure() {
        bind(ViewFactory::class.java).to(DefaultViewFactory::class.java)
        bind(ViewNavigator::class.java).asEagerSingleton()
        newPlayerExtensionFactorySetBinder().apply {
            addBinding().to(ViewNavigationFactory::class.java)
            addBinding().to(PlayerScreenResolutionFactory::class.java)
        }
    }

}