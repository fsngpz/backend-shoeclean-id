package id.shoeclean.engine.exceptions

/**
 * The custom exception class to indicate error when the Voucher Quota is already exceeded or empty.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
class VoucherQuotaExceededException(message: String) : RuntimeException(message)
