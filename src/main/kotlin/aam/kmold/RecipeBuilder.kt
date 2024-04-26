package aam.kmold

import aam.kmold.internal.buildTree
import aam.kmold.internal.endsWithNewLine
import aam.kmold.internal.parse
import aam.kmold.internal.write
import java.io.File
import java.io.FileOutputStream

class RecipeBuilder(
    private val moldExtension: String,
    private val variableManager: VariableManager
) {

    private val basePath = System.getProperty("user.dir")
    private val basePathFile = File(basePath)

    fun mkdir(block: MkDirBuilder.() -> Unit) {
        val mkdir = MkDirBuilder(variableManager).apply(block)
        if (File(basePath, mkdir.path).mkdirs()) {
            println("Created: ${mkdir.path}")
        }
    }

    fun append(block: AppendBuilder.() -> Unit) {
        val append = AppendBuilder(variableManager).apply(block)
        val file = basePathFile.resolve(append.to)
        val endsWithNewLine = endsWithNewLine(file)
        FileOutputStream(file, true).bufferedWriter().use { writer ->
            if (!endsWithNewLine) writer.appendLine()
            writer.appendLine(append.content)
        }
        println("Appended: ${append.to}")
    }

    fun appendAfterMarker(block: MarkedAppendBuilder.() -> Unit) {
        val append = MarkedAppendBuilder(variableManager).apply(block)
        val file = basePathFile.resolve(append.to)
        val tempFile = file.resolveSibling(".temp_mold")

        val reader = file.bufferedReader()
        val writer = tempFile.bufferedWriter()

        try {
            reader.lineSequence().forEach { line ->
                val contentIndex = line.indexOfFirst { !it.isWhitespace() }
                if (line.startsWith(append.marker, startIndex = contentIndex)) {
                    writer.appendLine(append.content)
                    repeat(contentIndex) { writer.append(' ') }
                    writer.appendLine(append.marker)
                } else {
                    writer.appendLine(line)
                }
            }
        } finally {
            reader.close()
            writer.close()
        }

        tempFile.copyTo(file, overwrite = true)
        tempFile.delete()
        println("Appended: ${append.to}")
    }

    fun instantiateDir(block: InstantiateBuilder.() -> Unit) {
        val instantiate = InstantiateBuilder(variableManager).apply(block)

        val from = basePathFile.resolve(instantiate.from)
        val to = basePathFile.resolve(instantiate.to)

        from.walk()
            .drop(1) // Drop directory itself
            .filterNot { it.name == ".DS_Store" }
            .forEach { file ->
                val actualPath = file.toRelativeString(from)
                    .split(File.separatorChar)
                    .joinToString(
                        separator = File.separatorChar.toString(),
                        transform = { segment ->
                            buildString { parseAndWrite(sequenceOf(segment), this) }.trim()
                        }
                    )

                if (file.isDirectory) {
                    val actualFile = File(to, actualPath)
                    actualFile.mkdirs()
                    println("Created: ${actualFile.relativeToOrSelf(basePathFile).path}")
                } else {
                    val actualFileName = if (moldExtension.isEmpty()) {
                        actualPath
                    } else {
                        actualPath.substringBeforeLast(moldExtension)
                    }
                    val actualFile = File(to, actualFileName)
                    actualFile.parentFile.mkdirs()
                    instantiateFile(file, actualFile)
                    println("Instantiated: ${actualFile.relativeToOrSelf(basePathFile).path}")
                }
            }
    }

    fun instantiate(block: InstantiateBuilder.() -> Unit) {
        val instantiate = InstantiateBuilder(variableManager).apply(block)

        val from = basePathFile.resolve(instantiate.from)
        val to = basePathFile.resolve(instantiate.to)
        to.parentFile.mkdirs()

        instantiateFile(from, to)

        println("Instantiated: ${instantiate.to}")
    }

    private fun instantiateFile(from: File, to: File) {
        val reader = from.bufferedReader()
        val writer = to.bufferedWriter()

        try {
            parseAndWrite(reader.lineSequence(), writer)
        } finally {
            reader.close()
            writer.close()
        }
    }

    private fun parseAndWrite(input: Sequence<String>, output: Appendable) {
        val tokens = parse(input)
        val head = buildTree(tokens)
        output.write(head, variableManager)
    }
}
