package id.shoeclean.engine.exceptions

/**
 * The custom exception class to indicate when the [Order] not found.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-30
 */
class OrderNotFoundException(message: String) : RuntimeException(message)
