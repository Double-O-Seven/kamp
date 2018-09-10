package ch.leadrian.samp.kamp.core.api.data

fun colorOf(value: Long): ch.leadrian.samp.kamp.core.api.data.Color = ch.leadrian.samp.kamp.core.api.data.ColorImpl(value.toInt())

fun colorOf(value: Int): ch.leadrian.samp.kamp.core.api.data.Color = ch.leadrian.samp.kamp.core.api.data.ColorImpl(value)

fun colorOf(r: Int, g: Int, b: Int, a: Int): ch.leadrian.samp.kamp.core.api.data.Color = ch.leadrian.samp.kamp.core.api.data.ColorImpl(rgbaValue(r = r, g = g, b = b, a = a))

fun mutableColorOf(value: Int): MutableColor = MutableColorImpl(value)

fun mutableColorOf(r: Int, g: Int, b: Int, a: Int): MutableColor = MutableColorImpl(rgbaValue(r = r, g = g, b = b, a = a))

private fun rgbaValue(r: Int, g: Int, b: Int, a: Int) =
        ((r and 0xFF) shl 24) or ((g and 0xFF) shl 16) or ((b and 0xFF) shl 8) or (a and 0xFF)

object Colors {

    @JvmStatic
    val GREY = colorOf(0xAFAFAFAA)

    @JvmStatic
    val ACTIVE_BORDER = colorOf(0xB4B4B4FF)

    @JvmStatic
    val ACTIVE_CAPTION = colorOf(0x99B4D1FF)

    @JvmStatic
    val ACTIVE_CAPTION_TEXT = colorOf(0x000000FF)

    @JvmStatic
    val ALICE_BLUE = colorOf(0xF0F8FFFF)

    @JvmStatic
    val ANTIQUE_WHITE = colorOf(0xFAEBD7FF)

    @JvmStatic
    val APP_WORKSPACE = colorOf(0xABABABFF)

    @JvmStatic
    val AQUA = colorOf(0x00FFFFFF)

    @JvmStatic
    val AQUAMARINE = colorOf(0x7FFFD4FF)

    @JvmStatic
    val AZURE = colorOf(0xF0FFFFFF)

    @JvmStatic
    val BEIGE = colorOf(0xF5F5DCFF)

    @JvmStatic
    val BISQUE = colorOf(0xFFE4C4FF)

    @JvmStatic
    val BLACK = colorOf(0x000000FF)

    @JvmStatic
    val BLANCHED_ALMOND = colorOf(0xFFEBCDFF)

    @JvmStatic
    val BLUE = colorOf(0x0000FFFF)

    @JvmStatic
    val BLUE_VIOLET = colorOf(0x8A2BE2FF)

    @JvmStatic
    val BROWN = colorOf(0xA52A2AFF)

    @JvmStatic
    val BURLY_WOOD = colorOf(0xDEB887FF)

    @JvmStatic
    val BUTTON_FACE = colorOf(0xF0F0F0FF)

    @JvmStatic
    val BUTTON_HIGHLIGHT_ = colorOf(0xFFFFFFFF)

    @JvmStatic
    val BUTTON_SHADOW = colorOf(0xA0A0A0FF)

    @JvmStatic
    val CADET_BLUE = colorOf(0x5F9EA0FF)

    @JvmStatic
    val CHARTREUSE = colorOf(0x7FFF00FF)

    @JvmStatic
    val CHOCOLATE = colorOf(0xD2691EFF)

    @JvmStatic
    val CONTROL = colorOf(0xF0F0F0FF)

    @JvmStatic
    val CONTROL_DARK_ = colorOf(0xA0A0A0FF)

    @JvmStatic
    val CONTROL_DARK_DARK_ = colorOf(0x696969FF)

    @JvmStatic
    val CONTROL_LIGHT_ = colorOf(0xE3E3E3FF)

    @JvmStatic
    val CONTROL_LIGHT_LIGHT_ = colorOf(0xFFFFFFFF)

    @JvmStatic
    val CONTROL_TEXT = colorOf(0x000000FF)

    @JvmStatic
    val CORAL = colorOf(0xFF7F50FF)

    @JvmStatic
    val CORNFLOWER_BLUE = colorOf(0x6495EDFF)

    @JvmStatic
    val CORN_SILK = colorOf(0xFFF8DCFF)

    @JvmStatic
    val CRIMSON = colorOf(0xDC143CFF)

    @JvmStatic
    val CYAN = colorOf(0x00FFFFFF)

