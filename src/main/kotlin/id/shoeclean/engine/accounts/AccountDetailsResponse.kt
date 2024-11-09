package id.shoeclean.engine.accounts

import id.shoeclean.engine.addresses.AddressResponse

/**
 * The data class represent the response of Account Details.
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-09
 */
data class AccountDetailsResponse(
    val name: String?,
    val email: String,
    val mobile: String?,
    val profilePictureUrl: String?,
    val isEmailVerified: Boolean,
    val mainAddress: AddressResponse?
) {
}
