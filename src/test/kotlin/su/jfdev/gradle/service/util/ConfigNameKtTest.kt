package su.jfdev.gradle.service.util

import org.junit.*
import org.junit.Assert.*

class ConfigNameKtTest {

    @Test fun makeConfigName() {
        assertEquals("testCompile", makeConfigName("test","compile"))
        assertEquals("testRuntime", makeConfigName("test","runtime"))

        assertEquals("testCompile", makeConfigName("Test","compile"))
        assertEquals("testRuntime", makeConfigName("Test","runtime"))

        assertEquals("testCompile", makeConfigName("Test","Compile"))
        assertEquals("testRuntime", makeConfigName("Test","Runtime"))

        assertEquals("compile", makeConfigName("main","compile"))
        assertEquals("runtime", makeConfigName("main","runtime"))
    }

    @Test fun `camel() should return all in camel case`() {
        assertEquals("testAnything", camel("test", "anything"))
        assertEquals("testAnything", camel("test", "Anything"))
        assertEquals("testAnything", camel("Test", "anything"))
        assertEquals("testAnything", camel("Test", "Anything"))
        assertEquals("testAnytHing", camel("Test", "AnytHing"))

        assertEquals("test", camel("Test"))

        assertEquals("testAnythingOrNone", camel("Test", "Anything", "or","none"))

        assertEquals("", camel())
    }

}