    @JvmStatic
    val DARK_BLUE = colorOf(0x00008BFF)

    @JvmStatic
    val DARK_CYAN = colorOf(0x008B8BFF)

    @JvmStatic
    val DARK_GOLDENROD = colorOf(0xB8860BFF)

    @JvmStatic
    val DARK_GRAY = colorOf(0xA9A9A9FF)

    @JvmStatic
    val DARK_GREEN = colorOf(0x006400FF)

    @JvmStatic
    val DARK_KHAKI = colorOf(0xBDB76BFF)

    @JvmStatic
    val DARK_MAGENTA = colorOf(0x8B008BFF)

    @JvmStatic
    val DARK_OLIVE_GREEN = colorOf(0x556B2FFF)

    @JvmStatic
    val DARK_ORANGE = colorOf(0xFF8C00FF)

    @JvmStatic
    val DARK_ORCHID = colorOf(0x9932CCFF)

    @JvmStatic
    val DARK_RED = colorOf(0x8B0000FF)

    @JvmStatic
    val DARK_SALMON = colorOf(0xE9967AFF)

    @JvmStatic
    val DARK_SEA_GREEN = colorOf(0x8FBC8BFF)

    @JvmStatic
    val DARK_SLATE_BLUE = colorOf(0x483D8BFF)

    @JvmStatic
    val DARK_SLATE_GRAY = colorOf(0x2F4F4FFF)

    @JvmStatic
    val DARK_TURQUOISE = colorOf(0x00CED1FF)

    @JvmStatic
    val DARK_VIOLET = colorOf(0x9400D3FF)

    @JvmStatic
    val DEEP_PINK = colorOf(0xFF1493FF)

    @JvmStatic
    val DEEP_SKY_BLUE = colorOf(0x00BFFFFF)

    @JvmStatic
    val DESKTOP = colorOf(0x000000FF)

    @JvmStatic
    val DIM_GRAY = colorOf(0x696969FF)

    @JvmStatic
    val DODGER_BLUE = colorOf(0x1E90FFFF)

    @JvmStatic
    val FIREBRICK = colorOf(0xB22222FF)

    @JvmStatic
    val FLORAL_WHITE = colorOf(0xFFFAF0FF)

    @JvmStatic
    val FOREST_GREEN = colorOf(0x228B22FF)

    @JvmStatic
    val FUCHSIA = colorOf(0xFF00FFFF)

    @JvmStatic
    val GAINSBOROUGH = colorOf(0xDCDCDCFF)

    @JvmStatic
    val GHOST_WHITE = colorOf(0xF8F8FFFF)

    @JvmStatic
    val GOLD = colorOf(0xFFD700FF)

    @JvmStatic
    val GOLDENROD = colorOf(0xDAA520FF)

    @JvmStatic
    val GRADIENT_ACTIVE_CAPTION = colorOf(0xB9D1EAFF)

    @JvmStatic
    val GRADIENT_INACTIVE_CAPTION = colorOf(0xD7E4F2FF)

    @JvmStatic
    val GRAY = colorOf(0x808080FF)

    @JvmStatic
    val GRAY_TEXT = colorOf(0x808080FF)

    @JvmStatic
    val GREEN = colorOf(0x008000FF)

    @JvmStatic
    val GREEN_YELLOW = colorOf(0xADFF2FFF)

    @JvmStatic
    val HIGHLIGHT_ = colorOf(0x3399FFFF)

    @JvmStatic
    val HIGHLIGHT_TEXT = colorOf(0xFFFFFFFF)

    @JvmStatic
    val HONEYDEW = colorOf(0xF0FFF0FF)

    @JvmStatic
    val HOT_PINK = colorOf(0xFF69B4FF)

    @JvmStatic
    val HOT_TRACK = colorOf(0x0066CCFF)

    @JvmStatic
    val INACTIVE_BORDER = colorOf(0xF4F7FCFF)

    @JvmStatic
    val INACTIVE_CAPTION = colorOf(0xBFCDDBFF)

    @JvmStatic
    val INACTIVE_CAPTION_TEXT = colorOf(0x434E54FF)

    @JvmStatic
    val INDIAN_RED = colorOf(0xCD5C5CFF)

    @JvmStatic
    val INDIGO = colorOf(0x4B0082FF)

    @JvmStatic
    val INFO = colorOf(0xFFFFE1FF)

    @JvmStatic
    val INFO_TEXT = colorOf(0x000000FF)

