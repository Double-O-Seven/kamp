package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import java.util.Properties

internal class VersionCheckerTest {

    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()

    @Test
    fun givenMatchingVersionsItShouldDoNothing() {
        every { nativeFunctionExecutor.getKampPluginVersion(any(), any()) } answers {
            firstArg<ReferenceString>().value = SAMPConstants.KAMP_CORE_VERSION
        }

        val caughtThrowable = catchThrowable { VersionChecker.check(nativeFunctionExecutor, Properties()) }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Test
    fun givenMismatchingVersionsAndNoConfigPropertyValueItShouldThrowException() {
        val kampCoreVersion = SAMPConstants.KAMP_CORE_VERSION
        every { nativeFunctionExecutor.getKampPluginVersion(any(), any()) } answers {
            firstArg<ReferenceString>().value = "$kampCoreVersion-fail"
        }

        val caughtThrowable = catchThrowable { VersionChecker.check(nativeFunctionExecutor, Properties()) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Kamp Plugin version $kampCoreVersion-fail is different than Kamp Core version $kampCoreVersion")
    }

    @Test
    fun givenMismatchingVersionsAndConfigPropertyValueFalseItShouldThrowException() {
        val kampCoreVersion = SAMPConstants.KAMP_CORE_VERSION
        every { nativeFunctionExecutor.getKampPluginVersion(any(), any()) } answers {
            firstArg<ReferenceString>().value = "$kampCoreVersion-fail"
        }
        val configProperties = Properties().apply {
            put(VersionChecker.IGNORE_VERSION_MISMATCH_PROPERTY_KEY, "false")
        }

        val caughtThrowable = catchThrowable { VersionChecker.check(nativeFunctionExecutor, configProperties) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Kamp Plugin version $kampCoreVersion-fail is different than Kamp Core version $kampCoreVersion")
    }

    @Test
    fun givenMismatchingVersionsAndConfigPropertyValueTrueItShouldNotThrowException() {
        val kampCoreVersion = SAMPConstants.KAMP_CORE_VERSION
        every { nativeFunctionExecutor.getKampPluginVersion(any(), any()) } answers {
            firstArg<ReferenceString>().value = "$kampCoreVersion-fail"
        }
        val configProperties = Properties().apply {
            put(VersionChecker.IGNORE_VERSION_MISMATCH_PROPERTY_KEY, "true")
        }

        val caughtThrowable = catchThrowable { VersionChecker.check(nativeFunctionExecutor, configProperties) }

        assertThat(caughtThrowable)
                .isNull()
    }

}