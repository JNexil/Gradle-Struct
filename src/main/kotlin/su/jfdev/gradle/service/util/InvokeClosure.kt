package su.jfdev.gradle.service.util

import org.codehaus.groovy.runtime.*

fun <T, R> closure(function: (T) -> R) = MethodClosure(function, "invoke")
