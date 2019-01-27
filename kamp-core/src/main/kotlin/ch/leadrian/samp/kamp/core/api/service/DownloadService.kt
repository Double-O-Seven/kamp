package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import javax.inject.Inject

class DownloadService
@Inject
internal constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    fun addCharacterModel(baseModelId: Int, newModelId: Int, dffName: String, txdName: String) {
        nativeFunctionExecutor.addCharModel(
                baseid = baseModelId,
                newid = newModelId,
                dffname = dffName,
                txdname = txdName
        )
    }

    @JvmOverloads
    fun addSimpleModel(
            baseModelId: Int,
            newModelId: Int,
            dffName: String,
            txdName: String,
            virtualWorldId: Int? = null
    ) {
        nativeFunctionExecutor.addSimpleModel(
                virtualworld = virtualWorldId ?: -1,
                baseid = baseModelId,
                newid = newModelId,
                dffname = dffName,
                txdname = txdName
        )
    }

    @JvmOverloads
    fun addTimedSimpleModel(
            baseModelId: Int,
            newModelId: Int,
            dffName: String,
            txdName: String,
            timeOn: Int,
            timeOff: Int,
            virtualWorldId: Int? = null
    ) {
        nativeFunctionExecutor.addSimpleModelTimed(
                virtualworld = virtualWorldId ?: -1,
                baseid = baseModelId,
                newid = newModelId,
                dffname = dffName,
                txdname = txdName,
                timeon = timeOn,
                timeoff = timeOff
        )
    }

    @JvmOverloads
    fun findModelFileNameFromCRC(crc: Int, resultSize: Int = 256): String? {
        val result = ReferenceString()
        val success = nativeFunctionExecutor.findModelFileNameFromCRC(crc, result, resultSize)
        return if (success) {
            result.value
        } else {
            null
        }
    }

    @JvmOverloads
    fun findTextureFileNameFromCRC(crc: Int, resultSize: Int = 256): String? {
        val result = ReferenceString()
        val success = nativeFunctionExecutor.findTextureFileNameFromCRC(crc, result, resultSize)
        return if (success) {
            result.value
        } else {
            null
        }
    }

}