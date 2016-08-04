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
    }

    @Unroll
    def "should add `#source` with #transitive"() {
        given:
        receiver.require.from(":target").compile source
        def exclusions = ALL - transitive - source

        expect:
        wasRequired(COMPILE, source)

        and:
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

    @Unroll
    def "should when add service, add `#source` to `#to`"() {
        given:
        receiver.require.service ":target"

        expect:
        wasRequired(COMPILE, to, source)

        where:
        source | to
        "api"  | "api"
        "main" | "main"
        "impl" | "test"
        "spec" | "spec"
    }


}