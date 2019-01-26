package ch.leadrian.samp.kamp.core.api.constants

enum class DownloadRequestType(override val value: Int) : ConstantValue<Int> {
    EMPTY(SAMPConstants.DOWNLOAD_REQUEST_EMPTY),
    MODEL_FILE(SAMPConstants.DOWNLOAD_REQUEST_MODEL_FILE),
    TEXTURE_FILE(SAMPConstants.DOWNLOAD_REQUEST_TEXTURE_FILE);

    companion object : ConstantValueRegistry<Int, DownloadRequestType>(*DownloadRequestType.values())
}