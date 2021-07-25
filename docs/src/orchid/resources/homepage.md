---
layout: homepage
relativePriority: 1.0
components:
  - template: '1-hero'
    backgroundColor: light
    sectionTitle: ''
  - template: '2-what-is-orchid'
    backgroundColor: dark
    sectionSize: 'medium'
    sectionTitle: 'What Is Orchid?'
  - template: '3-latest-news'
    backgroundColor: light
    sectionSize: 'small'
    sectionTitle: 'Latest News'
  - template: '4-get-started'
    backgroundColor: dark
    sectionSize: 'medium'
    sectionTitle: 'Ready to Get Started?'
    sectionSubtitle: "Let's make something beautiful"
    cards:
      - itemId: 'Your First Orchid Site'
        title: 'Learn the Basics'
        mediaUrl: 'assets/media/undraw_in_progress_ql66.svg'
      - itemId: 'How to Document a Kotlin Project'
        title: 'Document a Code Project'
        mediaUrl: 'assets/media/undraw_code_review_l1q9.svg'
      - itemId: 'How to Write a Blog'
        title: 'Make a Personal Blog'
        mediaUrl: 'assets/media/undraw_content_vbqo.svg'
  - template: '5-features'
    backgroundColor: light
    sectionSize: 'medium'
    sectionTitle: 'Features'
    sectionSubtitle: 'Everything you need to author, build, and deploy documentation sites, top-soil included'
    backgroundImage: 'assets/media/undraw_maker_launch_crhe.svg'
    subsections:
      - title: 'Document any language'
        bullets: 
          - { title: 'Java', itemId: 'orchid-javadoc-feature', icon: 'assets/svg/java.svg', iconAttribution: 'Icons made by https://www.flaticon.com/authors/freepik from https://www.flaticon.com' }
          - { title: 'Kotlin', itemId: 'orchid-kotlindoc-feature', icon: 'assets/svg/kotlin.svg', iconAttribution: 'https://commons.wikimedia.org/wiki/File:Kotlin-logo.svg' }
          - { title: 'Groovy', itemId: 'orchid-groovydoc-feature', icon: 'assets/svg/groovy.svg', iconAttribution: 'https://commons.wikimedia.org/wiki/File:Groovy-logo.svg' }
          - { title: 'Swift', itemId: 'orchid-swiftdoc-feature', icon: 'assets/svg/swift.svg', iconAttribution: 'https://commons.wikimedia.org/wiki/File:Swift_logo.svg' }
          - { title: 'CSS', itemId: 'orchid-kss-feature', icon: 'assets/svg/css.svg', iconAttribution: 'https://commons.wikimedia.org/wiki/File:CSS3_logo_and_wordmark.svg' }
      - title: 'Do more with tons of plugins'
        bullets:           
          - { title: 'Blog posts', itemId: 'orchid-posts-feature' }
          - { title: 'Wikis', itemId: 'orchid-wiki-feature' }
          - { title: 'Changelogs', itemId: 'orchid-changelog-feature' }
          - { title: 'Code Documentation', itemId: 'orchid-sourcedoc-feature' }
          - { title: 'Client-side search', itemId: 'orchid-search-feature' }
      - title: 'Connect to your existing tools'
        bullets: 
          - { title: 'GitHub Wikis', itemId: 'orchid-github-feature', icon: 'assets/svg/github.svg', iconAttribution: 'https://github.com/logos' }
          - { title: 'Swagger API Definitions', itemId: 'orchid-swagger-feature', icon: 'assets/svg/swagger.svg', iconAttribution: 'https://swagger.io/' }
          - { title: 'Netlify Forms', itemId: 'orchid-forms-feature', icon: 'assets/svg/netlify.svg', iconAttribution: 'https://www.netlify.com/press/' }
          - { title: 'Algolia DocSearch', itemId: 'orchid-search-feature', icon: 'assets/svg/algolia.svg', iconAttribution: 'https://www.algolia.com/press/?section=brand-guidelines' }
      - title: 'Pain-free integrated deployment'
        bullets: 
          - { title: 'GitHub Pages', itemId: 'orchid-github-feature', icon: 'assets/svg/github.svg', iconAttribution: 'https://github.com/logos' }
          - { title: 'Gitlab Pages', itemId: 'orchid-gitlab-feature', icon: 'assets/svg/gitlab.svg', iconAttribution: 'https://about.gitlab.com/press/press-kit/' }
          - { title: 'Bitbucket Cloud', itemId: 'orchid-bitbucket-feature', icon: 'assets/svg/bitbucket.svg', iconAttribution: 'https://www.atlassian.com/company/news/press-kit' }
          - { title: 'Netlify', itemId: 'orchid-netlify-feature', icon: 'assets/svg/netlify.svg', iconAttribution: 'https://www.netlify.com/press/' }
  - template: '6-connect'
    backgroundColor: primary
    sectionSize: 'medium'
    sectionTitle: 'Get Connected'
    
extraCss:
    - 'inline:.scss:section.section.component .container.content { /*zoom: 80%;*/ }'
---
