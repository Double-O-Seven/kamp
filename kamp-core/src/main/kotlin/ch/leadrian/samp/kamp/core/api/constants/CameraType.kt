package ch.leadrian.samp.kamp.core.api.constants

enum class CameraType(override val value: Int) : ConstantValue<Int> {
    CUT(SAMPConstants.CAMERA_CUT),
    MOVE(SAMPConstants.CAMERA_MOVE);

    companion object : ConstantValueRegistry<Int, CameraType>(*CameraType.values())

}
