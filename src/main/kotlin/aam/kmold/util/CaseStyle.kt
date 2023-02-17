package aam.kmold.util

import java.io.File

val CamelCase = CaseStyle(line = CharTransformation.LOWERCASE, word = CharTransformation.UPPERCASE, separator = "")
val PascalCase = CaseStyle(line = CharTransformation.NONE, word = CharTransformation.UPPERCASE, separator = "")
val SnakeCase = CaseStyle(line = CharTransformation.NONE, word = CharTransformation.LOWERCASE, separator = "_")
val KebabCase = CaseStyle(line = CharTransformation.NONE, word = CharTransformation.LOWERCASE, separator = "-")
val FilePath = CaseStyle(line = CharTransformation.NONE, word = CharTransformation.LOWERCASE, separator = File.separatorChar.toString())
val JavaPackage = CaseStyle(line = CharTransformation.NONE, word = CharTransformation.LOWERCASE, separator = ".")

class CaseStyle(
    val line: CharTransformation,
    val word: CharTransformation,
    val separator: String
)

enum class CharTransformation {

    NONE,
    UPPERCASE,
    LOWERCASE;
}

fun Iterable<String>.joinToString(case: CaseStyle): String {
    var isFirstTransformed = false
    return joinToString(
        separator = case.separator,
        transform = { word ->
            if (case.line != CharTransformation.NONE && !isFirstTransformed) {
                word.replaceFirstChar(case.line).also { isFirstTransformed = true }
            } else {
                word.replaceFirstChar(case.word)
            }
        }
    )
}

private fun String.replaceFirstChar(transformation: CharTransformation): String =
    when (transformation) {
        CharTransformation.NONE -> this
        CharTransformation.LOWERCASE -> replaceFirstChar { it.lowercaseChar() }
        CharTransformation.UPPERCASE -> replaceFirstChar { it.uppercaseChar() }
    }
