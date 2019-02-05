package ch.leadrian.samp.kamp.examples.streamertest

import ch.leadrian.samp.kamp.core.api.constants.GameTextStyle
import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.text.GameTextSender
import ch.leadrian.samp.kamp.streamer.api.callback.onEnter
import ch.leadrian.samp.kamp.streamer.api.callback.onLeave
import ch.leadrian.samp.kamp.streamer.api.service.StreamableCheckpointService
import ch.leadrian.samp.kamp.streamer.api.service.StreamableTextLabelService
import javax.annotation.PostConstruct
import javax.inject.Singleton

@Singleton
internal class MoneyZoneLoader(
        private val streamableCheckpointService: StreamableCheckpointService,
        private val streamableTextLabelService: StreamableTextLabelService,
        private val gameTextSender: GameTextSender
) {

    private val moneyZones: List<MoneyZone> = listOf(
            MoneyZone(vector3DOf(2000.3132f, 1538.6012f, 13.5859f), 0),
            MoneyZone(vector3DOf(1536.4822f, -1364.2263f, 329.4609f), 0),
            MoneyZone(vector3DOf(363.4277f, 173.7762f, 1008.3828f), 3),
            MoneyZone(vector3DOf(2498.1282f, -1752.4781f, 13.4492f), 0),
            MoneyZone(vector3DOf(1004.6713f, -1855.2542f, 12.8146f), 0),
            MoneyZone(vector3DOf(-2345.5991f, -364.7135f, 68.5775f), 0),
            MoneyZone(vector3DOf(-1637.9098f, 1417.8621f, 7.1875f), 0)
    )

    @PostConstruct
    fun loadMoneyZones() {
        moneyZones.forEach { moneyZone ->
            val streamableCheckpoint = streamableCheckpointService.createStreamableCheckpoint(
                    coordinates = moneyZone.coordinates,
                    size = 5f,
                    interiorIds = mutableSetOf(moneyZone.interiorId)
            )
            streamableCheckpoint.onEnter { player ->
                gameTextSender.sendGameTextToPlayer(
                        player,
                        GameTextStyle.PRICEDOWN_BOTTOM_RIGHT_8_SECONDS,
                        8,
                        "${TextDrawCodes.GREEN}+$250"
                )
                player.giveMoney(250)
            }
            streamableCheckpoint.onLeave { player ->
                gameTextSender.sendGameTextToPlayer(player, GameTextStyle.BANK_GOTHIC_CENTER_2, 3, "Goodbye!")
            }
        }
    }

    private data class MoneyZone(val coordinates: Vector3D, val interiorId: Int)

}