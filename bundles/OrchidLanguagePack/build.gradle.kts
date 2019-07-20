
dependencies {
    (rootProject.ext.get("languageExtensionProjects") as Iterable<Project>).forEach { compile(it) }
}
