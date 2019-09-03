dependencies {
    compile(Projects.OrchidCore(this))

    compile(Projects.Themes.OrchidEditorial(this))

    compile(Projects.Plugins.OrchidPages(this))
    compile(Projects.Plugins.OrchidWiki(this))
    compile(Projects.Plugins.OrchidForms(this))
    compile(Projects.Plugins.OrchidChangelog(this))
    compile(Projects.Plugins.OrchidSearch(this))

    compile(Projects.LanguageExtensions.OrchidDiagrams(this))
    compile(Projects.LanguageExtensions.OrchidSyntaxHighlighter(this))
}
