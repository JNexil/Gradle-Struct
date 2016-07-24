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
        new SourceSets(project)
    }

    def "should contains apiCompile, when api is disabled"() {
        given:
        useDisabledConfiguration()

        expect:
        project.configurations.findByName("apiCompile") != null
    }
}