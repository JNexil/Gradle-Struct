package su.jfdev.gradle.service

import nebula.test.IntegrationSpec

class ServiceFileSpec extends IntegrationSpec {
    String text = """
buildscript {
    dependencies {
        classpath files('../../../../build/classes/main')
    }
}
${applyPlugin(ServicePlugin.class)}
services {
    describe {
        impl impl: ["super.class": ["under.class"]],
            dual: ["other.super.class": ["under.class", "second.under"]]
    }
}
"""

    void setup() {
        buildFile << text
        runTasksSuccessfully("build")
    }

    def "should create file at resources"() {
        when:
        def file = new File(projectDir, "build/resources/$sources/META-INF/services/$service")
        then:
        file.exists()

        when:
        def lines = file.readLines().collect { it.trim() }

        then:
        lines.containsAll(implementations)
        lines.size() == implementations.size()

        where:
        sources | service             | implementations
        "impl"  | "super.class"       | ["under.class"]
        "dual"  | "other.super.class" | ["under.class", "second.under"]
    }
}