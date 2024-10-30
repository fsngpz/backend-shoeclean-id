package id.shoeclean.engine.exceptions

/**
 * The custom exception class to indicate error when the voucher is not suffice the order quantity (total pairs).
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-30
 */
class VoucherNotSufficeOrderQtyException(message: String) : RuntimeException(message)
