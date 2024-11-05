package id.shoeclean.engine.addresses

/**
 * The model class represent the request of [Address].
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-04
 */
data class AddressRequest(
    val label: String,
    val line: String,
    val city: String,
    val district: String,
    val subdistrict: String,
    val state: String,
    val isMainAddress: Boolean
)
