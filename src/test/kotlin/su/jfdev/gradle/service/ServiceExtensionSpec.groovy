package su.jfdev.gradle.service

import spock.lang.Ignore

import static su.jfdev.gradle.service.ConfigNameKt.makeConfigName
import static su.jfdev.gradle.service.ConfigNameKt.scopes

class ServiceExtensionSpec extends ServicePluginSpec {
    def "should add dependency with def impl"() {
        given:
        def userProject = addSubproject("user")
        def ownerProject = addSubproject("owner")
        project.subprojects {
            apply plugin: "java"
            apply plugin: ServicePlugin
        }

        ownerProject.services {
            impl.first = [:]
            impl.second = [:]
            impl.third = [:]
            main = "first"
        }

        when:
        userProject.services {
            defRequire(ownerProject.path)
            require(ownerProject.path, "second")
        }
        then:
        contains("first", "second")
        contains(false, "third")

    }

    @Ignore
    void contains(boolean expect = true, String... keys) {
        for (key in keys) for (scope in scopes) for (sources in ["api", "main", "impl"]) {
            def name = makeConfigName(sources, scope)
            def conf = project.configurations.findByName(name)
            def result = expect == conf.dependencies.any {
                it instanceof DependencyWithSources &&
                        it.project.path == ":$key" &&
                        it.configuration == scope &&
                        it.sources == sources
            }
            assert result ?: "$key || $scope | $sources"
        }
    }
}