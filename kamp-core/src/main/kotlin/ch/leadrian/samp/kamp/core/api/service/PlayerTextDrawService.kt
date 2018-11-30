package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextDrawId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerTextDrawFactory
import javax.inject.Inject

class PlayerTextDrawService
@Inject
internal constructor(
        private val playerTextDrawFactory: PlayerTextDrawFactory,
        private val textProvider: TextProvider
) {

    fun createPlayerTextDraw(
            player: Player,
            text: String,
            position: Vector2D
    ): PlayerTextDraw = playerTextDrawFactory.create(
            player = player,
            text = text,
            position = position
    )

    fun createPlayerTextDraw(
            player: Player,
            textKey: TextKey,
            position: Vector2D
    ): PlayerTextDraw {
        val text = textProvider.getText(player.locale, textKey)
        return playerTextDrawFactory.create(
                player = player,
                text = text,
                position = position
        )
    }

    fun isValidPlayerTextDraw(player: Player, playerTextDrawId: PlayerTextDrawId): Boolean =
            player.playerTextDrawRegistry[playerTextDrawId] != null

    fun getPlayerTextDraw(player: Player, playerTextDrawId: PlayerTextDrawId): PlayerTextDraw =
            player.playerTextDrawRegistry[playerTextDrawId] ?: throw NoSuchEntityException(
                    "No player text draw for player ID ${player.id.value} and ID ${playerTextDrawId.value}"
            )

    fun getAllPlayerTextDraws(player: Player): List<PlayerTextDraw> = player.playerTextDrawRegistry.getAll()

}