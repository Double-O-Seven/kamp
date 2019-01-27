package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.Animation
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerDamageStreamableActorReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableActorStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableActorStreamOutReceiver

interface StreamableActor :
        Streamable,
        OnStreamableActorStreamInReceiver,
        OnStreamableActorStreamOutReceiver,
        OnPlayerDamageStreamableActorReceiver {

    val extensions: EntityExtensionContainer<StreamableActor>

    var model: SkinModel

    var coordinates: Vector3D

    var angle: Float

    var position: Position

    val isStreamedIn: Boolean

    var virtualWorldId: Int

    var interiorIds: MutableSet<Int>

    var isInvulnerable: Boolean

    var health: Float

    fun applyAnimation(
            animation: Animation,
            fDelta: Float,
            loop: Boolean,
            lockX: Boolean,
            lockY: Boolean,
            freeze: Boolean,
            time: Int
    )

    fun clearAnimation()
}