package su.jfdev.gradle.service.util

import org.gradle.api.*
import su.jfdev.gradle.service.describe.*

operator fun Project.get(name: String) = Pack(this, name)