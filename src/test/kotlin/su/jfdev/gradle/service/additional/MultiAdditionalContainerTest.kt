package su.jfdev.gradle.service.additional

import org.junit.*
import org.junit.Assert.*

class MultiAdditionalContainerTest: AdditionalContainerTest() {
    override val container = AdditionalContainer.multi(sources)

    @Test fun `should contain few sourceSets`() {
        first@
        container.add("one")
        second@
        container.add("two")

        whenever@
        shouldContains("two")
        shouldContains("one")
    }

    @Test fun `should iterate few values`() {
        given@
        container.add("one")
        container.add("two")

        whenever@
        val list = container.toList()

        then@
        assertEquals(2, list.size)

        whenever@
        val (first, second) = list

        then@
        assertEquals("one", first.name)
        assertEquals("two", second.name)
    }
}