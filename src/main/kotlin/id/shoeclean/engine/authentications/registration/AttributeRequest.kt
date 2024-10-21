package id.shoeclean.engine.authentications.registration

/**
 * The data class represent the request of attributes for registration.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
data class AttributeRequest(
    val name: String? = null,
    val mobile: String? = null
) {
}
