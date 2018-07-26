package ch.leadrian.samp.kamp.apicodegen

import ch.leadrian.samp.cidl.parser.FileInterfaceDefinitionSource
import ch.leadrian.samp.cidl.parser.InterfaceDefinitionSource
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import java.nio.file.Path
import java.nio.file.Paths

const val INPUT_LONG_OPT = "input"
const val INPUT_SHORT_OPT = "i"

const val OUTPUT_LONG_OPT = "output"
const val OUTPUT_SHORT_OPT = "o"

const val PACKAGE_LONG_OPT = "package"
const val PACKAGE_SHORT_OPT = "p"

internal class CodeGeneratorArguments(
        val packageName: String,
        val outputDirectoryPath: Path,
        val interfaceDefinitionSources: Array<InterfaceDefinitionSource>
) {

    companion object {

        fun parse(args: Array<String>): CodeGeneratorArguments {
            val options = Options().apply {
                addOption(buildInputOption())
                addOption(buildOutputOption())
                addOption(buildPackageOption())
            }

            val commandLineParser = DefaultParser()
            val commandLine = commandLineParser.parse(options, args)
            val outputDirectoryPath = Paths.get(commandLine.getOptionValue("output"))
            val packageName = commandLine.getOptionValue("package")
            val interfaceDefinitionSources = commandLine
                    .getOptionValues(INPUT_LONG_OPT)
                    .map { FileInterfaceDefinitionSource(Paths.get(it)) }
            return CodeGeneratorArguments(
                    packageName = packageName,
                    outputDirectoryPath = outputDirectoryPath,
                    interfaceDefinitionSources = interfaceDefinitionSources.toTypedArray()
            )
        }

        private fun buildInputOption(): Option {
            return Option
                    .builder(INPUT_SHORT_OPT)
                    .longOpt(INPUT_LONG_OPT)
                    .required()
                    .desc("Path for input IDL file")
                    .argName("input file")
                    .numberOfArgs(Option.UNLIMITED_VALUES)
                    .build()
        }

        private fun buildOutputOption(): Option {
            return Option
                    .builder(OUTPUT_SHORT_OPT)
                    .longOpt(OUTPUT_LONG_OPT)
                    .required()
                    .desc("Path of output directory without package structure")
                    .argName("output directory")
                    .build()
        }

        private fun buildPackageOption(): Option {
            return Option
                    .builder(PACKAGE_SHORT_OPT)
                    .longOpt(PACKAGE_LONG_OPT)
                    .required()
                    .desc("Package of generated classes")
                    .argName("package name")
                    .build()
        }
    }

}