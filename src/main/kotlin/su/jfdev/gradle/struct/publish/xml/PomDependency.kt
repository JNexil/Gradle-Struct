package su.jfdev.gradle.struct.publish.xml

import org.gradle.api.artifacts.*
import su.jfdev.gradle.struct.describe.*


data class PomDependency(val groupId: String,
                         val artifactId: String,
                         val version: String,
                         val type: String = "jar",
                         val classifier: String? = null,
                         val exclusions: Sequence<PomExclusion> = emptySequence())

data class PomExclusion(val groupId: String, val artifactId: String)

internal infix fun Pack.dependenciesBy(scope: Scope): Sequence<PomDependency> = this[scope]
        .dependencies
        .asSequence()
        .flatMap(Dependency::dependencies)

private val Dependency?.dependencies: Sequence<PomDependency> get() = when (this) {
    is ModuleDependency -> dependencies()
    is PackDependency   -> sequenceOf(dependency)
    else                -> emptySequence()
}

private fun ModuleDependency.dependencies() = when {
    artifacts.isEmpty() -> sequenceOf(dependency)
    else                -> dependency dependencies artifacts
}

private val Dependency.dependency: PomDependency get() = PomDependency(
        groupId = group,
        artifactId = name,
        version = version,
        exclusions = exclusions
                                                                      )

private val Dependency.exclusions: Sequence<PomExclusion> get() = when (this) {
    is ModuleDependency -> excludeRules.asSequence().map(ExcludeRule::exclusion)
    else                -> emptySequence()
}

private val ExcludeRule.exclusion: PomExclusion get() = PomExclusion(
        groupId = group ?: "*",
        artifactId = module ?: "*"
                                                                    )

private infix fun PomDependency.dependencies(artifacts: Set<DependencyArtifact>) = artifacts.asSequence().map {
    copy(artifactId = it.name,
         type = it.type,
         classifier = it.classifier)
}