package su.jfdev.gradle.service.require

import groovy.lang.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.describe.Scope.*
import su.jfdev.gradle.service.util.*
import kotlin.jvm.JvmOverloads as over

class Require(val receiver: Module, val target: Module): Closure<Any>(Unit) {

    fun service(vararg implementations: String) {
        source("api")
        source("main")
        source("spec")
        val orDefault = if(implementations.isEmpty()) defaultImplementation else implementations.toList()
        for (implementation in orDefault)
            source(implementation, to = "test")
    }


    @over fun compile(name: String, to: String = name) = source(name, to, COMPILE)

    @over fun runtime(name: String, to: String = name) = source(name, to, RUNTIME)

    @over fun source(name: String, to: String = name, scope: Scope = COMPILE) {
        val receiver = receiver[name]
        val target = target.target(to)
        for (pack in receiver)
            pack.depend(scope, target)
    }

    fun doCall(function: Closure<*>){
        val closure = function.clone() as Closure<*>
        closure.delegate = this
        closure.call()
    }

    private val defaultImplementation: Collection<String> get() {
        val def = target.service.impl.firstOrNull() ?: return emptyList()
        return listOf(def).map { it.name }
    }
}