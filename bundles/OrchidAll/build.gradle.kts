dependencies {
    compile(Projects.OrchidCore(this))

    Projects.Plugins.all(this).forEach { compile(it) }
    Projects.Themes.all(this).forEach { compile(it) }
    Projects.LanguageExtensions.all(this).forEach { compile(it) }
    Projects.Integrations.all(this).forEach { compile(it) }
}
