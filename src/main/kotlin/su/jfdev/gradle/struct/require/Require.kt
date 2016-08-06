package su.jfdev.gradle.struct.require

import groovy.lang.*
import org.gradle.api.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.describe.Scope.*
import su.jfdev.gradle.struct.util.*
import java.util.*
import kotlin.jvm.JvmOverloads as over

class Require(val receiver: Project, val target: Project): Closure<Any>(Unit) {
    val inherited: MutableCollection<String> = ArrayList()

    fun inherit(vararg implementations: String) {
        template(*implementations)
        val ext = receiver.extensions.getByType(RequireExtension::class.java)
        for (require in ext.required) for (inherit in require.inherited) {
            test(inherit)
        }
    }

    fun template(vararg implementations: String) {
        inherited += implementations
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