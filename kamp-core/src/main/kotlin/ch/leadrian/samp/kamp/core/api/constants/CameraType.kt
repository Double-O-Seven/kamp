package ch.leadrian.samp.kamp.core.api.constants

enum class CameraType(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    CUT(SAMPConstants.CAMERA_CUT),
    MOVE(SAMPConstants.CAMERA_MOVE);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, CameraType>(*CameraType.values())

}
