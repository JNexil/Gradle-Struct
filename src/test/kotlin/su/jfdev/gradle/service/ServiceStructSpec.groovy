package su.jfdev.gradle.service

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import spock.lang.Shared
import su.jfdev.gradle.service.additional.AdditionalSources as Source
import su.jfdev.gradle.service.util.DependencyWithSources

import static su.jfdev.gradle.service.util.ConfigNameKt.makeConfigName

class ServiceStructSpec extends ServicePluginSpec {
    @Shared Project anyProject

    def "check upper and downer"() {
        given:
        anyProject = addSubproject("any")
        anyProject.plugins.apply("java")
        anyProject.plugins.apply(ServicePlugin)

        anyProject.services {
            describe {
                api()
                spec()
                impl()
            }
        }

        expect:
        checkDependencies(Source.api, Source.spec, Source.impl)
    }

    private void checkDependencies(Source... sources) {
        for (Source source : sources) {
            for (it in source.upper)
                assert isDepend(source.name(), it.value, it.key)

            for (it in source.downer)
                assert isDepend(it.value, source.name(), it.key)
        }
    }

    private boolean isDepend(String receiver, String toCheck, String scope = "compile") {
        def config = makeConfigName(receiver, scope)
        def configuration = anyProject.configurations.getByName(config)
        configuration.dependencies.any {
            it instanceof DependencyWithSources && it.sources == toCheck
        }
    }

    private SourceSet sourceSet(String name) {
        anyProject.sourceSets.getByName(name)
    }
}