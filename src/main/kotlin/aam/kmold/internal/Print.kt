package aam.kmold.internal

import aam.kmold.VariableManager
import aam.kmold.model.ConditionNode
import aam.kmold.model.EmptyNode
import aam.kmold.model.EscapeNode
import aam.kmold.model.Node
import aam.kmold.model.PlainNode
import aam.kmold.model.TemplateNode

fun Appendable.write(head: Node, variableManager: VariableManager) {
    val deque = ArrayDeque<Node>()
    deque.addLast(head)

    while (deque.isNotEmpty()) {
        val node = deque.removeLast()
        node.next?.let(deque::addLast)
        when (node) {
            is PlainNode -> {
                append(node.content)
            }
            is TemplateNode -> {
                append(variableManager.getString(node.variable))
            }
            is EscapeNode -> {
                append(node.escaped)
            }
            is ConditionNode -> {
                if (variableManager.getBoolean(node.variable)) {
                    node.positiveNode.let(deque::addLast)
                } else {
                    node.negativeNode.let(deque::addLast)
                }
            }
            is EmptyNode -> Unit
        }
    }
}
