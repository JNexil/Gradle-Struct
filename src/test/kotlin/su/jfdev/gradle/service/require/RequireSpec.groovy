package su.jfdev.gradle.service.require

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import spock.lang.Unroll
import su.jfdev.gradle.service.describe.ConfigurationDependency
import su.jfdev.gradle.service.describe.Scope
import su.jfdev.gradle.service.spec.ServiceSpock

import static su.jfdev.gradle.service.describe.Scope.COMPILE
import static su.jfdev.gradle.service.describe.Scope.RUNTIME

class RequireSpec extends ServiceSpock {
    public static final ALL = ["api", "main", "impl", "spec", "test"]
    Project target
    Project receiver

    @Override
    void setup() {
        serviceTo(
                target = addSubproject("target"),
                receiver = addSubproject("receiver")
        )
        target.service {
            api()
            spec()
            impl()
        }
        receiver.service {
            api()
            spec()
            impl()
        }
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
    def "should when add service, add `#source` with #requiredFrom without #nonRequiredFrom"() {
        given:
        receiver.require.service ":target"

        expect:
        wasRequired(scope, source)

        where:
        source | scope
        "api"  | COMPILE
        "main" | COMPILE
        "impl" | RUNTIME
        "spec" | COMPILE
    }

    private void assertNonRequired(Scope scope, String receiver, List<String> target) {
        for (String fromEntry : target)
            assert wasNotRequired(scope, receiver, fromEntry)
    }

    private void assertRequired(Scope scope, String receiver, List<String> target) {
        for (String fromEntry : target)
            assert wasRequired(scope, receiver, fromEntry)
    }

    private boolean wasNotRequired(Scope scope, String receiver, String target = receiver) {
        !wasRequired(scope, receiver, target)
    }

    private boolean wasRequired(Scope scope, String receiver, String target = receiver) {
        String $receiver = scope[receiver]
        String $target = scope[target]
        def configuration = this.receiver.configurations.getByName($receiver)
        isRequired(configuration, $target)
    }

    private boolean isRequired(Configuration configuration, String $target) {
        configuration.dependencies.any {
            if (it instanceof ConfigurationDependency && it.target.path == this.target.path) {
                def isTarget = it.configuration.name == $target
                isTarget || isRequired(it.configuration, $target)
            } else false
        }
    }
}