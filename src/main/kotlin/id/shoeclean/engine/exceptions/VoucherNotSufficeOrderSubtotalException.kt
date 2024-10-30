package id.shoeclean.engine.exceptions

/**
 * The custom exception class to indicate error when the voucher not suffice the subtotal of order.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-30
 */
class VoucherNotSufficeOrderSubtotalException(message: String) : RuntimeException(message)
