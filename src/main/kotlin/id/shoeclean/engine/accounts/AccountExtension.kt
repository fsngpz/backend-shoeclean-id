package id.shoeclean.engine.accounts

import id.shoeclean.engine.addresses.getMainAddress
import id.shoeclean.engine.addresses.toResponse

/**
 * an extension function to convert the [Account] to [AccountDetailsResponse].
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-09
 */
fun Account.toDetailsResponse(): AccountDetailsResponse {
    // -- get the main address --
    val mainAddress = this.addresses?.getMainAddress()
    // -- map the instance to AccountDetailsResponse --
    return AccountDetailsResponse(
        name = this.name,
        email = this.user.email,
        mobile = this.user.mobile,
        profilePictureUrl = this.profilePictureUrl,
        isEmailVerified = this.user.emailVerifiedAt != null,
        mainAddress = mainAddress?.toResponse()
    )
}
