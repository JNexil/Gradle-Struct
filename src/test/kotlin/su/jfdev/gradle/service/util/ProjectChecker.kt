package su.jfdev.gradle.service.util

import org.gradle.api.*

val Project.knownSources: ContainsChecker get() = sourceSets.checker("source")
val Project.knownConfigurations: ContainsChecker get() = configurations.checker("configuration")

val Project.unknownSources: ContainsChecker get() = knownSources.reverse
val Project.unknownConfigurations: ContainsChecker get() = knownConfigurations.reverse