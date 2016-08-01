package su.jfdev.gradle.service.util

import org.gradle.api.*

class ContainsChecker(val container: NamedDomainObjectCollection<*>): Collection<String> by container.names {
    var expect: Boolean = true

    override fun contains(element: String) = containsIt(element) == expect

    private fun containsIt(element: String) = container.findByName(element) != null

    val reverse: ContainsChecker get() = ContainsChecker(container).apply {
        expect = !this@ContainsChecker.expect
    }

    companion object {
        @JvmStatic fun checker(container: NamedDomainObjectCollection<*>): Collection<String>
                = ContainsChecker(container)
    }
}
