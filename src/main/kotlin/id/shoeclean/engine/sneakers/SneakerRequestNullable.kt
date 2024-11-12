package id.shoeclean.engine.sneakers

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * The model class represent the request of [Sneaker]. This request is nullable.
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-12
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
data class SneakerRequestNullable(
    val brand: String? = null,
    val color: String? = null
)
