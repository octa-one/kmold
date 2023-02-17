package aam.kmold.model

sealed class Node {

    var next: Node? = null
}

class EmptyNode : Node()

class TemplateNode(
    val variable: String
) : Node()

class EscapeNode(
    val escaped: String
) : Node()

class ConditionNode(
    val type: Token.Type,
    val variable: String,
    var positiveNode: Node = EmptyNode(),
    var negativeNode: Node = EmptyNode()
) : Node()

class PlainNode(
    val content: String
) : Node()
