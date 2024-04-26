package aam.kmold

class VariablesBuilder {

    private val any = HashMap<String, Any>()
    private val variableManager = MapVariableManager(any)

    fun boolean(id: String, expression: VariableManager.() -> Boolean) {
        any[id] = expression.invoke(variableManager)
    }

    fun string(id: String, expression: VariableManager.() -> String) {
        any[id] = expression.invoke(variableManager)
    }

    fun inputString(id: String, block: InputBuilder<String>.() -> Unit) {
        val input = InputBuilder<String>(variableManager).apply(block)
        val default = input.default
        while (true) {
            println(
                buildString {
                    append(input.name)
                    if (default != null) {
                        append(" [")
                        append(default)
                        append("]")
                    }
                    append(":")
                }
            )
            val line = readlnOrNull() ?: continue
            if (line.isEmpty()) {
                any[id] = default ?: continue
            } else {
                any[id] = line.trim()
            }
            break
        }
    }

    fun inputBoolean(id: String, block: InputBuilder<Boolean>.() -> Unit) {
        val input = InputBuilder<Boolean>(variableManager).apply(block)
        val default = input.default
        while (true) {
            println(
                buildString {
                    append(input.name)
                    if (default != null) {
                        append(" [")
                        if (default) append("Y")
                        else append("N")
                        append("]")
                    }
                    append(": (Y/N)")
                }
            )
            val line = readlnOrNull() ?: continue
            if (line.isEmpty()) {
                any[id] = default ?: continue
            } else {
                when (line.lowercase()) {
                    "y" -> any[id] = true
                    "n" -> any[id] = false
                    else -> continue
                }
            }
            break
        }
    }

    fun <T : Any> any(id: String, expression: VariableManager.() -> T) {
        any[id] = expression.invoke(variableManager)
    }

    fun build(): VariableManager = variableManager
}
