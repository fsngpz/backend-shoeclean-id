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
    return AddressResponse(id, this.label, this.line, this.city, this.district, this.subdistrict, this.state)
}
