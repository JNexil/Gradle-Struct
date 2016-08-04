package su.jfdev.gradle.service.describe

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.ServiceSpec

import static su.jfdev.gradle.service.describe.Scope.COMPILE
import static su.jfdev.gradle.service.describe.Scope.RUNTIME
import static su.jfdev.gradle.service.util.Checking.getKnownConfigurations
import static su.jfdev.gradle.service.util.Checking.getKnownSources

class ImplementationSpec extends ServiceSpec {

    protected Project getTarget() { project }

    protected Project getReceiver() { project }

    @Override
    void setup() {
        project.service "impl", "alt"
    }

    @Unroll
    def "should contains source set `#source`"() {
        when:
        def knownSources = getKnownSources(project)

        then:
        source in knownSources

        where:
        source << ["impl", "alt"]
    }

    @Unroll
    def "should contains runtime and compile configurations to `#source`"() {
        given:
        def knownConfiguration = getKnownConfigurations(project)

        when: "should contains compile configurations"
        String compile = Scope.COMPILE[source]

        then:
        compile in knownConfiguration

        when: "should contains runtime configurations"
        String runtime = Scope.RUNTIME[source]

        then:
        runtime in knownConfiguration

        where:
        source << ["impl", "alt"]
    }

    @Unroll
    def "should has hierarchy: #receiver <- [api,main], but not [test]"() {
        given:
        def target = ["api", "main"]
        def exclusions = ["api", "main", "impl", "alt", "test"] - target - receiver

        expect: "compile"
        assertNonRequired(COMPILE, receiver, exclusions)

        and:
        assertRequired(COMPILE, receiver, target)

        and: "runtime"
        assertNonRequired(RUNTIME, receiver, exclusions)

        and:
        assertRequired(RUNTIME, receiver, target)


        where:
        receiver << ["impl", "alt"]
    }
}