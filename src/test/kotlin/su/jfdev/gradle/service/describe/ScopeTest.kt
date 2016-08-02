package su.jfdev.gradle.service.describe

import org.junit.*
import su.jfdev.gradle.service.describe.Scope.*
import su.jfdev.gradle.service.util.*

class ScopeTest {
    @Test fun `should make configuration name by source set name`() {
        COMPILE["api"] `should equal` "apiCompile"
        RUNTIME["api"] `should equal` "apiRuntime"
        COMPILE["anyThing"] `should equal` "anyThingCompile"
    }
}