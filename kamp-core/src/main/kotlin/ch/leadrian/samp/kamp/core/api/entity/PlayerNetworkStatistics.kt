package ch.leadrian.samp.kamp.core.api.entity

interface PlayerNetworkStatistics : HasPlayer {

    val connectedTime: Int

    val messagesReceived: Int

    val bytesReceived: Int

    val messagesSent: Int

    val bytesSent: Int

    val messagesReceivedPerSecond: Int

    val packetLossPercentage: Float

    val connectionStatus: ch.leadrian.samp.kamp.core.api.constants.ConnectionStatus

    val ipAndPort: String

    val summaryString: String
}