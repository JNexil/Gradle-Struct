package su.jfdev.gradle.struct.publish.xml

import groovy.util.*

data class NodeHelper(val node: Node) {
    val String.new: Node get() = node.appendNode(this)

    operator fun String.invoke(block: NodeHelper.() -> Unit) = NodeHelper(new).block()
    operator fun String.minus(value: Any) {
        new.appendNode(this, value)
    }
    operator fun String.plus(value: Any?) {
        this - (value ?: return)
    }
}