package su.jfdev.gradle.service.describe

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.ServiceSpec

import static su.jfdev.gradle.service.describe.Scope.COMPILE
import static su.jfdev.gradle.service.describe.Scope.RUNTIME
import static su.jfdev.gradle.service.require.RequireSpec.ALL
import static su.jfdev.gradle.service.util.Checking.*

class ComponentsSpec extends ServiceSpec {
    protected Project getTarget() { project }

    protected Project getReceiver() { project }

    def "should not contains sources without sources {}"() {
        when:
        def unknown = getUnknownSources(project)

        then:
        source in unknown

        where:
        source << ["api", "spec", "impl"]
    }

    def "should contains default sources"() {
        given:
        buildPack source

        when:
        def known = getKnownSources(project)

        then:
        source in known

        where:
        source << ["api", "spec", "impl"]
    }

    def "should contains source custom source, but not default"() {
        given:
        buildPack mainName, source

        when: "Pack with #mainName as name shouldn't be in sourceSets"
        def unknown = getUnknownConfigurations(project)

        then:
        mainName in unknown

        when:
        def known = getKnownSources(project)

        then:
        source in known

        where:
        mainName | source
        "api"    | "myfirst"
        "spec"   | "second"
        "impl"   | "third"
    }

    def "should contains custom and dummy configurations from given source"() {
        given:
        def checking = getKnownConfigurations(project)
        buildPack mainName, source

        expect: "should contains dummy runtime configuration"
        RUNTIME[mainName] in checking

        and: "should contains dummy compile configuration"
        COMPILE[mainName] in checking

        and: "should contains custom runtime configuration"
        RUNTIME[source] in checking

        and: "should contains custom compile configuration"
        COMPILE[source] in checking

        where:
        mainName | source
        "api"    | "first"
        "spec"   | "second"
        "impl"   | "third"

        "api"    | "api"
        "spec"   | "spec"
        "impl"   | "impl"
    }

    @Unroll
    def "should has hierarchy: #source <- #transitive, but not reverse"() {
        given:
        def exclusions = ALL - transitive - source

        expect:
        assertNonRequired(COMPILE, source, exclusions)

        and:
        assertRequired(COMPILE, source, transitive)


        where:
        source | transitive
        "api"  | []
        "main" | ["api"]
        "impl" | ["api", "main"]
        "spec" | ["api", "main"]
        "test" | ["api", "main", "spec", "impl"]
    }

    private void buildPack(mainName, source = mainName) {
        project.service {
            delegate[mainName].call source
        }
    }
}