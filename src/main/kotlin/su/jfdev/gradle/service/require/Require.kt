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
        sources("impl", allowed = *implementations, to = "test")
    }

    @over fun compile(name: String, to: String = name) = source(name, to, COMPILE)

    @over fun runtime(name: String, to: String = name) = source(name, to, RUNTIME)

    @over fun source(name: String, to: String = name, scope: Scope = COMPILE) {
        val target = target[name]
        val receiver = receiver[to]
        receiver.depend(target, scope)
    }

    @over fun sources(name: String, to: String = name, scope: Scope = COMPILE, vararg allowed: String) {
        val target = target.target(name)
        val allAllowed = allowed.toList().orNull ?: default(name)
        for (pack in target)
            if (pack.name in allAllowed)
                source(name, to, scope)
    }

    fun doCall(function: Closure<*>){
        val closure = function.clone() as Closure<*>
        closure.delegate = this
        closure.call()
    }

    private fun default(name: String): Collection<String> {
        val def = target.service.packs[name]?.firstOrNull() ?: return emptyList()
        return listOf(def).map { it.name }
    }
}