package su.jfdev.gradle.struct.require

import groovy.lang.*
import org.gradle.api.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.describe.Scope.*
import su.jfdev.gradle.struct.util.*
import kotlin.jvm.JvmOverloads as over

class Require(val receiver: Project, val target: Requirement): Closure<Any>(Unit) {
    fun inherit(vararg implementations: String) {
        runtime("test")
        template(*implementations)
    }

    fun template(vararg implementations: String) {
        sources("api")
        sources("main")
        test(*implementations)
    }


    fun test(vararg names: String) {
        for (name in names) test(name)
    }

    fun test(name: String) {
        sources(name, "test")
    }

    @over fun compile(name: String, to: String = name) = source(name, to, COMPILE)

    @over fun runtime(name: String, to: String = name) = source(name, to, RUNTIME)

    @over fun source(name: String, to: String = name, scope: Scope = COMPILE) {
        val receiver = receiver[to]
        target require Request(receiver, name, scope)
    }

    @over fun sources(name: String, to: String = name) {
        val receiver = receiver[to]
        target require Request(receiver, name)
    }

    infix fun doCall(function: Closure<*>) = apply {
        val closure = function.clone() as Closure<*>
		closure.apply {
			delegate = this
			resolveStrategy = Closure.DELEGATE_FIRST
			call()
		}
    }

    data class Request(val receiver: Pack, val target: String, val scope: Scope? = null)
}