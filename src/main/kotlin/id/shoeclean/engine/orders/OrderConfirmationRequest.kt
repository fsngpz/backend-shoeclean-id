package id.shoeclean.engine.orders

import id.shoeclean.engine.transaction.TransactionMethod

/**
 * The request class for order confirmation.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
data class OrderConfirmationRequest(
    val voucherCode: String?,
    val transactionMethod: TransactionMethod?
)
