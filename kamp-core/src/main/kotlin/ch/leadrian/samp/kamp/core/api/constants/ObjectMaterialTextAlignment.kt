package ch.leadrian.samp.kamp.core.api.constants

enum class ObjectMaterialTextAlignment(override val value: Int) : ConstantValue<Int> {
    LEFT(SAMPConstants.OBJECT_MATERIAL_TEXT_ALIGN_LEFT),
    RIGHT(SAMPConstants.OBJECT_MATERIAL_TEXT_ALIGN_RIGHT),
    CENTER(SAMPConstants.OBJECT_MATERIAL_TEXT_ALIGN_CENTER);

    companion object : ConstantValueRegistry<Int, ObjectMaterialTextAlignment>(*ObjectMaterialTextAlignment.values())
}