import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

class Module(val path: String)

fun DependencyHandlerScope.api(vararg modules: Module) =
    dependsOnModules(modules.toList(), configurationName = "api")
fun DependencyHandlerScope.implementation(vararg modules: Module) =
    dependsOnModules(modules.toList(), configurationName = "implementation")
fun DependencyHandlerScope.testImplementation(vararg modules: Module) =
    dependsOnModules(modules.toList(), configurationName = "testImplementation")

fun DependencyHandlerScope.dependsOnModules(modules: Iterable<Module>, configurationName: String = "compile") =
    modules.forEach { add(configurationName, project(it.path)) }

fun Array<Module>.tasksNamed(taskName: String) =
    this.map { "${it.path}:$taskName" }.toTypedArray()
