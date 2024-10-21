package id.shoeclean.engine.exceptions

/**
 * The custom class to indicates when the email not found.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
class UserNotFoundException(message: String) : RuntimeException(message) {
}
