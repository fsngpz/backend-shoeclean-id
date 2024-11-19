package id.shoeclean.engine.vouchers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable

/**
 * The class that hold the Swagger documentation of [VoucherController].
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-19
 */
@Tag(name = "Vouchers API")
interface VoucherSwaggerController {
    @Operation(summary = "Get voucher by code")
    fun getVoucherByCode(@PathVariable code: String): VoucherResponse
}
