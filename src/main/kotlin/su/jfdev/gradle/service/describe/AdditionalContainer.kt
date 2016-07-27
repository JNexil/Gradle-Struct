package su.jfdev.gradle.service.describe

import org.gradle.api.tasks.*
import java.util.concurrent.*
import java.util.concurrent.atomic.*

abstract class AdditionalContainer private constructor(val sourceSets: SourceSetContainer): Iterable<SourceSet> {
    abstract infix fun add(sources: String): SourceSet
    abstract infix fun remove(sources: String): Boolean
    abstract operator fun contains(sources: String): Boolean
    abstract val isSingle: Boolean

    protected fun create(sources: String): SourceSet = sourceSets.maybeCreate(sources)
    protected fun SourceSet.destroy() = sourceSets.remove(this)

    companion object {
        fun smart(sourceSets: SourceSetContainer, single: Boolean) = when (single) {
            true  -> single(sourceSets)
            false -> multi(sourceSets)
        }

        fun multi(sourceSets: SourceSetContainer) = object: AdditionalContainer(sourceSets) {
            private val map = ConcurrentHashMap<String, SourceSet>()

            override fun add(sources: String) = map[sources] ?: create(sources).apply {
                map[sources] = this
            }

            override fun remove(sources: String) = map.remove(sources)?.destroy() != null

            override fun contains(sources: String) = map[sources] != null

            override fun iterator(): Iterator<SourceSet> = map.values.iterator()

            override val isSingle: Boolean get() = false
        }

        fun single(sourceSets: SourceSetContainer) = object: AdditionalContainer(sourceSets) {
            private val reference: AtomicReference<SourceSet> = AtomicReference()

            override fun add(sources: String) = create(sources).apply {
                reference.getAndSet(this)?.destroy()
            }

            override fun remove(sources: String) = reference.get()?.run {
                destroy()
                name == sources && reference.compareAndSet(this, null)
            } ?: false

            override fun contains(sources: String) = reference.get()?.name == sources

            override fun iterator(): Iterator<SourceSet> = sequenceOf(reference)
                    .mapNotNull { it.get() }
                    .iterator()

            override val isSingle: Boolean get() = true
        }
    }
}