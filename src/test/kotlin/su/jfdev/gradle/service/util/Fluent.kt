package su.jfdev.gradle.service.util

import org.junit.*


infix fun Any.`should equal`(expected: Any) = Assert.assertEquals(expected, this)