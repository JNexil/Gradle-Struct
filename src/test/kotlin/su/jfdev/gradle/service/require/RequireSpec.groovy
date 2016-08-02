package su.jfdev.gradle.service.require

import org.gradle.api.Project
import su.jfdev.gradle.service.describe.ConfigurationDependency
import su.jfdev.gradle.service.spec.ServiceSpock

class RequireSpec extends ServiceSpock {
    Project first
    Project second

    @Override
    void setup() {
        first = addSubproject("first")
        second = addSubproject("second")
        serviceTo(first, second)
        first.service {
            api()
            spec()
            impl()
        }
    }

    def "should add single sourceSet"() {
        given:
        second.require.from(":first") {
            compile "api"
        }

        expect:
        wasRequired "apiCompile"
    }

    private boolean wasRequired(String to, String from = to) {
        second.configurations.getByName(to).dependencies.any {
            it instanceof ConfigurationDependency &&
                    it.configuration.name == from &&
                    it.target.path == first.path
        }
    }
}