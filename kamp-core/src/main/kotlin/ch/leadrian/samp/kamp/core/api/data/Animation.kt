package ch.leadrian.samp.kamp.core.api.data

interface Animation {

    val library: String

    val animationName: String
}

fun animationOf(library: String, name: String): Animation =
        AnimationImpl(
                library = library,
                animationName = name
        )

private data class AnimationImpl(
        override val library: String,
        override val animationName: String
) : Animation
