dependencies {
    compile(Projects.OrchidCore(this))

    compile(Projects.Plugins.OrchidPosts(this))
    compile(Projects.Plugins.OrchidPages(this))
    compile(Projects.Plugins.OrchidForms(this))
    compile(Projects.Plugins.OrchidTaxonomies(this))

    Projects.LanguageExtensions.all(this).forEach { compile(it) }
}
