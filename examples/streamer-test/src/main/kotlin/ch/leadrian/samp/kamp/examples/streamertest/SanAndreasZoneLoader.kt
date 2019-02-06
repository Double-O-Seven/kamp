package ch.leadrian.samp.kamp.examples.streamertest

import ch.leadrian.samp.kamp.core.api.constants.GameTextStyle
import ch.leadrian.samp.kamp.core.api.constants.SanAndreasZone
import ch.leadrian.samp.kamp.core.api.text.GameTextSender
import ch.leadrian.samp.kamp.streamer.api.callback.onEnter
import ch.leadrian.samp.kamp.streamer.api.service.StreamableAreaService
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SanAndreasZoneLoader
@Inject
constructor(
        private val streamableAreaService: StreamableAreaService,
        private val gameTextSender: GameTextSender
) {

    @PostConstruct
    fun loadSanAndreasZones() {
        SanAndreasZone.allZones.filter { !it.isMainZone }.forEach { sanAndreasZone ->
            val streamableArea = streamableAreaService.createStreamableBox(sanAndreasZone.area3D)
            streamableArea.onEnter { player ->
                gameTextSender.sendGameTextToPlayer(
                        player,
                        GameTextStyle.PRICEDOWN_BOTTOM_RIGHT_8_SECONDS,
                        8,
                        sanAndreasZone.textKey
                )
            }
        }
    }

}