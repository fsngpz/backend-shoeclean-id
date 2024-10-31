package id.shoeclean.engine.vouchers

import java.math.BigDecimal
import java.time.OffsetDateTime

/**
 * The data class represent the response of [Voucher].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
data class VoucherResponse(
    val id: Long,
    val code: String,
    val type: VoucherType,
    val amountType: AmountType,
    val amount: BigDecimal,
    val expiredAt: OffsetDateTime,
    val quantity: Int
)
