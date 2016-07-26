package su.jfdev.gradle.service

import nebula.test.PluginProjectSpec

abstract class ServicePluginSpec extends PluginProjectSpec {
    String pluginName = "su.jfdev.gradle.service"

    void setup(){
        project.plugins.apply("java")
        project.plugins.apply(ServicePlugin)
    }
}