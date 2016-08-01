package su.jfdev.gradle.service.describe

import spock.lang.Unroll
import su.jfdev.gradle.service.util.ServiceSpec

class ServiceDescribeSpec extends ServiceSpec {

    def "Project should contain extension with Closure"() {
        when:
        def service = project.ext.service
        then:
        service instanceof Closure
    }

    @Unroll
    def "Should know `#known`, but not `#unknown`"() {
        given:
        project.service {
            api "myApi"
            spec "mySpec"
            impl "myImpl"
        }

        expect:
        known in knownSources
        unknown in unknownSources

        where:
        known << ["myApi", "mySpec", "myImpl"]
        unknown << ["api", "spec", "impl"]
    }

}