package ch.leadrian.samp.kamp.common.neon

import ch.leadrian.samp.kamp.core.api.base.HasModelId

enum class NeonColor(override val modelId: Int) : HasModelId {

    RED(18647),
    BLUE(18648),
    GREEN(18649),
    YELLOW(18650),
    PINK(18651),
    WHITE(18652)

}