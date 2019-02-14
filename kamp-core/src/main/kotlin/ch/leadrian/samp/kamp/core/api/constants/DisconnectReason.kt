package ch.leadrian.samp.kamp.core.api.constants

enum class DisconnectReason(override val value: Int) : ConstantValue<Int> {
    TIMEOUT(0),
    QUIT(1),
    KICK(2);

    companion object : ConstantValueRegistry<Int, DisconnectReason>(DisconnectReason.values())
}