package id.shoeclean.engine.vouchers

import id.shoeclean.engine.exceptions.VoucherExpiredException
import id.shoeclean.engine.exceptions.VoucherQuotaExceededException
import java.time.OffsetDateTime

/**
 * an extension function to check the quota for given [Voucher].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
fun Voucher.checkQuota() {
    val quota = this.quantity

    require(quota > 0) {
        throw VoucherQuotaExceededException("quota for this voucher is $quota")
    }
}

/**
 * an extension function to check the expired time of [Voucher].
 *
 */
fun Voucher.checkExpired() {
    val expiredAt = this.expiredAt

    require(expiredAt.isAfter(OffsetDateTime.now())) {
        throw VoucherExpiredException("voucher is already expired at $expiredAt")
    }
}

/**
 * an extension function to convert the [Voucher] to [VoucherResponse].
 *
 * @return the [VoucherResponse].
 */
fun Voucher.toResponse(): VoucherResponse {
    val id = this.id
    // -- validate the id --
    requireNotNull(id) {
        "the voucher id is null"
    }
    return VoucherResponse(id, code, type, amountType, amount, expiredAt, quantity)
}
