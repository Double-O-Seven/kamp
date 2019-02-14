package ch.leadrian.samp.kamp.core.api.constants

@Suppress("unused")
enum class ExplosionType(
        override val value: Int,
        val visible: Boolean,
        val splits: Boolean,
        val createsFire: Boolean,
        val physicalBlast: Boolean,
        val audibleSound: Boolean,
        val range: Range
) : ConstantValue<Int> {

    NORMAL_1(
            value = 0,
            visible = true,
            splits = false,
            createsFire = false,
            physicalBlast = true,
            audibleSound = true,
            range = Range.LARGE
    ),
    NORMAL_2(
            value = 1,
            visible = true,
            splits = false,
            createsFire = true,
            physicalBlast = false,
            audibleSound = true,
            range = Range.NORMAL
    ),
    NORMAL_3(
            value = 2,
            visible = true,
            splits = false,
            createsFire = true,
            physicalBlast = true,
            audibleSound = true,
            range = Range.LARGE
    ),
    NORMAL_4(
            value = 3,
            visible = true,
            splits = false,
            createsFire = true,
            physicalBlast = true,
            audibleSound = true,
            range = Range.LARGE
    ),
    UNUSUAL_SILENT_1(
            value = 4,
            visible = true,
            splits = true,
            createsFire = false,
            physicalBlast = true,
            audibleSound = false,
            range = Range.NORMAL
    ),
    UNUSUAL_SILENT_2(
            value = 5,
            visible = true,
            splits = true,
            createsFire = false,
            physicalBlast = true,
            audibleSound = false,
            range = Range.NORMAL
    ),
    REDDISH_AFTER_GLOW_1(
            value = 6,
            visible = true,
            splits = false,
            createsFire = false,
            physicalBlast = true,
            audibleSound = true,
            range = Range.VERY_LARGE
    ),
    REDDISH_AFTER_GLOW_2(
            value = 7,
            visible = true,
            splits = false,
            createsFire = false,
            physicalBlast = true,
            audibleSound = true,
            range = Range.HUGE
    ),
    INVISIBLE(
            value = 8,
            visible = false,
            splits = false,
            createsFire = false,
            physicalBlast = true,
            audibleSound = true,
            range = Range.NORMAL
    ),
    INVISIBLE_WITH_FIRE(
            value = 9,
            visible = false,
            splits = false,
            createsFire = true,
            physicalBlast = true,
            audibleSound = true,
            range = Range.NORMAL
    ),
    NORMAL_5(
            value = 10,
            visible = true,
            splits = false,
            createsFire = false,
            physicalBlast = true,
            audibleSound = true,
            range = Range.LARGE
    ),
    NORMAL_6(
            value = 11,
            visible = true,
            splits = false,
            createsFire = false,
            physicalBlast = true,
            audibleSound = true,
            range = Range.SMALL
    ),
    REALLY_SMALL(
            value = 12,
            visible = true,
            splits = false,
            createsFire = false,
            physicalBlast = true,
            audibleSound = true,
            range = Range.VERY_SMALL
    ),
    BLACK_BURN_NO_DAMAGE(
            value = 13,
            visible = false,
            splits = false,
            createsFire = false,
            physicalBlast = false,
            audibleSound = false,
            range = Range.LARGE
    );

    enum class Range {
        VERY_SMALL,
        SMALL,
        NORMAL,
        LARGE,
        VERY_LARGE,
        HUGE
    }

    companion object : ConstantValueRegistry<Int, ExplosionType>(ExplosionType.values())
}