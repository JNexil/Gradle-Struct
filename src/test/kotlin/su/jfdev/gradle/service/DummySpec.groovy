package su.jfdev.gradle.service

class DummySpec extends ServicePluginSpec {
    def "should contains apiCompile and other always"() {
        expect:
        project.configurations.findByName("apiCompile") != null
        project.configurations.findByName("apiRuntime") != null

        project.configurations.findByName("specCompile") != null
        project.configurations.findByName("specRuntime") != null

        project.configurations.findByName("implCompile") != null
        project.configurations.findByName("implRuntime") != null
    }
}