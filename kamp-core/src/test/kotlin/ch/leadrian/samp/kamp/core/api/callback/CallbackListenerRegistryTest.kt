package ch.leadrian.samp.kamp.core.api.callback

import com.google.common.reflect.ClassPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.functions

internal class CallbackListenerRegistryTest {

    private val callbackListenerRegistry = CallbackListenerRegistry(CallbackListener::class)

    @Test
    fun shouldRegisterCallbackListener() {
        callbackListenerRegistry.register(FooCallbackListener)

        assertThat(callbackListenerRegistry.listeners.toList())
                .containsExactly(FooCallbackListener)
    }

    @Test
    fun shouldReturnListenersInExpectedOrder() {
        callbackListenerRegistry.register(QuxCallbackListener)
        callbackListenerRegistry.register(FooCallbackListener)
        callbackListenerRegistry.register(BazCallbackListener)
        callbackListenerRegistry.register(BarCallbackListener)
        callbackListenerRegistry.register(BatCallbackListener)


        val listeners = callbackListenerRegistry.listeners

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

        assertThat(callbackListenerRegistry.listeners.toList())
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

        assertThat(callbackListenerRegistry.listeners.toList())
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

        assertThat(callbackListenerRegistry.listeners.toList())
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
            val className = "${simpleName}Processor"
            Files.newBufferedWriter(Paths.get("src", "main", "kotlin", "ch", "leadrian", "samp", "kamp", "core", "runtime", "callback", "$className.kt")).use { writer ->
                writer.apply {
                    val callbackMethod = clazz.kotlin.functions.first { it.name.startsWith("on") }
                    val packageImports = TreeSet<String>()
                    packageImports += "ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry"
                    packageImports += "ch.leadrian.samp.kamp.core.api.callback.$simpleName"
                    packageImports += "javax.inject.Inject"
                    packageImports += "javax.inject.Singleton"
                    packageImports += callbackMethod.parameters.drop(1).map { it.type.classifier as KClass<*> }
                            .filter { it.java.getPackage() != null }
                            .filter { it.java.getPackage().name != "kotlin" && it.java.getPackage().name != "java.lang" }
                            .map { it.java.name }

                    write("package ch.leadrian.samp.kamp.core.runtime.callback\n\n")
                    packageImports.forEach {
                        write("import $it\n")
                    }
                    write("\n")
                    write("@Singleton\n")
                    write("internal class $className\n")
                    write("@Inject\n")
                    write("constructor() : CallbackListenerRegistry<$simpleName>($simpleName::class), $simpleName {\n\n")
                    callbackMethod.apply {
                        writer.write("    override fun $name(")
                        writer.write(parameters.drop(1).joinToString(", ") {
                            val type = (it.type.classifier as KClass<*>).simpleName
                            "${it.name}: $type"
                        })
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