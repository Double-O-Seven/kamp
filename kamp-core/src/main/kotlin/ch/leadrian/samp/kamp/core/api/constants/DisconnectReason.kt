package ch.leadrian.samp.kamp.core.api.constants

enum class DisconnectReason(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    TIMEOUT(0),
    QUIT(1),
    KICK(2);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, DisconnectReason>(*DisconnectReason.values())
}