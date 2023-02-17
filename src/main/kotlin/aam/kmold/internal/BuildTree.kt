package aam.kmold.internal

import aam.kmold.model.ConditionNode
import aam.kmold.model.EmptyNode
import aam.kmold.model.EscapeNode
import aam.kmold.model.Node
import aam.kmold.model.PlainNode
import aam.kmold.model.TemplateNode
import aam.kmold.model.Token

fun buildTree(tokens: List<Token>): Node {
    val head = EmptyNode()
    val deque = ArrayDeque<ConditionNode>()

    var node: Node = head

    tokens.forEach { token ->
        when (token.type) {
            Token.Type.IF -> {
                val conditionNode = ConditionNode(token.type, token.body)
                node.next = conditionNode
                node = conditionNode.positiveNode
                deque.addLast(conditionNode)
            }
            Token.Type.ENDIF -> {
                var ifToken: ConditionNode
                do {
                    ifToken = deque.removeLast()
                } while (ifToken.type != Token.Type.IF)
                node = ifToken
            }
            Token.Type.ELSE -> {
                val ifToken = deque.last()
                node = ifToken.negativeNode
            }
            Token.Type.ELIF -> {
                val ifToken = deque.last()
                val conditionNode = ConditionNode(token.type, token.body)
                ifToken.negativeNode.next = conditionNode
                node = conditionNode.positiveNode
                deque.addLast(conditionNode)
            }
            Token.Type.PLAIN -> {
                val nextNode = PlainNode(token.body)
                node.next = nextNode
                node = nextNode
            }
            Token.Type.ESCAPE -> {
                val nextNode = EscapeNode(token.body)
                node.next = nextNode
                node = nextNode
            }
            Token.Type.TEMPLATE -> {
                val nextNode = TemplateNode(token.body)
                node.next = nextNode
                node = nextNode
            }
        }
    }

    return head
}
