package su.jfdev.gradle.service.require

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.PluginSpec
import su.jfdev.gradle.struct.describe.Scope

class RequireSpec extends PluginSpec {
    public static final ALL = ["api", "main", "impl", "spec", "test"]

    protected Project getTarget() { project.project(":target") }

    protected Project getReceiver() { project.project(":receiver") }

    void setup() {
        applyTo(
                addSubproject("target"),
                addSubproject("receiver")
        )
        project.subprojects {
            implementations {
                impl
            }
        }
    }

    @Unroll
    def "should add `#source`"() {
        given:
        receiver.require.from(":target"){
            sources source
        }

        expect:
        assertRequired(source)

        where:
        source << ["api", "main", "impl", "test"]
    }

    @Unroll
    def "should when add template, add `#source` to `#to` by compile"() {
        given:
        receiver.require.from(":target").template "impl"

        expect:
        assertRequired(Scope.COMPILE, to, source)

        where:
        source | to
        "api"  | "api"
        "main" | "main"
    }


    @Unroll
    def "should when add template, add `#source` to `#to` by runtime"() {
        given:
        receiver.require.from(":target").template "impl"

        expect:
        assertRequired(Scope.RUNTIME, to, source)

        where:
        source | to
        "api"  | "api"
        "main" | "main"
        "impl" | "test"
    }

    def "should late depend, when source set is not exist"() {
        given:
        receiver.require.from(":target"){
            sources "yetUnknown", "main"
        }

        def targetConfigurations = target.project.configurations

        expect:
        !targetConfigurations.findByName("yetUnknownCompile")

        when:
        target.describe {
            yetUnknown
        }
        then:
        targetConfigurations.findByName("yetUnknownCompile")

        and:
        assertRequired "main", "yetUnknown"
    }
}