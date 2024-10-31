package id.shoeclean.engine.vouchers

import id.shoeclean.engine.exceptions.VoucherNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * The service class to handle feature related to [Voucher].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
@Service
class VoucherService(private val voucherRepository: VoucherRepository) {

    /**
     * a function to get the [Voucher] using the unique identifier.
     *
     * @param id the voucher unique identifier.
     * @return the [Voucher] instance.
     */
    fun get(id: Long): Voucher {
        return voucherRepository.findByIdOrNull(id) ?: throw VoucherNotFoundException("no voucher found for id $id")
    }

    /**
     * a function to handle retrieving the [Voucher] using the code. this function also will check the quota
     * and expiry date of the [Voucher].
     *
     * @param code the code of [Voucher].
     * @return the [Voucher]
     */
    fun get(code: String): Voucher {
        val voucher = voucherRepository.findByCode(code) ?: throw VoucherNotFoundException("no voucher found for $code")
        // -- check the quota and expiry date of voucher --
        voucher.checkQuota()
        voucher.checkExpired()
        // -- return the voucher --
        return voucher
    }
}
