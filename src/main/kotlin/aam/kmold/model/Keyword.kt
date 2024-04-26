package aam.kmold.model

enum class Keyword(val str: String) {

    TEMPLATE_START("{"),
    IF("if("),
    ELIF("elif("),
    ELSE("else"),
    ENDIF("endif");
}
