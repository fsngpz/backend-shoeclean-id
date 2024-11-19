package id.shoeclean.engine.vouchers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * The REST Controller of feature related to [Voucher].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
@RestController
@RequestMapping("/v1/vouchers")
class VoucherController(private val voucherService: VoucherService) : VoucherSwaggerController {

    /**
     * a GET mapping to get the voucher using the code.
     *
     * @param code the code of [Voucher].
     * @return the [VoucherResponse].
     */
    @GetMapping("/{code}")
    override fun getVoucherByCode(@PathVariable code: String): VoucherResponse {
        // -- get the voucher by code --
        return voucherService.get(code).toResponse()
    }
}
