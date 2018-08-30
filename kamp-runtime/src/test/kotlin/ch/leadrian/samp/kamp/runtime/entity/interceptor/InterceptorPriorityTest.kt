package ch.leadrian.samp.kamp.runtime.entity.interceptor

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class InterceptorPriorityTest {

    @Test
    fun givenNoInterceptorPriorityAnnotationItShouldReturnZero() {
        val value = WithoutPriority()

        val priority = value.interceptorPriority

        assertThat(priority)
                .isZero()
    }

    @Test
    fun givenInterceptorPriorityAnnotationItShouldReturnPriorityValue() {
        val value = WithPriority()

        val priority = value.interceptorPriority

        assertThat(priority)
                .isEqualTo(1337)
    }

    private class WithoutPriority

    @InterceptorPriority(1337)
    private class WithPriority

}