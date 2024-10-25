package id.shoeclean.engine.addresses

/**
 * The model class represent the request of [Address].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-25
 */
data class AddressRequest(
    val label: String? = null,
    val line: String? = null,
    val city: String? = null,
    val district: String? = null,
    val subdistrict: String? = null,
    val state: String? = null,
)
