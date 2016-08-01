package su.jfdev.gradle.service.spec

import spock.lang.Unroll
import su.jfdev.gradle.service.util.ContainsChecker

abstract class ServiceComponentsSpec extends ServiceSpec {

    abstract ContainsChecker getChecker()
    abstract Iterable<String> getItems()

    @Unroll
    def "Should #checker.action `#item`"() {
        given:
        project.service {
            api "myApi"
            spec "mySpec"
            impl "myImpl"
        }

        expect:
        item in knownSources

        where:
        item << items
    }
}