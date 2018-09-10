package ch.leadrian.samp.kamp.core.api.constants

enum class ObjectMaterialTextAlignment(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    LEFT(SAMPConstants.OBJECT_MATERIAL_TEXT_ALIGN_LEFT),
    RIGHT(SAMPConstants.OBJECT_MATERIAL_TEXT_ALIGN_RIGHT),
    CENTER(SAMPConstants.OBJECT_MATERIAL_TEXT_ALIGN_CENTER);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, ObjectMaterialTextAlignment>(*ObjectMaterialTextAlignment.values())
}