package com.eden.orchid.api.options

sealed class ValidationResult {
    data class Passed(val source: Any) : ValidationResult()
    data class Failed(val source: Any, val errors: List<ValidationError>) : ValidationResult()

    fun requireSuccess() {
        when (this) {
            is Passed -> {
                // we passed, do nothing
            }
            is Failed -> {
                throw ValidationException(source, errors)
            }
        }
    }
}

class ValidationException(
    val source: Any,
    val errors: List<ValidationError>
) : Exception(
    """
    |${source::class.java.simpleName} did not pass validation. The following errors were reported:
    |${errors.joinToString(separator = "\n - ") { it.description }}
    """.trimMargin()
)

data class ValidationError
@JvmOverloads
constructor(
    val description: String,
    val cause: Throwable? = null
) {
    companion object {
        @JvmStatic
        fun from(throwable: Throwable): ValidationError {
            return ValidationError(
                description = "Unknown error thrown during validation",
                cause = throwable
            )
        }
    }
}

fun validateNotBlank(
    propertyName: String,
    value: String?,
    message: String = "$propertyName cannot be blank"
): ValidationError? {
    return if (value.isNullOrBlank()) ValidationError(message)
    else null
}

fun <T: Any> validateNotNull(
    propertyName: String,
    value: T?,
    message: String = "$propertyName cannot be null"
): ValidationError? {
    return if (value == null) ValidationError(message)
    else null
}

fun validateNotEmpty(
    propertyName: String,
    value: List<String>?,
    message: String = "$propertyName cannot be empty"
): ValidationError? {
    return if (value.isNullOrEmpty()) ValidationError(message)
    else null
}

fun runValidation(
    message: String,
    block: ()->Boolean,
): ValidationError? {
    val result = runCatching { block() }
    return if (result.isFailure) ValidationError(message)
    else if (result.getOrNull() != true) ValidationError(message)
    else null
}
