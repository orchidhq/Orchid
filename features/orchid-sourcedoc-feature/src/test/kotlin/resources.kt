package com.eden.orchid.sourcedoc

import com.eden.orchid.testhelpers.OrchidIntegrationTest

fun OrchidIntegrationTest.testCss() {
    resource(
        "assets/css/orchidSourceDoc.scss",
        """
            |* {
            |  box-sizing: border-box;
            |}
            |.sourcedoc-page {
            |  display: grid;
            |  grid-template-columns: 70% auto;
            |  
            |  .orchid-sourcedoc {
            |    grid-column: 1;
            |    border: thin solid gray;
            |    padding: 20px 40px;
            |  }
            |  .menus {
            |    grid-column: 2;
            |    border: thin solid gray;
            |    padding: 20px 40px;
            |  }
            |}
            |
            |.orchid-sourcedoc {
            |  
            |  --section-color: black;
            |  .section {
            |    border: thin solid var(--section-color);
            |    border-radius: 8px;
            |    position: relative;
            |    padding: 0 40px;
            |    margin-top: 40px;
            |    margin-bottom: 40px;
            |
            |    &::before {
            |      content: attr(data-tab-text);
            |      background: var(--section-color);
            |      color: white;
            |      font-family: monospace;
            |      border-top-left-radius: 8px;
            |      border-top-right-radius: 8px;
            |      position: absolute;
            |      height: 20px;
            |      top: -20px;
            |      right: 10px;
            |      padding: 0 10px;
            |      vertical-align: middle;
            |    }
            |
            |    * {
            |      padding-top: 20px;
            |      padding-bottom: 20px;
            |    }
            |  }
            |}
            """.trimMargin()
    )
}

fun OrchidIntegrationTest.testPageStructure() {
    val attrs = object {
        val root = object {
            val classes = "class=\"section section-root\""
            val tabText = "data-tab-text=\"{{ page.sectionId(section) }}\""
            val style = "style=\"--section-color: green;\""
        }

        val element = object {
            val classes = "class=\"section section-element\""
            val tabText = "data-tab-text=\"{{ element.kind }}\""
            val style = "style=\"--section-color: black;\""
        }

        val signature = object {
            val classes = "class=\"section section-signature\""
            val tabText = "data-tab-text=\"signature\""
            val style = "style=\"--section-color: red;\""
        }

        val components = object {
            val classes = "class=\"section summary-components\""
            val tabText = "data-tab-text=\"summary components\""
            val style = "style=\"--section-color: purple;\""
        }

        val component = object {
            val classes = "class=\"section summary-component\""
            val tabText = "data-tab-text=\"{{ component.type }}\""
            val style = "style=\"--section-color: orange;\""
        }

        val comments = object {
            val classes = "class=\"section section-comments\""
            val tabText = "data-tab-text=\"comments\""
            val style = "style=\"--section-color: blue;\""
        }
    }

    resource(
        "templates/pages/sourceDocPage.peb",
        """
            |<div class="sourcedoc-page">
            |  <h1>{{ page.title }}</h1>
            |  <div class="orchid-sourcedoc">
            |    <h2>Page Content</h2>
            |    {{ __renderSection(page, page.rootSection, 1) }}
            |  </div>
            |  <div class="menus">
            |    <div class="theme-menu">
            |      <h2>Theme Menu</h2>
            |      {% include 'themeMenu.peb' %}
            |    </div>
            |    <div class="page-menu">
            |      <h2>Page Menu</h2>
            |      {% include 'pageMenu.peb' %}
            |    </div>
            |  </div>
            |</div>
            |
            |{% macro __renderSection(page, section, depth) %}
            |<div ${attrs.root.classes} ${attrs.root.tabText} ${attrs.root.style} id="{{ page.sectionId(section) }}">
            |{% for element in section.elements %}
            |  <div ${attrs.element.classes} ${attrs.element.tabText} ${attrs.element.style} id="{{ page.elementId(element) }}">
            |    <div ${attrs.signature.classes} ${attrs.signature.tabText} ${attrs.signature.style}>{{ page.renderSignature(element) | raw }}</div>
            |    
            |    {% if depth == 1 %}
            |    <div ${attrs.components.classes} ${attrs.components.tabText} ${attrs.components.style}>
            |      {% embed 'includes/componentHolder' with {"componentHolder": page.summaryComponents} %}
            |      {% block componentWrapper %}
            |        <div ${attrs.component.classes} ${attrs.component.tabText} ${attrs.component.style}>
            |          {{ component.renderContent(page.context, page) | raw }}
            |        </div>
            |        {% endblock %}
            |        {% block componentNoWrapper %}{{ component.renderContent(page.context, page) | raw }}{% endblock %}
            |      {% endembed %}
            |    </div>
            |    {% endif %}
            |    
            |    <div ${attrs.comments.classes} ${attrs.comments.tabText} ${attrs.comments.style}>{{ page.renderComment(element)|compileAs('md') }}</div>
            |    {% set childrenSections = page.getSectionsData(element) %}
            |    {% if childrenSections|length > 0 %}
            |      {% for childSection in childrenSections %}
            |        {{ __renderSection(page, childSection, depth+1) }}
            |      {% endfor %}
            |    {% endif %}
            |  </div>
            |{% endfor %}
            |</div>
            |{% endmacro %}
        """.trimMargin()
    )
}
