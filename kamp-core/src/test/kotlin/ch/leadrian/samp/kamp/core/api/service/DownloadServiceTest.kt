package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class DownloadServiceTest {

    private lateinit var downloadService: DownloadService
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()

    @BeforeEach
    fun setUp() {
        downloadService = DownloadService(nativeFunctionExecutor)
    }

    @Test
    fun shouldAddCharacterModel() {
        every { nativeFunctionExecutor.addCharModel(any(), any(), any(), any()) } returns 0

        downloadService.addCharacterModel(
                baseModelId = 123,
                newModelId = 456,
                dffName = "test.dff",
                txdName = "test.txd"
        )

        verify {
            nativeFunctionExecutor.addCharModel(
                    baseid = 123,
                    newid = 456,
                    dffname = "test.dff",
                    txdname = "test.txd"
            )
        }
    }

    @Nested
    inner class AddSimpleModelTests {

        @Test
        fun shouldAddSimpleModel() {
            every { nativeFunctionExecutor.addSimpleModel(any(), any(), any(), any(), any()) } returns 0

            downloadService.addSimpleModel(
                    baseModelId = 123,
                    newModelId = 456,
                    dffName = "test.dff",
                    txdName = "test.txd",
                    virtualWorldId = 1337
            )

            verify {
                nativeFunctionExecutor.addSimpleModel(
                        baseid = 123,
                        newid = 456,
                        dffname = "test.dff",
                        txdname = "test.txd",
                        virtualworld = 1337
                )
            }
        }

        @Test
        fun givenNoVirtualWorldItShouldUseFallbackValue() {
            every { nativeFunctionExecutor.addSimpleModel(any(), any(), any(), any(), any()) } returns 0

            downloadService.addSimpleModel(
                    baseModelId = 123,
                    newModelId = 456,
                    dffName = "test.dff",
                    txdName = "test.txd"
            )

            verify {
                nativeFunctionExecutor.addSimpleModel(
                        baseid = 123,
                        newid = 456,
                        dffname = "test.dff",
                        txdname = "test.txd",
                        virtualworld = -1
                )
            }
        }

    }

    @Nested
    inner class AddTimedSimpleModelTests {

        @Test
        fun shouldAddSimpleModel() {
            every {
                nativeFunctionExecutor.addSimpleModelTimed(any(), any(), any(), any(), any(), any(), any())
            } returns 0

            downloadService.addTimedSimpleModel(
                    baseModelId = 123,
                    newModelId = 456,
                    dffName = "test.dff",
                    txdName = "test.txd",
                    timeOn = 12,
                    timeOff = 18,
                    virtualWorldId = 1337
            )

            verify {
                nativeFunctionExecutor.addSimpleModelTimed(
                        baseid = 123,
                        newid = 456,
                        dffname = "test.dff",
                        txdname = "test.txd",
                        timeon = 12,
                        timeoff = 18,
                        virtualworld = 1337
                )
            }
        }

        @Test
        fun givenNoVirtualWorldItShouldUseFallbackValue() {
            every {
                nativeFunctionExecutor.addSimpleModelTimed(any(), any(), any(), any(), any(), any(), any())
            } returns 0

            downloadService.addTimedSimpleModel(
                    baseModelId = 123,
                    newModelId = 456,
                    dffName = "test.dff",
                    txdName = "test.txd",
                    timeOn = 12,
                    timeOff = 18
            )

            verify {
                nativeFunctionExecutor.addSimpleModelTimed(
                        baseid = 123,
                        newid = 456,
                        dffname = "test.dff",
                        txdname = "test.txd",
                        timeon = 12,
                        timeoff = 18,
                        virtualworld = -1
                )
            }
        }

    }

    @Nested
    inner class FindModelFileNameFromCRCTests {

        @Test
        fun givenSuccessfulExecutionItShouldReturnModelFileName() {
            every { nativeFunctionExecutor.findModelFileNameFromCRC(1337, any(), any()) } answers {
                secondArg<ReferenceString>().value = "test.dff"
                true
            }

            val modelFileName = downloadService.findModelFileNameFromCRC(1337)

            assertThat(modelFileName)
                    .isEqualTo("test.dff")
        }

        @Test
        fun givenFailedExecutionItShouldReturnNull() {
            every { nativeFunctionExecutor.findModelFileNameFromCRC(1337, any(), any()) } answers {
                secondArg<ReferenceString>().value = "FAIL"
                false
            }

            val modelFileName = downloadService.findModelFileNameFromCRC(1337)

            assertThat(modelFileName)
                    .isNull()
        }
    }

    @Nested
    inner class FindTextureFileNameFromCRCTests {

        @Test
        fun givenSuccessfulExecutionItShouldReturnTextureFileName() {
            every { nativeFunctionExecutor.findTextureFileNameFromCRC(1337, any(), any()) } answers {
                secondArg<ReferenceString>().value = "test.txd"
                true
            }

            val textureFileName = downloadService.findTextureFileNameFromCRC(1337)

            assertThat(textureFileName)
                    .isEqualTo("test.txd")
        }

        @Test
        fun givenFailedExecutionItShouldReturnNull() {
            every { nativeFunctionExecutor.findTextureFileNameFromCRC(1337, any(), any()) } answers {
                secondArg<ReferenceString>().value = "FAIL"
                false
            }

            val textureFileName = downloadService.findTextureFileNameFromCRC(1337)

            assertThat(textureFileName)
                    .isNull()
        }
    }
}