    @JvmStatic
    val IVORY = colorOf(0xFFFFF0FF)

    @JvmStatic
    val KHAKI = colorOf(0xF0E68CFF)

    @JvmStatic
    val LAVENDER = colorOf(0xE6E6FAFF)

    @JvmStatic
    val LAVENDER_BLUSH = colorOf(0xFFF0F5FF)

    @JvmStatic
    val LAWN_GREEN = colorOf(0x7CFC00FF)

    @JvmStatic
    val LEMON_CHIFFON = colorOf(0xFFFACDFF)

    @JvmStatic
    val LIGHT_BLUE = colorOf(0xADD8E6FF)

    @JvmStatic
    val LIGHT_CORAL = colorOf(0xF08080FF)

    @JvmStatic
    val LIGHT_CYAN = colorOf(0xE0FFFFFF)

    @JvmStatic
    val LIGHT_GOLDENROD_YELLOW = colorOf(0xFAFAD2FF)

    @JvmStatic
    val LIGHT_GRAY = colorOf(0xD3D3D3FF)

    @JvmStatic
    val LIGHT_GREEN = colorOf(0x90EE90FF)

    @JvmStatic
    val LIGHT_PINK = colorOf(0xFFB6C1FF)

    @JvmStatic
    val LIGHT_SALMON = colorOf(0xFFA07AFF)

    @JvmStatic
    val LIGHT_SEA_GREEN = colorOf(0x20B2AAFF)

    @JvmStatic
    val LIGHT_SKY_BLUE = colorOf(0x87CEFAFF)

    @JvmStatic
    val LIGHT_SLATE_GRAY = colorOf(0x778899FF)

    @JvmStatic
    val LIGHT_STEEL_BLUE = colorOf(0xB0C4DEFF)

    @JvmStatic
    val LIGHT_YELLOW = colorOf(0xFFFFE0FF)

    @JvmStatic
    val LIME = colorOf(0x00FF00FF)

    @JvmStatic
    val LIME_GREEN = colorOf(0x32CD32FF)

    @JvmStatic
    val LINEN = colorOf(0xFAF0E6FF)

    @JvmStatic
    val MAGENTA = colorOf(0xFF00FFFF)

    @JvmStatic
    val MAROON = colorOf(0x800000FF)

    @JvmStatic
    val MEDIUM_AQUAMARINE = colorOf(0x66CDAAFF)

    @JvmStatic
    val MEDIUM_BLUE = colorOf(0x0000CDFF)

    @JvmStatic
    val MEDIUM_ORCHID = colorOf(0xBA55D3FF)

    @JvmStatic
    val MEDIUM_PURPLE = colorOf(0x9370DBFF)

    @JvmStatic
    val MEDIUM_SEA_GREEN = colorOf(0x3CB371FF)

    @JvmStatic
    val MEDIUM_SLATE_BLUE = colorOf(0x7B68EEFF)

    @JvmStatic
    val MEDIUM_SPRING_GREEN = colorOf(0x00FA9AFF)

    @JvmStatic
    val MEDIUM_TURQUOISE = colorOf(0x48D1CCFF)

    @JvmStatic
    val MEDIUM_VIOLET_RED = colorOf(0xC71585FF)

    @JvmStatic
    val MENU = colorOf(0xF0F0F0FF)

    @JvmStatic
    val MENU_BAR = colorOf(0xF0F0F0FF)

    @JvmStatic
    val MENU_HIGHLIGHT_ = colorOf(0x3399FFFF)

    @JvmStatic
    val MENU_TEXT = colorOf(0x000000FF)

    @JvmStatic
    val MIDNIGHT_BLUE = colorOf(0x191970FF)

    @JvmStatic
    val MINT_CREAM = colorOf(0xF5FFFAFF)

    @JvmStatic
    val MISTY_ROSE = colorOf(0xFFE4E1FF)

    @JvmStatic
    val MOCCASIN = colorOf(0xFFE4B5FF)

    @JvmStatic
    val NAVAJO_WHITE = colorOf(0xFFDEADFF)

    @JvmStatic
    val NAVY = colorOf(0x000080FF)

    @JvmStatic
    val OLD_LACE = colorOf(0xFDF5E6FF)

    @JvmStatic
    val OLIVE = colorOf(0x808000FF)

    @JvmStatic
    val OLIVE_DRAB = colorOf(0x6B8E23FF)

