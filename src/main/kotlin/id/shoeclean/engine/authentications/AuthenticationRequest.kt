package id.shoeclean.engine.authentications

/**
 * The model class for request authentication.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
data class AuthenticationRequest(
    val email: String? = null,
    val password: String? = null
)
