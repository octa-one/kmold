package aam.kmold

class MkDirBuilder(variableManager: VariableManager) : VariableManager by variableManager {

    lateinit var path: String
}

class InstantiateBuilder(variableManager: VariableManager) : VariableManager by variableManager {

    lateinit var from: String
    lateinit var to: String
}

class AppendBuilder(variableManager: VariableManager) : VariableManager by variableManager {

    lateinit var content: String
    lateinit var to: String
}

class InputBuilder<T : Any>(variableManager: VariableManager) : VariableManager by variableManager {

    lateinit var name: String
    var default: T? = null
}
