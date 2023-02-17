package aam.kmold

import aam.kmold.util.getNotNull

interface VariableManager {

    fun getBoolean(key: String): Boolean
    fun getString(key: String): String

    fun <T : Any> get(key: String): T
}

class MapVariableManager(
    private val any: Map<String, Any>
) : VariableManager {

    override fun getBoolean(key: String): Boolean =
        any.getNotNull(key) as Boolean

    override fun getString(key: String): String =
        any.getNotNull(key).toString()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(key: String): T =
        any.getNotNull(key) as T
}
