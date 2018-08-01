package ch.leadrian.samp.kamp.api.data

interface Animation {

    companion object {

        fun valueOf(library: String, name: String): Animation =
                AnimationImpl(
                        library = library,
                        animationName = name
                )
    }

    val library: String

    val animationName: String
}

private data class AnimationImpl(
        override val library: String,
        override val animationName: String
) : Animation
