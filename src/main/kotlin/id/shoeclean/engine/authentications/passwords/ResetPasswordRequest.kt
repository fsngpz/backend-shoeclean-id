package id.shoeclean.engine.authentications.passwords

/**
 * The data class represent the request to reset the password.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
data class ResetPasswordRequest(
    val tokenUid: String? = null,
    val password: String? = null
)
