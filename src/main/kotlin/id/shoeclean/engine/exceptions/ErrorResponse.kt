package id.shoeclean.engine.exceptions

/**
 * The model class represent the error response.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
data class ErrorResponse(
    val type: String,
    val message: String
)
