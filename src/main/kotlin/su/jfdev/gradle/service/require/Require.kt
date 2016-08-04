package su.jfdev.gradle.service.require

import groovy.lang.*
import org.gradle.api.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.describe.Scope.*
import su.jfdev.gradle.service.util.*
import kotlin.jvm.JvmOverloads as over

class Require(val receiver: Project, val target: Project): Closure<Any>(Unit) {

    fun service(vararg implementations: String) {
        source("api")
        source("main")
        test(*implementations)
    }

    fun test(vararg names: String) = names.forEach {
        sources(it, "test")
    }

    @over fun compile(name: String, to: String = name) = source(name, to, COMPILE)

    @over fun runtime(name: String, to: String = name) = source(name, to, RUNTIME)

    @over fun source(name: String, to: String = name, scope: Scope = COMPILE) {
        val target = target[name]
        val receiver = receiver[to]
        receiver.depend(target, scope)
    }

    @over fun sources(name: String, to: String = name) {
        val target = target[name]
        val receiver = receiver[to]
        receiver depend target
    }

    fun doCall(function: Closure<*>){
        val closure = function.clone() as Closure<*>
        closure.delegate = this
        closure.call()
    }
}