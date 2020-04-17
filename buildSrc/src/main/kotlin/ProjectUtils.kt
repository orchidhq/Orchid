import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.support.delegates.ProjectDelegate

class Module(val path: String)

fun DependencyHandlerScope.implementation(lib: String) =
    add("implementation", lib)

fun DependencyHandlerScope.implementation(vararg modules: Module) =
    dependsOnModules(modules.toList(), configurationName = "implementation")
fun DependencyHandlerScope.testImplementation(vararg modules: Module) =
    dependsOnModules(modules.toList(), configurationName = "testImplementation")

fun DependencyHandlerScope.implementation(modules: Iterable<Module>) =
    dependsOnModules(modules, "implementation")
fun DependencyHandlerScope.testImplementation(modules: Iterable<Module>) =
    dependsOnModules(modules, "testImplementation")

fun DependencyHandlerScope.orchidRuntimeOnly(modules: Iterable<Module>) =
    dependsOnModules(modules, "orchidRuntimeOnly")

fun DependencyHandlerScope.dependsOnModules(modules: Iterable<Module>, configurationName: String = "compile") =
    modules.forEach { add(configurationName, project(it.path)) }

fun List<Module>.tasksNamed(taskName: String) =
    this.map { "${it.path}:$taskName" }.toTypedArray()
fun Collection<Project>.sourceSetsNamed(sourceSetName: String): Array<SourceSet> =
    this.mapNotNull { (it.property("sourceSets") as? SourceSetContainer)?.getByName(sourceSetName) }.toTypedArray()
operator fun List<Module>.invoke(delegate: Project) : List<Project> =
    this.map { delegate.project(it.path) }
