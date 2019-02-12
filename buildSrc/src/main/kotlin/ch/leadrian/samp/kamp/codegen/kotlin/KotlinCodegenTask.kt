package ch.leadrian.samp.kamp.codegen.kotlin

import ch.leadrian.samp.kamp.cidl.model.Function
import ch.leadrian.samp.kamp.cidl.model.InterfaceDefinitionUnit
import ch.leadrian.samp.kamp.cidl.parser.FileInterfaceDefinitionSource
import ch.leadrian.samp.kamp.cidl.parser.InterfaceDefinitionParser
import org.gradle.api.DefaultTask
import org.gradle.api.internal.file.FileLookup
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class KotlinCodegenTask
@Inject
constructor(private val fileLookup: FileLookup) : DefaultTask() {

    private val extension: KotlinCodegenExtension
        get() = project.extensions.getByType(KotlinCodegenExtension::class.java)

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

    private val outputDirectory: File by lazy {
        project.buildDir.resolve(KotlinCodegenPlugin.GENERATED_SOURCE_DIRECTORY)
    }

    private val runtimeOutputDirectory: File by lazy {
        outputDirectory.resolve(extension.runtimeJavaPackageName.replace('.', File.separatorChar))
    }

    private val amxApiOutputDirectory: File by lazy {
        outputDirectory.resolve(extension.amxApiJavaPackageName.replace('.', File.separatorChar))
    }

    private val sampNativeFunctionExecutorKtGenerator: SAMPNativeFunctionExecutorKtGenerator by lazy {
        SAMPNativeFunctionExecutorKtGenerator(functions, extension.runtimeJavaPackageName, runtimeOutputDirectory)
    }

    private val sampNativeFunctionExecutorImplKtGenerator: SAMPNativeFunctionExecutorImplKtGenerator by lazy {
        SAMPNativeFunctionExecutorImplKtGenerator(functions, extension.runtimeJavaPackageName, runtimeOutputDirectory)
    }

    private val amxNativeFunctionGenerators: List<AmxNativeFunctionGenerator> by lazy {
        (0..32).map { AmxNativeFunctionGenerator(it, extension.amxApiJavaPackageName, amxApiOutputDirectory) }
    }

    @TaskAction
    fun generate() {
        sampNativeFunctionExecutorKtGenerator.generate()
        sampNativeFunctionExecutorImplKtGenerator.generate()
        amxNativeFunctionGenerators.forEach { it.generate() }
    }

    @InputFiles
    fun getInputFiles(): List<File> = interfaceDefinitionFiles

    @OutputFiles
    fun getOutputFiles(): List<File> = mutableListOf<File>().apply {
        addAll(sampNativeFunctionExecutorKtGenerator.outputFiles)
        addAll(sampNativeFunctionExecutorImplKtGenerator.outputFiles)
        amxNativeFunctionGenerators.forEach { addAll(it.outputFiles) }
    }
}
