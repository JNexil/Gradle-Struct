package su.jfdev.gradle.service.util

import org.gradle.api.*

class ContainsChecker(val item: String, val container: NamedDomainObjectCollection<*>): Collection<String> by container.names {
    var expect: Boolean = true

    override fun contains(element: String) = containsIt(element) == expect

    private fun containsIt(element: String) = container.findByName(element) != null

    val reverse: ContainsChecker get() = ContainsChecker(item, container).apply {
        expect = !this@ContainsChecker.expect
    }
    val action: String
        get() = when {
                    expect -> "contains"
                    else   -> "not contains"
                } + " $item"
}

fun NamedDomainObjectCollection<*>.checker(item: String) = ContainsChecker(item, this)
