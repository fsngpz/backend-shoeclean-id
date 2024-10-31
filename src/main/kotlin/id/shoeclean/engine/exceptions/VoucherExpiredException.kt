package id.shoeclean.engine.exceptions

/**
 * The exception class to indicate error when the voucher already expired.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
class VoucherExpiredException(message: String) : RuntimeException(message)
