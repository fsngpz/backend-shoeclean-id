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
