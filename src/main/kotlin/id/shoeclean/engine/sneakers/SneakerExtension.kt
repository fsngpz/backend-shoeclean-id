package id.shoeclean.engine.sneakers

/**
 * an extension function to convert the [Sneaker] to [SneakerResponse].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
fun Sneaker.toResponse(): SneakerResponse {
    val id = this.id
    requireNotNull(id) {
        "the id is null"
    }
    return SneakerResponse(id, this.brand, this.color, this.imageUrl)
}

/**
 * an extension function to convert the [SneakerRequestNullable] to [SneakerRequest].
 *
 * @return the [SneakerRequest] instance.
 */
fun SneakerRequestNullable.toRequest(): SneakerRequest {
    // -- validate the SneakerRequestNullable --
    requireNotNull(this.brand) {
        "field brand cannot be null"
    }
    requireNotNull(this.color) {
        "field color cannot be null"
    }
    return SneakerRequest(this.brand, this.color)
}


/**
 * an extension function to convert the [Sneaker] to [SneakerRequestNullable] instance.
 *
 * @return the [SneakerRequestNullable] instance.
 */
fun Sneaker.toRequestNullable(): SneakerRequestNullable {
    return SneakerRequestNullable(
        brand = this.brand,
        color = this.color
    )
}
