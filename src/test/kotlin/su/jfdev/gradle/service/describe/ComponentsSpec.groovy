package su.jfdev.gradle.service.describe

import su.jfdev.gradle.service.spec.ServiceComponentsSpec
import su.jfdev.gradle.service.util.ContainsChecker

import static su.jfdev.gradle.service.util.ProjectCheckerKt.*

class ComponentsSpec {
    static class KnownSourceSets extends ServiceComponentsSpec {
        ContainsChecker checker = getKnownSources(project)
        Iterable<String> items = ["myApi", "mySpec", "myImpl"]
    }

    static class UnknownSourceSets extends ServiceComponentsSpec {
        ContainsChecker checker = getUnknownSources(project)
        Iterable<String> items = ["api", "spec", "impl"]
    }
}