package com.eden.orchid.forms

import com.eden.orchid.impl.generators.HomepageGenerator
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
import kotlinx.html.textArea
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class FormsTests : OrchidIntegrationTest(withGenerator<HomepageGenerator>(), FormsModule()) {

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

}
