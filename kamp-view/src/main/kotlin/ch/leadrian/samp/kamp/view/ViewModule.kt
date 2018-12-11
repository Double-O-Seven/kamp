package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.view.factory.DefaultViewFactory
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.AbsoluteViewDimensionsCalculator
import ch.leadrian.samp.kamp.view.layout.ViewAreaCalculator
import ch.leadrian.samp.kamp.view.layout.ViewLayoutCalculator
import ch.leadrian.samp.kamp.view.navigation.ViewNavigationElementFactory
import ch.leadrian.samp.kamp.view.navigation.ViewNavigationFactory
import ch.leadrian.samp.kamp.view.navigation.ViewNavigator

internal class ViewModule : KampModule() {

    override fun configure() {
        bind(ViewContext::class.java)
        bind(ViewLayoutCalculator::class.java)
        bind(ViewAreaCalculator::class.java)
        bind(AbsoluteViewDimensionsCalculator::class.java)
        bind(ViewFactory::class.java).to(DefaultViewFactory::class.java)
        bind(ViewNavigationElementFactory::class.java)
        bind(ViewNavigator::class.java).asEagerSingleton()
        newPlayerExtensionFactorySetBinder().apply {
            addBinding().to(ViewNavigationFactory::class.java)
            addBinding().to(ScreenResolutionFactory::class.java)
        }
    }

}