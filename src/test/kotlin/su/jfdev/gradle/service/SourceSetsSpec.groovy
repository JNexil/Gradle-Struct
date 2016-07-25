package su.jfdev.gradle.service

import nebula.test.PluginProjectSpec
import spock.lang.Ignore

class SourceSetsSpec extends PluginProjectSpec {

    public static final String TEST_FILE = "sourceSetsTest.gradle"

    String pluginName = "su.jfdev.gradle.service"

    @Ignore useDisabledConfiguration(){
        project.plugins.apply("java")
        project.plugins.apply(ServicePlugin)
        project.services {
            apiSources = false
        }
    }

    def "should contains apiCompile and other always"() {
        given:
        useDisabledConfiguration()

        expect:
        project.configurations.findByName("apiCompile") != null
        project.configurations.findByName("apiRuntime") != null

        project.configurations.findByName("specCompile") != null
        project.configurations.findByName("specRuntime") != null

        project.configurations.findByName("implCompile") != null
        project.configurations.findByName("implRuntime") != null
    }
}