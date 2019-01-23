package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.GameMode
import java.util.Properties

internal object GameModeLoader {

    private const val GAME_MODE_CLASS_PROPERTY_KEY = "kamp.gamemode.class.name"

    fun load(configProperties: Properties): GameMode {
        val gameModeClassName = configProperties.getProperty(GAME_MODE_CLASS_PROPERTY_KEY)
                ?: throw IllegalStateException("Could not find required property $GAME_MODE_CLASS_PROPERTY_KEY")
        val gameModeClass = Class.forName(gameModeClassName)
        return gameModeClass.newInstance() as GameMode
    }

}