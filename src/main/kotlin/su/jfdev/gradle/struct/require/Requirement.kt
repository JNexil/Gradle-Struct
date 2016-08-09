package su.jfdev.gradle.struct.require

import org.gradle.api.*
import su.jfdev.gradle.struct.describe.Scope.*
import su.jfdev.gradle.struct.require.Require.*
import su.jfdev.gradle.struct.util.*

sealed class Requirement {
    abstract infix fun require(request: Request)

    class External(val pattern: String): Requirement() {
        override fun require(request: Request) {
            val (receiver, target, scope) = request
            val configuration = receiver[scope ?: COMPILE]
            val formattedTarget = pattern.format(target)
            configuration.dependencies += receiver.project.dependencies.create(formattedTarget)
        }
    }

    class Module(val project: Project): Requirement() {
        override fun require(request: Request) {
            val (receiver, target, scope) = request
            val _target = project[target]
            when (scope) {
                null -> receiver depend _target
                else -> receiver.depend(_target, scope)
            }
        }
    }
}