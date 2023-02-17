package aam.kmold.model

data class Token(
    val type: Type,
    val body: String
) {

    constructor(type: Type) : this(type, "")

    enum class Type {

        ESCAPE, // \
        TEMPLATE, // { ... }
        IF, // if
        ELIF, // elif
        ELSE, // else
        ENDIF, // endif
        PLAIN;
    }
}
