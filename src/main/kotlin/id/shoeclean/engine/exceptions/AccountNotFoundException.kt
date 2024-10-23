package id.shoeclean.engine.exceptions

/**
 * The custom class to indicates when the account not found.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
class AccountNotFoundException(message: String) : RuntimeException(message) {
}
