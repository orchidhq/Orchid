package com.eden.orchid.sourcedoc

import com.eden.orchid.testhelpers.OrchidIntegrationTest

fun OrchidIntegrationTest.testCss() {
    resource(
        "assets/css/orchidSourcedoc.scss", """
            |.orchid-sourcedoc {
            |  --section-color: black;
            |  * {
            |    padding: 0;
            |    margin: 0;
            |  }
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
    resource(
        "templates/pages/sourceDocPage.peb", """
            |<div class="orchid-sourcedoc">
            |{{ renderSection(page, page.rootSection) }}
            |</div>
            |
            |{% macro renderSection(page, section) %}
            |{% for element in section.elements %}
            |  <div class="section section-root" data-tab-text="{{ element.kind }}" style="--section-color: black;">
            |    <div class="section section-signature" data-tab-text="signature" style="--section-color: red;">{{ page.renderSignature(element)|raw }}</div>
            |    <div class="section section-comments" data-tab-text="comments" style="--section-color: blue;">{{ page.renderComment(element)|compileAs('md') }}</div>
            |    {% set childrenSections = page.getSectionsData(element) %}
            |    {% if childrenSections|length > 0 %}
            |      <div class="section section-children" data-tab-text="children - {{ (childrenSections | first).name }}" style="--section-color: green;">
            |      {% for childSection in childrenSections %}
            |        {{ renderSection(page, childSection) }}
            |      {% endfor %}
            |      </div>
            |    {% endif %}
            |  </div>
            |{% endfor %}
            |{% endmacro %}
        """.trimMargin()
    )
}

fun OrchidIntegrationTest.testMenuStructure() {
    resource(
        "templates/pages/sourceDocPage.peb", """
        |{{ page.title }}
        |{% include 'themeMenu.peb' %}
        """.trimMargin()
    )
}