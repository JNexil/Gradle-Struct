package su.jfdev.gradle.service.additional

import com.nhaarman.mockito_kotlin.*
import org.gradle.api.tasks.*
import org.junit.*
import org.junit.Assert.*
import java.util.*

abstract class AdditionalContainerTest {
    abstract val container: AdditionalContainer
    val sources: SourceSetContainer = mock()
    val sourceSets = ArrayList<String>()

    init {
        whenever(sources.maybeCreate(any())).then { maybeCreate ->
            val sourceSet: SourceSet = mock()
            whenever(sourceSet.name).then {
                maybeCreate.getArgument(0)
            }
            sourceSets += sourceSet.name
            sourceSet
        }
        whenever(sources.remove(any())).then {
            sourceSets -= it.getArgument<SourceSet>(0).name
            null
        }
    }

    private val ANY = "any"

    @Test fun `should create sourceSet and contain it when non-modified`() {
        whenever@
        container.add(ANY)
        `should cache this sourceSet`@
        shouldContains(ANY)
    }

    @Test fun `should iterate given sourceSet`() {
        whenever@
        container.add(ANY)
        then@
        assertEquals(ANY, container.single().name)
    }

    @Test fun `should contain sourceSet in SourceSetContainer`() {
        whenever@
        container.add(ANY)
        then@
        assertEquals(ANY, sourceSets.single())
    }

    @Test fun `should destroy sourceSet in SSContainer when remove it in container`() {
        before@
        container.add(ANY)

        whenever@
        container.remove(ANY)

        then@
        shouldNotContains(ANY)
        shouldDestroy(ANY)
    }

    fun shouldDestroy(any: String) {
        assert(any !in sourceSets) { "Container doesn't destroy sourceSet"}
    }

    fun shouldContains(any: String) {
        assert(any in container) { "Container doesn't cache sourceSet" }
    }

    fun shouldNotContains(any: String) {
        assert(any !in container) { "Container doesn't remove sourceSet" }
    }
}

