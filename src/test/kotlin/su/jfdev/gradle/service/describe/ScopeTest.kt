package su.jfdev.gradle.service.describe

import org.junit.*
import org.junit.Assert.*
import su.jfdev.gradle.service.describe.Scope.*

class ScopeTest {
    @Test fun `should make configuration name by source set name`() {
        COMPILE["api"] `should equal` "apiCompile"
        RUNTIME["api"] `should equal` "apiRuntime"
        COMPILE["anyThing"] `should equal` "anyThingCompile"
    }

    infix fun Any.`should equal`(expected: Any) = assertEquals(expected, this)
}