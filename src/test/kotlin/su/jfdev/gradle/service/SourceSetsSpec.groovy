package su.jfdev.gradle.service

class SourceSetsSpec extends ServicePluginSpec {
    def "should contains apiCompile and other always"() {
        given:
        project.services {
            apiSources = false
        }

        expect:
        project.configurations.findByName("apiCompile") != null
        project.configurations.findByName("apiRuntime") != null

        project.configurations.findByName("specCompile") != null
        project.configurations.findByName("specRuntime") != null

        project.configurations.findByName("implCompile") != null
        project.configurations.findByName("implRuntime") != null
    }
}