package su.jfdev.gradle.service

import org.codehaus.groovy.runtime.*

inline operator fun <reified T> Any.get(name: String): T = InvokerHelper.getProperty(this, name) as T