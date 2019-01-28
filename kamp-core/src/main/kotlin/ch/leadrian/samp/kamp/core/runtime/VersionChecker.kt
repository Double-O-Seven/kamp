package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import java.util.Properties

internal object VersionChecker {

    const val IGNORE_VERSION_MISMATCH_PROPERTY_KEY = "kamp.ignore.version.mismatch"

    private val log = loggerFor<Server>()

    fun check(nativeFunctionExecutor: SAMPNativeFunctionExecutor, configProperties: Properties) {
        val kampPluginVersion = nativeFunctionExecutor.getKampPluginVersion()
        if (kampPluginVersion != SAMPConstants.KAMP_CORE_VERSION) {
            handleVersionMismatch(configProperties, kampPluginVersion)
        } else {
            log.info("Running with Kamp version ${SAMPConstants.KAMP_CORE_VERSION}")
        }
    }

    private fun SAMPNativeFunctionExecutor.getKampPluginVersion(): String? {
        val version = ReferenceString()
        getKampPluginVersion(version, 256)
        return version.value
    }

    private fun handleVersionMismatch(configProperties: Properties, kampPluginVersion: String?) {
        val message = "Kamp Plugin version $kampPluginVersion is different than Kamp Core version ${SAMPConstants.KAMP_CORE_VERSION}"
        if (ignoreVersionMismatch(configProperties)) {
            log.warn(message)
        } else {
            throw IllegalStateException(message)
        }
    }

    private fun ignoreVersionMismatch(configProperties: Properties): Boolean =
            configProperties.getProperty(IGNORE_VERSION_MISMATCH_PROPERTY_KEY)?.toBoolean() ?: false

}