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
        project.implementations {
            impl
            alt
        }
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
    def "should has hierarchy: [#target] <- [#receiver]"() {
        when:
        def requiring = requiring(scope: COMPILE, receiverSrc: receiver, targetSrc: target)

        then: "compile"
        requiring.isRequired()

        when:
        requiring = requiring.with(scope: RUNTIME)

        then:
        requiring.isRequired()


        where:
        receiver | target
        "test"   | "impl"
        "test"   | "alt"

        "impl"   | "main"
        "alt"    | "main"
    }
}