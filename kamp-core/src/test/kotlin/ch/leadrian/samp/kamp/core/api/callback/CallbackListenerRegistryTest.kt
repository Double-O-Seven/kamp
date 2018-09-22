package ch.leadrian.samp.kamp.core.api.callback

import com.google.common.reflect.ClassPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.full.functions

internal class CallbackListenerRegistryTest {

    private val callbackListenerRegistry = CallbackListenerRegistry(CallbackListener::class)

    @Test
    fun shouldRegisterCallbackListener() {
        callbackListenerRegistry.register(FooCallbackListener)

        assertThat(callbackListenerRegistry.getListeners().toList())
                .containsExactly(FooCallbackListener)
    }

    @Test
    fun shouldReturnListenersInExpectedOrder() {
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)
        callbackListenerRegistry.register(BazCallbackListener)
        callbackListenerRegistry.register(BarCallbackListener)
        callbackListenerRegistry.register(BatCallbackListener)


        val listeners = callbackListenerRegistry.getListeners()

        assertThat(listeners.toList())
                .containsExactly(
                        BatCallbackListener,
                        BarCallbackListener,
                        FooCallbackListener,
                        BazCallbackListener,
                        QuxCallbackListener
                )
    }

    @Test
    fun shouldNotContainDuplicates() {
        callbackListenerRegistry.register(FooCallbackListener)

        callbackListenerRegistry.register(FooCallbackListener, 99)

        assertThat(callbackListenerRegistry.getListeners().toList())
                .containsExactly(FooCallbackListener)
    }

    @Test
    fun registeringAgainShouldUpdatePriority() {
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)
        callbackListenerRegistry.register(BazCallbackListener)
        callbackListenerRegistry.register(BarCallbackListener)
        callbackListenerRegistry.register(BatCallbackListener)

        callbackListenerRegistry.register(QuxCallbackListener, 99)

        assertThat(callbackListenerRegistry.getListeners().toList())
                .containsExactly(
                        QuxCallbackListener,
                        BatCallbackListener,
                        BarCallbackListener,
                        FooCallbackListener,
                        BazCallbackListener
                )
    }

    @Test
    fun shouldRemoveCallbackListener() {
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)

        callbackListenerRegistry.unregister(QuxCallbackListener)

        assertThat(callbackListenerRegistry.getListeners().toList())
                .containsExactlyInAnyOrder(FooCallbackListener)
    }

    @Test
    fun generateClasses() {
        ClassPath.from(this::class.java.classLoader).allClasses.filter {
            it.packageName == "ch.leadrian.samp.kamp.core.api.callback"
        }.map {
            it.load()
        }.filter {
            it.simpleName.startsWith("On") && it.simpleName.endsWith("Listener")
        }.forEach { clazz ->
            val simpleName = clazz.simpleName
            Files.newBufferedWriter(Paths.get("${simpleName}Registry.kt")).use { writer ->
                writer.apply {
                    write("package ch.leadrian.samp.kamp.core.runtime.callback\n\n")
                    write("import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry\n")
                    write("import ch.leadrian.samp.kamp.core.api.callback.$simpleName\n")
                    write("import javax.inject.Inject\n")
                    write("import javax.inject.Singleton\n\n")
                    write("@Singleton\n")
                    write("internal class ${simpleName}Registry\n")
                    write("@Inject\n")
                    write("constructor() : CallbackListenerRegistry<$simpleName>($simpleName::class), $simpleName {\n\n")
                    clazz.kotlin.functions.first { it.name.startsWith("on") }.apply {
                        writer.write("    override fun $name(")
                        writer.write(parameters.drop(1).joinToString(", ") { "${it.name}: ${it.type}" })
                        writer.write(")")
                        if (returnType.toString() != "kotlin.Unit") {
                            writer.write(": $returnType")
                        }
                        writer.write(" {\n")
                        writer.write("        getListeners().forEach {\n")
                        writer.write("            it.$name(${this.parameters.drop(1).map { it.name }.joinToString(", ")})\n")
                        writer.write("        }\n")
                        writer.write("    }\n\n")
                    }
                    write("}\n")
                }
            }
        }
    }

    @Priority(5, CallbackListener::class)
    private object FooCallbackListener : CallbackListener

    @Priorities(Priority(6, CallbackListener::class))
    private object BarCallbackListener : CallbackListener

    @Priorities(Priority(7, CallbackListener::class))
    @Priority(3, CallbackListener::class)
    private object BazCallbackListener : CallbackListener

    @Priorities(Priority(8, CallbackListener::class))
    private object BatCallbackListener : CallbackListener

    @Priorities(Priority(9, String::class))
    private object QuxCallbackListener : CallbackListener

    private interface CallbackListener

}