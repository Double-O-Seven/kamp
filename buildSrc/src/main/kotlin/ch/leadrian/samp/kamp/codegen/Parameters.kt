package ch.leadrian.samp.kamp.codegen

import ch.leadrian.samp.cidl.model.Parameter

val Parameter.isOutParameter: Boolean
    get() = hasAttribute("out")