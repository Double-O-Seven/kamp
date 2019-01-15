package ch.leadrian.samp.kamp.codegen

import ch.leadrian.samp.cidl.model.Function

val Function.isCallback: Boolean
    get() = hasAttribute("callback")

val Function.isNative: Boolean
    get() = hasAttribute("native")

val Function.hasNoImplementation: Boolean
    get() = hasAttribute("noimpl")

val Function.camelCaseName: String
    get() = "${name[0].toLowerCase()}${name.substring(1)}"

