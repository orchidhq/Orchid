package com.eden.orchid.api.options

import com.eden.orchid.api.OrchidContext

class DefaultValidator(
    val context: OrchidContext
) : OptionsValidator {

    override fun validate(optionsHolder: Any) {
        // this validation mechanism was way too complex and difficult to work with, and the Hibernate validator brought
        // in too many weird dependencies, so I'm re-writing this mechanismg to not depend on the normal
        // OptionsExtractor validation mechanism
        val validationResult = if (optionsHolder is OptionsHolder) {
            try {
                val errors = optionsHolder.validate(context)

                if(errors.isEmpty()) {
                    ValidationResult.Passed(optionsHolder)
                } else {
                    ValidationResult.Failed(optionsHolder, errors)
                }
            } catch (e: Exception) {
                ValidationResult.Failed(optionsHolder, listOf(ValidationError.from(e)))
            }
        } else {
            ValidationResult.Passed(optionsHolder)
        }

        validationResult.requireSuccess()
    }
}
