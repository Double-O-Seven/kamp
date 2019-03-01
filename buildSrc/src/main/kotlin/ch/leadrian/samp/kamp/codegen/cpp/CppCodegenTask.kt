package ch.leadrian.samp.kamp.codegen.cpp

import ch.leadrian.samp.kamp.cidl.model.Function
import ch.leadrian.samp.kamp.cidl.parser.FileInterfaceDefinitionSource
import ch.leadrian.samp.kamp.cidl.parser.InterfaceDefinitionParser
import org.gradle.api.DefaultTask
import org.gradle.api.internal.file.FileLookup
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class CppCodegenTask
@Inject
constructor(private val fileLookup: FileLookup) : DefaultTask() {

    private val extension: CppCodegenExtension
        get() = project.extensions.getByType(CppCodegenExtension::class.java)

    private val interfaceDefinitionFiles: List<File> by lazy {
        extension
                .interfaceDefinitionFiles
                .asSequence()
                .map { fileLookup.fileResolver.resolve(it) }
                .toList()
    }

    private val functions: List<Function> by lazy {
        with(InterfaceDefinitionParser()) {
            val interfaceDefinitionSources = interfaceDefinitionFiles
                    .asSequence()
                    .map { FileInterfaceDefinitionSource(it.toPath()) }
                    .toList()
            parse(*interfaceDefinitionSources.toTypedArray()).functions
        }
    }

    private val cppOutputDirectory: File
        get() = fileLookup.fileResolver.resolve(extension.cppOutputDirectoryPath)

    private val headersOutputDirectory: File
        get() = fileLookup.fileResolver.resolve(extension.headersOutputDirectoryPath)

    private val sampCallbacksCppGenerator: SAMPCallbacksCppGenerator by lazy {
        SAMPCallbacksCppGenerator(functions, cppOutputDirectory)
    }

    private val sampCallbacksDefGenerator: SAMPCallbacksDefGenerator by lazy {
        SAMPCallbacksDefGenerator(functions, cppOutputDirectory)
    }

    private val sampCallbacksMethodCacheCppGenerator: SAMPCallbacksMethodCacheCppGenerator by lazy {
        SAMPCallbacksMethodCacheCppGenerator(functions, cppOutputDirectory)
    }

    private val sampCallbacksMethodCacheHppGenerator: SAMPCallbacksMethodCacheHppGenerator by lazy {
        SAMPCallbacksMethodCacheHppGenerator(functions, headersOutputDirectory)
    }

    private val sampNativeFunctionsCppGenerator: SAMPNativeFunctionsCppGenerator by lazy {
        SAMPNativeFunctionsCppGenerator(
                functions = functions,
                javaPackageName = extension.runtimeJavaPackageName,
                kampPluginVersion = getKampPluginVersion(),
                outputDirectory = cppOutputDirectory
        )
    }

    @TaskAction
    fun generate() {
        sampCallbacksCppGenerator.generate()
        sampCallbacksDefGenerator.generate()
        sampCallbacksMethodCacheCppGenerator.generate()
        sampCallbacksMethodCacheHppGenerator.generate()
        sampNativeFunctionsCppGenerator.generate()
    }

    @Input
    fun getKampPluginVersion(): String = project.version.toString()

    @InputFiles
    fun getInputFiles(): List<File> = interfaceDefinitionFiles

    @OutputFiles
    fun getOutputFiles(): List<File> = mutableListOf<File>().apply {
        addAll(sampCallbacksCppGenerator.outputFiles)
        addAll(sampCallbacksDefGenerator.outputFiles)
        addAll(sampCallbacksMethodCacheCppGenerator.outputFiles)
        addAll(sampCallbacksMethodCacheHppGenerator.outputFiles)
        addAll(sampNativeFunctionsCppGenerator.outputFiles)
    }

}
