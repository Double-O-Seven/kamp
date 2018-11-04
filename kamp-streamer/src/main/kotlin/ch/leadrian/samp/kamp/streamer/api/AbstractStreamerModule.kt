package ch.leadrian.samp.kamp.streamer.api

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.streamer.runtime.Streamer
import com.google.inject.multibindings.Multibinder

abstract class AbstractStreamerModule : KampModule() {

    protected fun newStreamerSetBinder(): Multibinder<Streamer> =
            Multibinder.newSetBinder(binder(), Streamer::class.java)

}