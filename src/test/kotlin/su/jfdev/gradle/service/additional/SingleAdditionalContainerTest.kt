package su.jfdev.gradle.service.additional

import org.junit.*
import org.junit.Assert.*

class SingleAdditionalContainerTest: AdditionalContainerTest() {
    override val container = AdditionalContainer.single(sources)

    @Test fun `should contain only one sourceSet`() {
        first@
        container.add("one")
        second@
        container.add("two")

        whenever@
        shouldContains("two")
        shouldNotContains("one")
        shouldDestroy("one")
    }

    @Test fun `should iterate single value`() {
        whenever@
        container.add("one")
        container.add("two")

        then@
        assertEquals("two", container.single().name)
    }
}