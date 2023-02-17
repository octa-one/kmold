package aam.kmold.util

fun <K, V> Map<K, V>.getNotNull(key: K): V =
    requireNotNull(get(key)) { "Variable or input $key is missing" }
