package su.jfdev.gradle.service.require

import groovy.lang.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.describe.Scope.*
import su.jfdev.gradle.service.util.*

class Require(val receiver: Module, val target: Module) {
    fun service(vararg implementations: String) {
        source("api")
        source("main")
        source("spec")
        for (implementation in implementations)
            source(implementation, to = "test", scope = RUNTIME)
    }

    fun source(name: String, scope: String)
        = source(name, name, scope)

    fun source(name: String, scope: Scope)
            = source(name, name, scope)

    fun source(name: String, to: String, scope: String) {
        val scopeEnum = Scope[scope]
        source(name, to, scopeEnum)
    }

    fun source(name: String, to: String = name, scope: Scope = COMPILE) {
        val receiver = receiver[name]
        val target = target.getMain(to)
        for (pack in receiver)
            pack.depend(scope, target)
    }

    fun call(function: Closure<*>){
        val closure = function.clone() as Closure<*>
        closure.delegate = this
        closure.call()
    }
}