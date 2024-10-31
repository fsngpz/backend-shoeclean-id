package id.shoeclean.engine.vouchers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Vouchers API")
class VoucherController(private val voucherService: VoucherService) {

    /**
     * a GET mapping to get the voucher using the code.
     *
     * @param code the code of [Voucher].
     * @return the [VoucherResponse].
     */
    @GetMapping("/{code}")
    @Operation(summary = "Get voucher by code")
    fun getVoucherByCode(@PathVariable code: String): VoucherResponse {
        // -- get the voucher by code --
        return voucherService.get(code).toResponse()
    }
}