    @JvmStatic
    val ORANGE = colorOf(0xFFA500FF)

    @JvmStatic
    val ORANGE_RED = colorOf(0xFF4500FF)

    @JvmStatic
    val ORCHID = colorOf(0xDA70D6FF)

    @JvmStatic
    val PALE_GOLDENROD = colorOf(0xEEE8AAFF)

    @JvmStatic
    val PALE_GREEN = colorOf(0x98FB98FF)

    @JvmStatic
    val PALE_TURQUOISE = colorOf(0xAFEEEEFF)

    @JvmStatic
    val PALE_VIOLET_RED = colorOf(0xDB7093FF)

    @JvmStatic
    val PAPAYA_WHIP = colorOf(0xFFEFD5FF)

    @JvmStatic
    val PEACH_PUFF = colorOf(0xFFDAB9FF)

    @JvmStatic
    val PERU = colorOf(0xCD853FFF)

    @JvmStatic
    val PINK = colorOf(0xFFC0CBFF)

    @JvmStatic
    val PLUM = colorOf(0xDDA0DDFF)

    @JvmStatic
    val POWDER_BLUE = colorOf(0xB0E0E6FF)

    @JvmStatic
    val PURPLE = colorOf(0x800080FF)

    @JvmStatic
    val RED = colorOf(0xFF0000FF)

    @JvmStatic
    val ROSY_BROWN = colorOf(0xBC8F8FFF)

    @JvmStatic
    val ROYAL_BLUE = colorOf(0x4169E1FF)

    @JvmStatic
    val SADDLE_BROWN = colorOf(0x8B4513FF)

    @JvmStatic
    val SALMON = colorOf(0xFA8072FF)

    @JvmStatic
    val SANDY_BROWN = colorOf(0xF4A460FF)

    @JvmStatic
    val SCROLLBAR = colorOf(0xC8C8C8FF)

    @JvmStatic
    val SEA_GREEN = colorOf(0x2E8B57FF)

    @JvmStatic
    val SEASHELL = colorOf(0xFFF5EEFF)

    @JvmStatic
    val SIENNA = colorOf(0xA0522DFF)

    @JvmStatic
    val SILVER = colorOf(0xC0C0C0FF)

    @JvmStatic
    val SKY_BLUE = colorOf(0x87CEEBFF)

    @JvmStatic
    val SLATE_BLUE = colorOf(0x6A5ACDFF)

    @JvmStatic
    val SLATE_GRAY = colorOf(0x708090FF)

    @JvmStatic
    val SNOW = colorOf(0xFFFAFAFF)

    @JvmStatic
    val SPRING_GREEN = colorOf(0x00FF7FFF)

    @JvmStatic
    val STEEL_BLUE = colorOf(0x4682B4FF)

    @JvmStatic
    val TAN = colorOf(0xD2B48CFF)

    @JvmStatic
    val TEAL = colorOf(0x008080FF)

    @JvmStatic
    val THISTLE = colorOf(0xD8BFD8FF)

    @JvmStatic
    val TOMATO = colorOf(0xFF6347FF)

    @JvmStatic
    val TRANSPARENT = colorOf(0x00000000)

    @JvmStatic
    val TURQUOISE = colorOf(0x40E0D0FF)

    @JvmStatic
    val VIOLET = colorOf(0xEE82EEFF)

    @JvmStatic
    val WHEAT = colorOf(0xF5DEB3FF)

    @JvmStatic
    val WHITE = colorOf(0xFFFFFFFF)

    @JvmStatic
    val WHITE_SMOKE = colorOf(0xF5F5F5FF)

    @JvmStatic
    val WINDOW = colorOf(0xFFFFFFFF)

    @JvmStatic
    val WINDOW_FRAME = colorOf(0x646464FF)

    @JvmStatic
    val WINDOW_TEXT = colorOf(0x000000FF)

    @JvmStatic
    val YELLOW = colorOf(0xFFFF00FF)

    @JvmStatic
    val YELLOW_GREEN = colorOf(0x9ACD32FF)

    @JvmStatic
    val STEALTH_ORANGE = colorOf(0xFF880000)

    @JvmStatic
    val STEALTH_OLIVE = colorOf(0x66660000)

    @JvmStatic
    val STEALTH_GREEN = colorOf(0x33DD1100)

    @JvmStatic
    val STEALTH_PINK = colorOf(0xFF22EE00)

    @JvmStatic
    val STEALTH_BLUE = colorOf(0x0077BB00)
}
