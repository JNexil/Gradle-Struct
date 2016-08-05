package su.jfdev.gradle.service.apply

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.ServiceSpec

import static su.jfdev.gradle.service.describe.Scope.COMPILE
import static su.jfdev.gradle.service.describe.Scope.RUNTIME
import static su.jfdev.gradle.service.require.RequireSpec.ALL
import static su.jfdev.gradle.service.util.Checking.getKnownConfigurations
import static su.jfdev.gradle.service.util.Checking.getKnownSources

class ApplySpec extends ServiceSpec {
    Project getTarget() { project }

    Project getReceiver() { project }

    def "should contains default sources"() {
        when:
        def known = getKnownSources(project)

        then:
        source in known

        where:
        source << ["api", "main", "test"]
    }

    def "should contains configurations from given source"() {
        given:
        def checking = getKnownConfigurations(project)

        expect: "should contains runtime configuration"
        RUNTIME[mainName] in checking

        and: "should contains compile configuration"
        COMPILE[mainName] in checking

        where:
        mainName << ["api", "main", "test"]
    }

    @Unroll
    def "should has hierarchy: #receiver <- #target, but not reverse"() {
        given:
        def exclusions = ALL - target - receiver
        def requiring = requiring(scope: COMPILE, receiverSrc: receiver)

        expect:
        requiring.assertNonRequired(exclusions)

        and:
        requiring.assertRequired(target)


        where:
        receiver | target
        "api"    | []
        "main"   | ["api"]
    }
}