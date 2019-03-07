package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.kamp.cidl.model.Constant
import ch.leadrian.samp.kamp.cidl.model.Function
import ch.leadrian.samp.kamp.cidl.model.InterfaceDefinitionUnit
import ch.leadrian.samp.kamp.cidl.parser.FileInterfaceDefinitionSource
import ch.leadrian.samp.kamp.cidl.parser.InterfaceDefinitionParser
import org.gradle.api.DefaultTask
import org.gradle.api.internal.file.FileLookup
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class JavaCodegenTask
@Inject
constructor(private val fileLookup: FileLookup) : DefaultTask() {

    @get:Nested
    internal val extension: JavaCodegenExtension by lazy {
        project.extensions.getByType(JavaCodegenExtension::class.java)
    }

    private val interfaceDefinitionFiles: List<File> by lazy {
        extension
                .interfaceDefinitionFiles
                .asSequence()
                .map { fileLookup.fileResolver.resolve(it) }
                .toList()
    }

    private val interfaceDefinitionUnit: InterfaceDefinitionUnit by lazy {
        with(InterfaceDefinitionParser()) {
            val interfaceDefinitionSources = interfaceDefinitionFiles
                    .asSequence()
                    .map { FileInterfaceDefinitionSource(it.toPath()) }
                    .toList()
            parse(*interfaceDefinitionSources.toTypedArray())
        }
    }

    private val functions: List<Function>
        get() = interfaceDefinitionUnit.functions

    private val constants: List<Constant>
        get() = interfaceDefinitionUnit.constants

    private val outputDirectory: File by lazy {
        project.buildDir.resolve(JavaCodegenPlugin.GENERATED_SOURCE_DIRECTORY)
    }

    private val apiOutputDirectory: File by lazy {
        outputDirectory.resolve(extension.constantsJavaPackageName.replace('.', File.separatorChar))
    }

    private val runtimeOutputDirectory: File by lazy {
        outputDirectory.resolve(extension.runtimeJavaPackageName.replace('.', File.separatorChar))
    }

    private val sampCallbacksJavaGenerator: SAMPCallbacksJavaGenerator by lazy {
        SAMPCallbacksJavaGenerator(functions, extension.runtimeJavaPackageName, runtimeOutputDirectory)
    }

    private val sampConstantsJavaGenerator: SAMPConstantsJavaGenerator by lazy {
        SAMPConstantsJavaGenerator(
                constants = constants,
                javaPackageName = extension.constantsJavaPackageName,
                kampCoreVersion = getKampCoreVersion(),
                outputDirectory = apiOutputDirectory
        )
    }

    private val sampNativeFunctionsJavaGenerator: SAMPNativeFunctionsJavaGenerator by lazy {
        SAMPNativeFunctionsJavaGenerator(functions, extension.runtimeJavaPackageName, runtimeOutputDirectory)
    }

    @TaskAction
    fun generate() {
        sampCallbacksJavaGenerator.generate()
        sampConstantsJavaGenerator.generate()
        sampNativeFunctionsJavaGenerator.generate()
    }

    @Input
    fun getKampCoreVersion(): String = project.version.toString()

    @OutputFiles
    fun getOutputFiles(): List<File> = mutableListOf<File>().apply {
        addAll(sampCallbacksJavaGenerator.outputFiles)
        addAll(sampConstantsJavaGenerator.outputFiles)
        addAll(sampNativeFunctionsJavaGenerator.outputFiles)
    }
}