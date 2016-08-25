package su.jfdev.gradle.struct.publish.xml

import groovy.util.*
import org.gradle.api.*

data class NodeHelper(val node: Node) {
    constructor(node: XmlProvider): this(node.asNode())
    val String.new: Node get() = node.appendNode(this)

    operator fun String.invoke(block: NodeHelper.() -> Unit) = NodeHelper(new).block()
    operator fun String.minus(value: Any) = new.setValue(value)
    operator fun String.plus(value: Any?) {
        this - (value ?: return)
    }
}