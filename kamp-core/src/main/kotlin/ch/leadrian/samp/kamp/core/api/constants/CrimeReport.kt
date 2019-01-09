package ch.leadrian.samp.kamp.core.api.constants

enum class CrimeReport(
        override val value: Int,
        val description: String
) : ConstantValue<Int> {

    CODE_10_7_A(21, "Out of service"),
    CODE_10_7_B(22, "Out of service"),
    CODE_10_17(10, "Meet Complainant"),
    CODE_10_21_A(7, "Call () by phone"),
    CODE_10_21_B(8, "Call () by phone"),
    CODE_10_21_C(9, "Call () by phone"),
    CODE_10_24(6, "Assignment Completed"),
    CODE_10_28_A(13, "Vehicle registration information"),
    CODE_10_28_B(15, "Vehicle registration information"),
    CODE_10_34(17, "Riot"),
    CODE_10_37(18, "(Investigate) suspicious vehicle"),
    CODE_10_47(4, "Emergency road repairs needed"),
    CODE_10_71(3, "Advise nature of fire (area, type, contents of the building)"),
    CODE_10_81_A(5, "Breathalyzer"),
    CODE_10_81_B(11, "Breathalyzer"),
    CODE_10_81_C(14, "Breathalyzer"),
    CODE_10_81_D(19, "Breathalyzer"),
    CODE_10_91_A(12, "Pick up prisoner/subject"),
    CODE_10_91_B(16, "Pick up prisoner/subject");

    companion object : ConstantValueRegistry<Int, CrimeReport>(*CrimeReport.values())

}