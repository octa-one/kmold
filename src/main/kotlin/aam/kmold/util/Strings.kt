package aam.kmold.util

fun String.safeSubstring(startIndex: Int, endIndex: Int): String =
    substring(startIndex, endIndex.coerceAtMost(length))
