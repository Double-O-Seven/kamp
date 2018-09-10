package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerNetworkStatistics
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString

internal class PlayerNetworkStatisticsImpl(
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : PlayerNetworkStatistics {

    override val connectedTime: Int
        get() = nativeFunctionExecutor.netStats_GetConnectedTime(player.id.value)

    override val messagesReceived: Int
        get() = nativeFunctionExecutor.netStats_MessagesReceived(player.id.value)

    override val bytesReceived: Int
        get() = nativeFunctionExecutor.netStats_BytesReceived(player.id.value)

    override val messagesSent: Int
        get() = nativeFunctionExecutor.netStats_MessagesSent(player.id.value)

    override val bytesSent: Int
        get() = nativeFunctionExecutor.netStats_BytesSent(player.id.value)

    override val messagesReceivedPerSecond: Int
        get() = nativeFunctionExecutor.netStats_MessagesRecvPerSecond(player.id.value)

    override val packetLossPercentage: Float
        get() = nativeFunctionExecutor.netStats_PacketLossPercent(player.id.value)

    override val connectionStatus: ch.leadrian.samp.kamp.core.api.constants.ConnectionStatus
        get() = nativeFunctionExecutor.netStats_ConnectionStatus(player.id.value).let { ch.leadrian.samp.kamp.core.api.constants.ConnectionStatus[it] }

    override val ipAndPort: String
        get() {
            val ipAndPort = ReferenceString()
            nativeFunctionExecutor.netStats_GetIpPort(playerid = player.id.value, ip_port = ipAndPort, ip_port_len = 21)
            return ipAndPort.value ?: ""
        }

    override val summaryString: String
        get() {
            val networkStatisticsString = ReferenceString()
            nativeFunctionExecutor.getPlayerNetworkStats(playerid = player.id.value, retstr = networkStatisticsString, size = 400)
            return networkStatisticsString.value ?: ""
        }
}