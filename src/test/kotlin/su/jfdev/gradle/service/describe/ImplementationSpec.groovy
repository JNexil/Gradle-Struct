package su.jfdev.gradle.service.describe

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.ServiceSpec
import su.jfdev.gradle.service.util.PackKt

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
    def "`#source` should depend `main`"() {
        expect:
        isRequired(Scope.COMPILE, source)

        and:
        isRequired(Scope.RUNTIME, source)

        where:
        source << ["impl", "alt"]
    }



    boolean isRequired(Scope scope, String source){
        def configuration = scope[source]
        def $configuration = project.configurations.getByName(configuration)
        def dependency = PackKt.get(project, "main").get(scope)
        $configuration.allDependencies.any { it == dependency }
    }
}