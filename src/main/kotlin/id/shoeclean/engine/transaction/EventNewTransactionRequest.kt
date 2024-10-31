package id.shoeclean.engine.transaction

import id.shoeclean.engine.orders.Order
import java.math.BigDecimal

/**
 * The data class for request new [Transaction].
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
data class EventNewTransactionRequest(
    val order: Order,
    val totalAmount: BigDecimal,
    val deduction: BigDecimal,
    val finalAmount: BigDecimal,
    val method: TransactionMethod
)
