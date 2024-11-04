package id.shoeclean.engine.addresses

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The model class represent the request of [Address]. This request is nullable
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-25
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
data class AddressRequestNullable(
    val label: String? = null,
    val line: String? = null,
    val city: String? = null,
    val district: String? = null,
    val subdistrict: String? = null,
    val state: String? = null,
    @field:JsonProperty(value = "isSelected")
    val isSelected: Boolean? = null
)
