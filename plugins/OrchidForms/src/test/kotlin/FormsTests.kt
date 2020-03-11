package com.eden.orchid.forms

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.plugindocs.PluginDocsModule
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.br
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h3
import kotlinx.html.header
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.option
import kotlinx.html.select
import kotlinx.html.textArea
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class FormsTests : OrchidIntegrationTest(FormsModule(), PluginDocsModule(), withGenerator<HomepageGenerator>()) {

// Test methods of registering forms
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test rendering a form that was pre-indexed (built-in contact form)")
    fun test01() {
        resource(
            "homepage.txt",
            """
            |---
            |components:
            |  - type: form
            |    form: contact
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-form component-order-0") {
                        header {
                            h3 {
                                +"Contact Us"
                            }
                        }

                        form(action = "thank-you", classes = "orchid-form", method = FormMethod.post) {
                            name = "contact"
                            attributes["data-netlify"] = "true"
                            div("row") {
                                div("col col-lg-6 col-sm-12 ") {
                                    label {
                                        htmlFor = "contact--name"
                                        +"Name"
                                    }
                                    input(type = InputType.text, name = "name") {
                                        id = "contact--name"
                                        placeholder = "Name"
                                        required = true
                                    }
                                }
                                div("col col-lg-6 col-sm-12 ") {
                                    label {
                                        htmlFor = "contact--email"
                                        +"Email"
                                    }
                                    input(type = InputType.email, name = "email") {
                                        id = "contact--email"
                                        placeholder = "Email"
                                        required = true
                                    }
                                }
                                div("col col-lg-12 col-sm-12 ") {
                                    label {
                                        htmlFor = "contact--subject"
                                        +"Subject"
                                    }
                                    input(type = InputType.text, name = "subject") {
                                        id = "contact--subject"
                                        placeholder = "Subject"
                                        required = true
                                    }
                                }
                                div("col col-lg-12 col-sm-12 ") {
                                    label {
                                        htmlFor = "contact--comments"
                                        +"Message"
                                    }
                                    textArea {
                                        name = "comments"
                                        id = "contact--comments"
                                        placeholder = "Message"
                                        required = true
                                    }
                                }
                                div("col col-lg-6 col-sm-12 ") {
                                    input(type = InputType.checkBox, name = "signUpForNewsletter") {
                                        id = "contact--signUpForNewsletter"
                                        checked = true
                                    }
                                    label {
                                        htmlFor = "contact--signUpForNewsletter"
                                        +"Sign up for our newsletter"
                                    }
                                }
                            }
                            br()
                            input(type = InputType.submit) {
                                value = "Submit"
                            }
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering a form that was pre-indexed (custom-in form)")
    fun test02() {
        resource(
            "homepage.txt",
            """
            |---
            |components:
            |  - type: form
            |    form: customForm
            |---
            """.trimMargin()
        )
        resource(
            "forms/customForm.yaml",
            """
            |title: 'Contact Us'
            |action: 'thank-you'
            |fields:
            |  name:
            |    label: 'Name'
            |    type: 'text'
            |    required: true
            |    span: 'auto'
            |    order: 1
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-form component-order-0") {
                        header {
                            h3 {
                                +"Contact Us"
                            }
                        }

                        form(action = "thank-you", classes = "orchid-form", method = FormMethod.post) {
                            name = "customForm"
                            div("row") {
                                div("col col-lg-6 col-sm-12 ") {
                                    label {
                                        htmlFor = "customForm--name"
                                        +"Name"
                                    }
                                    input(type = InputType.text, name = "name") {
                                        id = "customForm--name"
                                        placeholder = "Name"
                                        required = true
                                    }
                                }
                            }
                            br()
                            input(type = InputType.submit) {
                                value = "Submit"
                            }
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering a form that was provided inline")
    fun test03() {
        resource(
            "homepage.txt",
            """
            |---
            |components:
            |  - type: form
            |    form:
            |      title: 'Contact Us'
            |      action: 'thank-you'
            |      fields:
            |        name:
            |          label: 'Name'
            |          type: 'text'
            |          required: true
            |          span: 'auto'
            |          order: 1
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-form component-order-0") {
                        header {
                            h3 {
                                +"Contact Us"
                            }
                        }

                        form(action = "thank-you", classes = "orchid-form", method = FormMethod.post) {
                            name = "Contact Us"
                            div("row") {
                                div("col col-lg-6 col-sm-12 ") {
                                    label {
                                        htmlFor = "Contact Us--name"
                                        +"Name"
                                    }
                                    input(type = InputType.text, name = "name") {
                                        id = "Contact Us--name"
                                        placeholder = "Name"
                                        required = true
                                    }
                                }
                            }
                            br()
                            input(type = InputType.submit) {
                                value = "Submit"
                            }
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test method and redirectionPage hidden fields are added if needed")
    fun test04() {
        resource(
            "homepage.txt",
            """
            |---
            |components:
            |  - type: form
            |    form:
            |      title: 'Contact Us'
            |      action: 'thank-you'
            |      method: put
            |      redirectionPage: 'Not Found'
            |      fields:
            |        name:
            |          label: 'Name'
            |          type: 'text'
            |          required: true
            |          span: 'auto'
            |          order: 1
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-form component-order-0") {
                        header {
                            h3 {
                                +"Contact Us"
                            }
                        }

                        form(action = "thank-you", classes = "orchid-form", method = FormMethod.put) {
                            name = "Contact Us"
                            div("row") {
                                div("col col-lg-6 col-sm-12 ") {
                                    label {
                                        htmlFor = "Contact Us--name"
                                        +"Name"
                                    }
                                    input(type = InputType.text, name = "name") {
                                        id = "Contact Us--name"
                                        placeholder = "Name"
                                        required = true
                                    }
                                }
                                div("col col-lg-6 col-sm-12 ") {
                                    input(type = InputType.hidden, name = "__onSubmit") {
                                        id = "Contact Us--__onSubmit"
                                        value = "http://orchid.test/404.html"
                                    }
                                }
                                div("col col-lg-6 col-sm-12 ") {
                                    input(type = InputType.hidden, name = "__method") {
                                        id = "Contact Us--__method"
                                        value = "put"
                                    }
                                }
                            }
                            br()
                            input(type = InputType.submit) {
                                value = "Submit"
                            }
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering a form that was pre-indexed (built-in contact form) with the Form tag")
    fun test05() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{% form 'contact' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    form(action = "thank-you", classes = "orchid-form", method = FormMethod.post) {
                        name = "contact"
                        attributes["data-netlify"] = "true"
                        div("row") {
                            div("col col-lg-6 col-sm-12 ") {
                                label {
                                    htmlFor = "contact--name"
                                    +"Name"
                                }
                                input(type = InputType.text, name = "name") {
                                    id = "contact--name"
                                    placeholder = "Name"
                                    required = true
                                }
                            }
                            div("col col-lg-6 col-sm-12 ") {
                                label {
                                    htmlFor = "contact--email"
                                    +"Email"
                                }
                                input(type = InputType.email, name = "email") {
                                    id = "contact--email"
                                    placeholder = "Email"
                                    required = true
                                }
                            }
                            div("col col-lg-12 col-sm-12 ") {
                                label {
                                    htmlFor = "contact--subject"
                                    +"Subject"
                                }
                                input(type = InputType.text, name = "subject") {
                                    id = "contact--subject"
                                    placeholder = "Subject"
                                    required = true
                                }
                            }
                            div("col col-lg-12 col-sm-12 ") {
                                label {
                                    htmlFor = "contact--comments"
                                    +"Message"
                                }
                                textArea {
                                    name = "comments"
                                    id = "contact--comments"
                                    placeholder = "Message"
                                    required = true
                                }
                            }
                            div("col col-lg-6 col-sm-12 ") {
                                input(type = InputType.checkBox, name = "signUpForNewsletter") {
                                    id = "contact--signUpForNewsletter"
                                    checked = true
                                }
                                label {
                                    htmlFor = "contact--signUpForNewsletter"
                                    +"Sign up for our newsletter"
                                }
                            }
                        }
                        br()
                        input(type = InputType.submit) {
                            value = "Submit"
                        }
                    }
                }
            }
    }

// Test default rendering of each form field type
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test rendering a checkbox fields")
    fun test11() {
        resource(
            "homepage.txt",
            """
            |---
            |components:
            |  - type: form
            |    form:
            |      title: 'Contact Us'
            |      fields:
            |        name:
            |          label: Name
            |          type: checkbox
            |          default: true
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches(selector = "form > .row > .col") {
                    input(type = InputType.checkBox, name = "name") {
                        id = "Contact Us--name"
                        checked = true
                    }
                    label {
                        htmlFor = "Contact Us--name"
                        +"Name"
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering a dropdown fields")
    fun test12() {
        resource(
            "homepage.txt",
            """
            |---
            |components:
            |  - type: form
            |    form:
            |      title: 'Contact Us'
            |      fields:
            |        name:
            |          label: 'Name'
            |          type: 'dropdown'
            |          required: true
            |          multiple: true
            |          options: 
            |            one: 'Value One'
            |            two: 'Value Two'
            |            three: 'Value Three'
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches(selector = "form > .row > .col") {
                    label {
                        htmlFor = "Contact Us--name"
                        +"Name"
                    }
                    select {
                        name = "name"
                        id = "Contact Us--name"
                        required = true
                        multiple = true
                        option {
                            value = "two"
                            +"Value Two"
                        }
                        option {
                            value = "three"
                            +"Value Three"
                        }
                        option {
                            value = "one"
                            +"Value One"
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering a hidden fields")
    fun test13() {
        resource(
            "homepage.txt",
            """
            |---
            |components:
            |  - type: form
            |    form:
            |      title: 'Contact Us'
            |      fields:
            |        name:
            |          label: 'Name'
            |          type: 'hidden'
            |          value: 'something super secret'
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches(selector = "form > .row > .col") {
                    input(type = InputType.hidden, name = "name") {
                        id = "Contact Us--name"
                        value = "something super secret"
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering a radio fields")
    fun test14() {
        resource(
            "homepage.txt",
            """
            |---
            |components:
            |  - type: form
            |    form:
            |      title: 'Contact Us'
            |      fields:
            |        name:
            |          label: 'Name'
            |          type: 'radio'
            |          required: true
            |          options: 
            |            one: 'Value One'
            |            two: 'Value Two'
            |            three: 'Value Three'
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches(selector = "form > .row > .col") {
                    label {
                        htmlFor = "Contact Us--name"
                        +"Name"
                    }
                    div("radio") {
                        label {
                            input(type = InputType.radio, name = "name") {
                                id = "Contact Us--name--two"
                                value = "two"
                                required = true
                            }
                            +"Value Two"
                        }
                    }
                    div("radio") {
                        label {
                            input(type = InputType.radio, name = "name") {
                                id = "Contact Us--name--three"
                                value = "three"
                                required = true
                            }
                            +"Value Three"
                        }
                    }
                    div("radio") {
                        label {
                            input(type = InputType.radio, name = "name") {
                                id = "Contact Us--name--one"
                                value = "one"
                                required = true
                            }
                            +"Value One"
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering a textarea fields")
    fun test15() {
        resource(
            "homepage.txt",
            """
            |---
            |components:
            |  - type: form
            |    form:
            |      title: 'Contact Us'
            |      fields:
            |        name:
            |          label: 'Name'
            |          type: 'textarea'
            |          required: true
            |          rows: '4'
            |          cols: 80
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches(selector = "form > .row > .col") {
                    label {
                        htmlFor = "Contact Us--name"
                        +"Name"
                    }
                    textArea(rows = "4", cols = "80") {
                        name = "name"
                        id = "Contact Us--name"
                        placeholder = "Name"
                        required = true
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering a textual fields")
    fun test16() {
        resource(
            "homepage.txt",
            """
            |---
            |components:
            |  - type: form
            |    form:
            |      title: 'Contact Us'
            |      fields:
            |        name:
            |          label: 'Name'
            |          type: 'text'
            |          required: true
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches(selector = "form > .row > .col") {
                    label {
                        htmlFor = "Contact Us--name"
                        +"Name"
                    }
                    input(type = InputType.text, name = "name") {
                        id = "Contact Us--name"
                        placeholder = "Name"
                        required = true
                    }
                }
            }
    }

}
