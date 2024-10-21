package id.shoeclean.engine.authentications.registration

/**
 * The model class represent the request for registration.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
data class RegistrationRequest(
    val email: String?,
    val password: String?,
    val attributes: AttributeRequest?
)
