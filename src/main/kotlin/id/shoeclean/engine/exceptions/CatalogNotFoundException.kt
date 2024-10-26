package id.shoeclean.engine.exceptions

/**
 * The exception class to indicate error when the [Catalog] not found.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
class CatalogNotFoundException(message: String) : RuntimeException(message)
