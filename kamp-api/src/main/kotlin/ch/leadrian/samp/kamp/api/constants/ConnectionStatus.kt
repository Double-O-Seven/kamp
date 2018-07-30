package ch.leadrian.samp.kamp.api.constants

enum class ConnectionStatus(override val value: Int) : ConstantValue<Int> {
    NO_ACTION(0),
    DISCONNECT_ASAP(1),
    DISCONNECT_ASAP_SILENTLY(2),
    DISCONNECT_ON_NO_ACK(3),
    REQUESTED_CONNECTION(4),
    HANDLING_CONNECTION_REQUEST(5),
    UNVERIFIED_SENDER(6),
    SET_ENCRYPTION_ON_MULTIPLE_16_BYTE_PACKET(7),
    CONNECTED(8);

    companion object : ConstantValueRegistry<Int, ConnectionStatus>(*ConnectionStatus.values())
}