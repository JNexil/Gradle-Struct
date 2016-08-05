package su.jfdev.gradle.service.require

import groovy.transform.ToString
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import su.jfdev.gradle.service.describe.Pack
import su.jfdev.gradle.service.describe.Scope

import static su.jfdev.gradle.service.util.PackKt.get

@ToString
class Requiring {
    Scope scope = Scope.COMPILE
    Project receiver
    Project target

    String receiverSrc
    String targetSrc = null

    Requiring copy() {
        new Requiring(
                scope: scope,
                receiver: receiver,
                target: target,
                receiverSrc: receiverSrc,
                targetSrc: targetSrc
        )
    }

    Requiring with(Map<String, Object> args) {
        def copy = copy()
        for (arg in args) copy[arg.key] = arg.value
        copy
    }

    Requiring with(String target) {
        with(targetSrc: target)
    }

    void assertNonRequired(Iterable<String> targets) {
        for (target in targets.collect { with(it) }) assert target.isNotRequired()
    }

    void assertRequired(Iterable<String> targets) {
        for (target in targets.collect { with(it) }) assert target.isRequired()
    }

    boolean isNotRequired() {
        !isRequired()
    }

    boolean isRequired() {
        Pack $receiver = get(receiver, receiverSrc ?: targetSrc)
        Pack $target = get(target, targetSrc ?: receiverSrc)
        isRequired(scope, $receiver, $target)
    }

    static boolean isRequired(Scope scope, Pack receiver, Pack target) {
        def $receiver = receiver[scope]
        def $target = target[scope]
        isRequired($receiver, $target)
    }

    static boolean isRequired(Configuration receiver, Configuration target) {
        target in receiver.hierarchy
    }
}