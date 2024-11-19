package id.shoeclean.engine.transaction

import id.shoeclean.engine.catalogs.ServiceType
import java.math.BigDecimal
import java.time.OffsetDateTime

/**
 * The data class represent the response of [Transaction].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
data class TransactionResponse(
    val uscId: String,
    val serviceType: ServiceType,
    val totalAmount: BigDecimal,
    val discount: BigDecimal,
    val subtotal: BigDecimal,
    val method: TransactionMethod,
    val status: TransactionStatus,
    val createdAt: OffsetDateTime
)
