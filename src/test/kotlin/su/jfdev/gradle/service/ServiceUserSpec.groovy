package su.jfdev.gradle.service

import org.gradle.api.Project
import spock.lang.Ignore
import su.jfdev.gradle.service.util.DependencyWithSources

import static su.jfdev.gradle.service.util.ConfigNameKt.makeConfigName
import static su.jfdev.gradle.service.util.ConfigNameKt.scopes

class ServiceUserSpec extends ServicePluginSpec {
    Project userProject
    Project ownerProject

    @Override
    void setup() {
        userProject = addSubproject("user")
        ownerProject = addSubproject("owner")
    }

    def "should add dependency with def impl"() {
        given:
        project.subprojects {
            apply plugin: "java"
            apply plugin: ServicePlugin
        }

        ownerProject.services {
            implementations.main "first"
            describe {
                api()
                spec()
                impl(
                        first: [
                                "interface": ["impl"]
                        ],
                        second: [
                                "interface": ["impl"]
                        ],
                        third: [
                                "interface": ["impl"]
                        ]
                )
            }
        }

        when:
        userProject.services {
            require {
                service ownerProject.path
                service ownerProject.path, "second"
            }
        }
        then:
        contains("first", "second")
        contains(false, "third")
    }

    @Ignore
    void contains(boolean expect = true, String... keys) {
        for (key in keys) for (scope in scopes) for (sources in ["api", "main", "impl"]) {
            def name = makeConfigName(sources, scope)
            def conf = userProject.configurations.findByName(name)
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