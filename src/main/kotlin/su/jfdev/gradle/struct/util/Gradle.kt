package su.jfdev.gradle.struct.util

import groovy.lang.*
import org.gradle.util.*

infix fun <T> T.configure(delegate: Closure<*>) = apply {
    ConfigureUtil.configure(delegate, this)
}