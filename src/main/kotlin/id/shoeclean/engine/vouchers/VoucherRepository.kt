package id.shoeclean.engine.vouchers

import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository for [Voucher].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
interface VoucherRepository : JpaRepository<Voucher, Long> {

    /**
     * a method to find the [Voucher] by code.
     *
     * @param code the code of [Voucher].
     * @return the [Voucher] or null.
     */
    fun findByCode(code: String): Voucher?
}
