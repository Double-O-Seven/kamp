package ch.leadrian.samp.kamp.codegen

import ch.leadrian.samp.kamp.cidl.model.Parameter

val Parameter.isOutParameter: Boolean
    get() = hasAttribute("out")