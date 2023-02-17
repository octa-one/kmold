package aam.kmold

class MoldBuilder {

    private lateinit var variableManager: VariableManager

    var moldExtension = MOLD_EXTENSION

    fun variables(block: VariablesBuilder.() -> Unit) {
        variableManager = VariablesBuilder().apply(block).build()
    }

    fun recipe(block: RecipeBuilder.() -> Unit) {
        RecipeBuilder(moldExtension, variableManager).apply(block)
    }
}

fun mold(block: MoldBuilder.() -> Unit) {

    MoldBuilder().apply(block)
}

private const val MOLD_EXTENSION = ".mold"
