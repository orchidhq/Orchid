package com.eden.orchid.sourcedoc

import com.eden.orchid.testhelpers.OrchidIntegrationTest

fun OrchidIntegrationTest.testCss() {
    resource(
        "assets/css/orchidSourcedoc.scss", """
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
    resource(
        "templates/pages/sourceDocPage.peb", """
            |<div class="sourcedoc-page">
            |  <h1>{{ page.title }}</h1>
            |  <div class="orchid-sourcedoc">
            |    <h2>Page Content</h2>
            |    {{ renderSection(page, page.rootSection) }}
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
            |{% macro renderSection(page, section) %}
            |<div class="section section-root" data-tab-text="{{ page.sectionId(section) }}" style="--section-color: green;" id="{{ page.sectionId(section) }}">
            |{% for element in section.elements %}
            |  <div class="section section-element" data-tab-text="{{ element.kind }}" style="--section-color: black;" id="{{ page.elementId(element) }}">
            |    <div class="section section-signature" data-tab-text="signature" style="--section-color: red;">{{ page.renderSignature(element)|raw }}</div>
            |    <div class="section section-comments" data-tab-text="comments" style="--section-color: blue;">{{ page.renderComment(element)|compileAs('md') }}</div>
            |    {% set childrenSections = page.getSectionsData(element) %}
            |    {% if childrenSections|length > 0 %}
            |      {% for childSection in childrenSections %}
            |        {{ renderSection(page, childSection) }}
            |      {% endfor %}
            |    {% endif %}
            |  </div>
            |{% endfor %}
            |</div>
            |{% endmacro %}
        """.trimMargin()
    )
}
