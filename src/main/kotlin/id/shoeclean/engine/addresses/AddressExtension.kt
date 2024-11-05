package id.shoeclean.engine.addresses

/**
 * The extension function to map the [Address] to [AddressResponse].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-25
 */
fun Address.toResponse(): AddressResponse {
    val id = this.id
    // -- validate the id --
    requireNotNull(id) {
        "the address id is null"
    }
    return AddressResponse(
        id,
        this.label,
        this.line,
        this.city,
        this.district,
        this.subdistrict,
        this.state,
        this.isMainAddress
    )
}

/**
 * an extension function to convert the [Address] to [AddressRequestNullable] instance.
 *
 * @return the [AddressRequestNullable] instance.
 */
fun Address.toRequestNullable(): AddressRequestNullable {
    return AddressRequestNullable(
        label = this.label,
        line = this.line,
        city = this.city,
        district = this.district,
        subdistrict = this.subdistrict,
        state = this.state,
        isSelected = this.isMainAddress
    )
}

/**
 * the extension function to convert the [AddressRequestNullable] to  [AddressRequest].
 *
 * @return the [AddressRequest] instance.
 */
fun AddressRequestNullable.toRequest(): AddressRequest {
    // -- validate the request body --
    requireNotNull(this.label) {
        "field label cannot be null"
    }
    requireNotNull(this.line) {
        "field line cannot be null"
    }
    requireNotNull(this.city) {
        "field city cannot be null"
    }
    requireNotNull(this.district) {
        "field district cannot be null"
    }
    requireNotNull(this.subdistrict) {
        "field subdistrict cannot be null"
    }
    requireNotNull(this.state) {
        "field state cannot be null"
    }
    return AddressRequest(
        label = this.label,
        line = this.line,
        city = this.city,
        district = this.district,
        subdistrict = this.subdistrict,
        state = this.state,
        isMainAddress = this.isSelected ?: false
    )
}
