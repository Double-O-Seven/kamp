package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.cidl.model.Constant
import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.model.InterfaceDefinitionUnit
import ch.leadrian.samp.cidl.parser.FileInterfaceDefinitionSource
import ch.leadrian.samp.cidl.parser.InterfaceDefinitionParser
import org.gradle.api.DefaultTask
import org.gradle.api.internal.file.FileLookup
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class JavaCodegenTask
@Inject
constructor(private val fileLookup: FileLookup) : DefaultTask() {

    private val extension: JavaCodegenExtension
        get() = project.extensions.getByType(JavaCodegenExtension::class.java)

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
        fileLookup.fileResolver.resolve(extension.outputDirectoryPath)
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
                kampCoreVersion = project.version.toString(),
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

    @InputFiles
    fun getInputFiles(): List<File> = interfaceDefinitionFiles

    @OutputFiles
    fun getOutputFiles(): List<File> = mutableListOf<File>().apply {
        addAll(sampCallbacksJavaGenerator.outputFiles)
        addAll(sampConstantsJavaGenerator.outputFiles)
        addAll(sampNativeFunctionsJavaGenerator.outputFiles)
    }
}