package id.shoeclean.engine.addresses

/**
 * The model class represent the response of [Address].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-25
 */
data class AddressResponse(
    val id: Long,
    val label: String,
    val line: String,
    val city: String,
    val district: String,
    val subdistrict: String,
    val state: String,
    val isSelected: Boolean
)
