package ch.leadrian.samp.kamp.examples.amxinteroptest

import ch.leadrian.samp.kamp.core.api.amx.AmxCallbackFactory
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AmxCallbacks
@Inject
constructor(
        private val amxCallbackFactory: AmxCallbackFactory,
        private val messageSender: MessageSender,
        private val playerService: PlayerService,
        private val vehicleService: VehicleService
) {

    @PostConstruct
    fun initialize() {
        amxCallbackFactory.create2<Int, String>("OnPlayerText") { playerId, text ->
            val player = playerService.getPlayer(PlayerId.valueOf(playerId))
            messageSender.sendMessageToPlayer(player, Colors.WHITE, "You said: {0}", text)
            1
        }
        amxCallbackFactory.create3<Int, Int, Int>("OnPlayerEnterVehicle") { playerId, vehicleId, isPassenger ->
            val player = playerService.getPlayer(PlayerId.valueOf(playerId))
            val vehicle = vehicleService.getVehicle(VehicleId.valueOf(vehicleId))
            messageSender.sendMessageToPlayer(player, Colors.WHITE, "Entering {0}...", vehicle.model)
            1
        }
    }

}