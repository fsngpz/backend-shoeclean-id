package id.shoeclean.engine.sneakers

/**
 * an extension function to convert the [Sneaker] to [SneakerResponse].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
fun Sneaker.toResponse(): SneakerResponse {
    return SneakerResponse(this.brand, this.color, this.imageUrl)
}
