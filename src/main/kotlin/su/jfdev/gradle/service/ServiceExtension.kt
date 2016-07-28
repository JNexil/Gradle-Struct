package su.jfdev.gradle.service

import groovy.lang.*
import org.gradle.api.*
import su.jfdev.gradle.service.additional.*
import su.jfdev.gradle.service.implementation.*
import su.jfdev.gradle.service.require.*
import su.jfdev.gradle.service.util.*

open class ServiceExtension(val project: Project) {
    val sourceSets = project.sourceSets
    val describe = AdditionalDescriber(this)
    val implementations = ImplementDescriber(this)
    val require = ParallelRequireUser(this)

    fun require(closure: Closure<Any>) = closure.delegate(require)
    fun describe(closure: Closure<Any>) = closure.delegate(describe)

    private fun Closure<Any>.delegate(receiver: Any) {
        delegate = receiver
        resolveStrategy = Closure.DELEGATE_FIRST
        call()
    }
}