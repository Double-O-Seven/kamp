package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString

internal object VersionChecker {

    val log = loggerFor<Server>()

    fun check(nativeFunctionExecutor: SAMPNativeFunctionExecutor) {
        val version = ReferenceString()
        nativeFunctionExecutor.getKampPluginVersion(version, 256)
        val kampPluginVersion = version.value
        if (kampPluginVersion != SAMPConstants.KAMP_CORE_VERSION) {
            throw IllegalStateException("Kamp Plugin version $kampPluginVersion is different than Kamp Core version ${SAMPConstants.KAMP_CORE_VERSION}")
        } else {
            log.info("Running with Kamp version ${SAMPConstants.KAMP_CORE_VERSION}")
        }
    }

}