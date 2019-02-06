package ch.leadrian.samp.kamp.view.base

import java.util.Locale

object TextTransformers {

    private val tildeRemover = TildeReplacer("")

    @JvmStatic
    fun toUpperCase(): TextTransformer = ToUpperCaseTextTransformer

    @JvmStatic
    fun toLowerCase(): TextTransformer = ToLowerCaseTextTransformer

    @JvmStatic
    fun removeTilde(): TextTransformer = tildeRemover

    @JvmStatic
    fun replaceTilde(replacement: String): TextTransformer = TildeReplacer(replacement)

    @JvmStatic
    fun formatAtSign(): TextTransformer = AtSignFormatter

    @JvmStatic
    fun trim(): TextTransformer = Trimmer

}

infix fun TextTransformer.andThen(next: TextTransformer): TextTransformer = CombiningTextTransformer(this, next)

private class CombiningTextTransformer(private val first: TextTransformer, private val second: TextTransformer) :
        TextTransformer {

    override fun transform(text: String, locale: Locale): String =
            second.transform(first.transform(text, locale), locale)

}

private object ToUpperCaseTextTransformer : TextTransformer {

    override fun transform(text: String, locale: Locale): String = text.toUpperCase(locale)

}

private object ToLowerCaseTextTransformer : TextTransformer {

    override fun transform(text: String, locale: Locale): String = text.toLowerCase(locale)

}

private class TildeReplacer(private val replacement: String) : TextTransformer {

    override fun transform(text: String, locale: Locale): String = text.replace("~", replacement)

}

private object AtSignFormatter : TextTransformer {

    override fun transform(text: String, locale: Locale): String = text.replace("@", "(at)")

}

private object Trimmer : TextTransformer {

    override fun transform(text: String, locale: Locale): String = text.trim()

}
