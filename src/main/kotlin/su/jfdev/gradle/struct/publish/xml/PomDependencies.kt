package su.jfdev.gradle.struct.publish.xml

import org.gradle.api.publish.maven.*
import su.jfdev.gradle.struct.describe.*

internal fun MavenPublication.improveDependencies(pack: Pack) = pom.withXml {
    NodeHelper(it).addDependencies(pack)
}

private fun NodeHelper.addDependencies(pack: Pack) {
    "dependencies" {
        for (scope in Scope.values()) {
            for ((groupId, artifactId, version, type, classifier, exclusions) in pack dependenciesBy scope) {
                "dependency" {
                    "artifactId" - artifactId
                    "groupId" - groupId
                    "version" - version
                    "type" - type
                    "scope" - scope.name.toLowerCase()
                    "classifier" + classifier
                    "exclusions" {
                        for ((excludeGroup, excludeName) in exclusions) {
                            "exclusion" {
                                "groupId" - excludeGroup
                                "artifactId" - excludeName
                            }
                        }
                    }
                }
            }
        }
    }
}
