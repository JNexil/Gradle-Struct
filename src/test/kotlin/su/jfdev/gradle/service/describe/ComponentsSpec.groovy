package su.jfdev.gradle.service.describe

import spock.lang.Unroll
import su.jfdev.gradle.service.spec.ServiceSpock
import su.jfdev.gradle.service.util.PropertiesKt

import static su.jfdev.gradle.service.describe.Scope.COMPILE
import static su.jfdev.gradle.service.describe.Scope.RUNTIME
import static su.jfdev.gradle.service.util.Checking.*
import static su.jfdev.gradle.service.util.HierarchyChecker.depend
import static su.jfdev.gradle.service.util.HierarchyChecker.nonDepend

class ComponentsSpec extends ServiceSpock {
    def getSources() { PropertiesKt.getSourceSets(project) }

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
    def "should has hierarchy: #from -> #to, but not reverse"() {
        given:
        def depend = depend(project)
        def nonDepend = nonDepend(project)

        expect:
        depend.invoke(RUNTIME, from, to)
        depend.invoke(COMPILE, from, to)

        and: "reverse"
        nonDepend.invoke(RUNTIME, to, from)
        nonDepend.invoke(COMPILE, to, from)

        where:
        from   | to
        "api"  | "main"
        "api"  | "impl"
        "api"  | "spec"
        "api"  | "test"

        "main" | "impl"
        "main" | "test"
        "main" | "spec"

        "impl" | "test"

        "spec" | "test"
    }

    private void buildPack(mainName, source = mainName) {
        project.service {
            delegate[mainName].call source
        }
    }
}