package su.jfdev.gradle.service.describe

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.ServiceSpec

import static su.jfdev.gradle.service.describe.Scope.COMPILE
import static su.jfdev.gradle.service.describe.Scope.RUNTIME
import static su.jfdev.gradle.service.util.Checking.getKnownConfigurations
import static su.jfdev.gradle.service.util.Checking.getKnownSources

class ImplementationSpec extends ServiceSpec {

    Project getTarget() { project }

    Project getReceiver() { project }

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
        String compile = COMPILE[source]

        then:
        compile in knownConfiguration

        when: "should contains runtime configurations"
        String runtime = RUNTIME[source]

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

        def requiring = requiring(scope: COMPILE, receiverSrc: receiver)

        expect: "compile"
        requiring.assertNonRequired(exclusions)

        and:
        requiring.assertRequired(target)

        and: "runtime"
        requiring.assertNonRequired(exclusions)

        and:
        requiring.assertRequired(target)


        where:
        receiver << ["impl", "alt"]
    }
}