package su.jfdev.gradle.service.describe

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.ServiceSpec

import static su.jfdev.gradle.service.describe.Scope.COMPILE
import static su.jfdev.gradle.service.describe.Scope.RUNTIME
import static su.jfdev.gradle.service.require.RequireSpec.ALL
import static su.jfdev.gradle.service.util.Checking.getKnownConfigurations
import static su.jfdev.gradle.service.util.Checking.getKnownSources

class ComponentsSpec extends ServiceSpec {
    protected Project getTarget() { project }

    protected Project getReceiver() { project }

    def "should contains default sources"() {
        when:
        def known = getKnownSources(project)

        then:
        source in known

        where:
        source << ["api", "spec", "impl"]
    }

    def "should contains configurations from given source"() {
        given:
        def checking = getKnownConfigurations(project)

        expect: "should contains runtime configuration"
        RUNTIME[mainName] in checking

        and: "should contains compile configuration"
        COMPILE[mainName] in checking

        where:
        mainName << ["api", "spec", "impl"]
    }

    @Unroll
    def "should has hierarchy: #receiver <- #target, but not reverse"() {
        given:
        def exclusions = ALL - target - receiver
        expect:
        assertNonRequired(COMPILE, receiver, exclusions)

        and:
        assertRequired(COMPILE, receiver, target)


        where:
        receiver | target
        "api"    | []
        "main"   | ["api"]
        "impl"   | ["api", "main"]
        "spec"   | ["api", "main"]
        "test"   | ["api", "main", "spec", "impl"]
    }
}