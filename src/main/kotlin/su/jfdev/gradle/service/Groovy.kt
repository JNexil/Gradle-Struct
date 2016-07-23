package su.jfdev.gradle.service

import org.codehaus.groovy.runtime.*

fun Any.find(name: String): Any? = InvokerHelper.getProperty(this, name)