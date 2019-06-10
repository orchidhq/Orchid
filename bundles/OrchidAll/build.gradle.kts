dependencies {
    (rootProject.ext.get("mainProjects") as Iterable<Project>).forEach { compile(it) }
}
