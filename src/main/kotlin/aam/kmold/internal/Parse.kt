package aam.kmold.internal

import aam.kmold.model.Keyword
import aam.kmold.model.Token
import aam.kmold.util.safeSubstring
import java.lang.StringBuilder

internal fun parse(input: Sequence<String>): List<Token> {
    val tokens = mutableListOf<Token>()
    val plainBuilder = StringBuilder()

    fun maybeEndPlain() {
        if (plainBuilder.isNotEmpty()) {
            tokens += Token(Token.Type.PLAIN, plainBuilder.toString())
            plainBuilder.setLength(0)
        }
    }

    input.forEach { line ->
        var i = 0
        while (i < line.length) {
            when (val char = line[i]) {
                '\\' -> {
                    maybeEndPlain()
                    val escaped = getEscapedChar(line, i)
                    tokens += Token(Token.Type.ESCAPE, escaped.toString())
                    i += 2
                }
                '$' -> {
                    maybeEndPlain()
                    val keyword = getKeywordOrNull(line, ++i)
                    requireNotNull(keyword) { "Illegal character $" }
                    i += keyword.str.length
                    when (keyword) {
                        Keyword.TEMPLATE_START -> {
                            val variable = getBraceContent(line, i, '}')
                            tokens += Token(Token.Type.TEMPLATE, variable)
                            i += (1 + variable.length)
                        }
                        Keyword.IF -> {
                            val condition = getBraceContent(line, i, ')')
                            tokens += Token(Token.Type.IF, condition)
                            i += (1 + condition.length)
                        }
                        Keyword.ELIF -> {
                            val condition = getBraceContent(line, i, ')')
                            tokens += Token(Token.Type.ELIF, condition)
                            i += (1 + condition.length)
                        }
                        Keyword.ELSE -> {
                            tokens += Token(Token.Type.ELSE)
                        }
                        Keyword.ENDIF -> {
                            tokens += Token(Token.Type.ENDIF)
                        }
                    }
                }
                else -> {
                    plainBuilder.append(char)
                    i++
                }
            }
        }
        plainBuilder.appendLine()
    }
    maybeEndPlain()

    return tokens.toList()
}

private fun getKeywordOrNull(input: String, i: Int): Keyword? =
    Keyword.entries.firstOrNull { keyword ->
        input.safeSubstring(i, i + keyword.str.length) == keyword.str
    }

private fun getBraceContent(input: String, i: Int, brace: Char): String {
    var j = i
    while (j < input.length && input[j] != brace) {
        j++
    }
    require(j - i > 0) { "Empty block" }
    return input.substring(i, j)
}

private fun getEscapedChar(input: String, i: Int): Char {
    val nextChar = input.getOrNull(i + 1)
    requireNotNull(nextChar) { "Illegal character \\" }
    require(nextChar in ESCAPABLE) { "Illegal escape: $nextChar" }
    return nextChar
}

private val ESCAPABLE = charArrayOf('$', '\\')
