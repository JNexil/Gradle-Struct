package su.jfdev.gradle.service.describe

import spock.lang.Unroll
import su.jfdev.gradle.service.spec.ServiceSpock

import static su.jfdev.gradle.service.util.Checking.getKnownSources

class ComponentsSpec extends ServiceSpock {

    @Unroll
    def "should contains source `#item`"(item) {
        given:
        project.service {
            api item
        }

        when:
        def checking = getKnownSources(project)

        then:
        item in checking

        where:
        item << ["myfirst", "second", "third", "api"]
    }
}