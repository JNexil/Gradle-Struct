package su.jfdev.gradle.service.require

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.ServiceSpec

import static su.jfdev.gradle.service.describe.Scope.COMPILE

class RequireSpec extends ServiceSpec {
    public static final ALL = ["api", "main", "impl", "spec", "test"]

    Project getTarget() { project.project(":target") }

    Project getReceiver() { project.project(":receiver") }

    @Override
    void setup() {
        serviceTo(
                addSubproject("target"),
                addSubproject("receiver")
        )
        project.subprojects {
            implementations {
                impl
            }
        }
    }

    @Unroll
    def "should add `#source` with #transitive"() {
        given:
        receiver.require.from(":target").compile source
        def exclusions = ALL - transitive - source

        def requiring = requiring(scope: COMPILE, receiverSrc: source)

        expect:
        requiring.isRequired()

        when:
        requiring = requiring.with(receiver: target)

        then:
        requiring.assertNonRequired(exclusions)
        requiring.assertRequired(transitive)

        where:
        source | transitive
        "api"  | []
        "main" | ["api"]
        "impl" | ["api", "main"]
        "test" | ["api", "main", "impl"]
    }

    @Unroll
    def "should when add service, add `#source` to `#to`"() {
        given:
        receiver.require.from(":target").service "impl"

        when:
        def request = requiring(scope: COMPILE, receiverSrc: to, targetSrc: source)
        then:
        request.isRequired()

        where:
        source | to
        "api"  | "api"
        "main" | "main"
        "impl" | "test"
    }


}