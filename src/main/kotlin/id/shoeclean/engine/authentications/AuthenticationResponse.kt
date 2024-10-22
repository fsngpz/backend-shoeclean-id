package id.shoeclean.engine.authentications

/**
 * The data class represent the response of Authentication.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
data class AuthenticationResponse(val bearerToken: String, val roles: List<String>)
