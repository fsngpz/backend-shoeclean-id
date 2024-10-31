package id.shoeclean.engine.orders

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * The REST Controller of feature related to [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-30
 */
@RestController
@RequestMapping("/v1/orders")
@Tag(name = "Orders API")
class OrderController(private val orderService: OrderService) {

    /**
     * a GET mapping to handle retrieving the [OrderDetailResponse].
     *
     * @param uscId the urban sneaker care order unique identifier.
     * @param httpServletRequest the [HttpServletRequest].
     * @return the [OrderDetailResponse] instance.
     */
    @GetMapping("/{uscId}")
    @Operation(summary = "Get details order by id")
    fun get(
        @PathVariable uscId: String,
        httpServletRequest: HttpServletRequest
    ): OrderDetailResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- get the details of order --
        return orderService.getDetails(accountId, uscId)
    }

    /**
     * a GET mapping to handle retrieving the [OrderDetailResponse] using the voucher code.
     *
     * @param uscId the urban sneaker care order unique identifier.
     * @param voucherCode the voucher code.
     * @param httpServletRequest the [HttpServletRequest] instance.
     * @return the [OrderDetailResponse] instance.
     */
    @GetMapping("/{uscId}/{voucherCode}")
    @Operation(summary = "Get the details order by usc id with voucher code")
    fun applyVoucher(
        @PathVariable uscId: String,
        @PathVariable voucherCode: String,
        httpServletRequest: HttpServletRequest
    ): OrderDetailResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- get the details of order using the voucher code--
        return orderService.applyVoucher(accountId, uscId, voucherCode)
    }

    /**
     * a POST mapping to handle creating new [Order].
     *
     * @param request the [OrderRequest] instance.
     * @param httpServletRequest the [HttpServletRequest].
     * @return the [SubmitOrderResponse].
     */
    @PostMapping
    @Operation(summary = "Create a new order")
    fun create(
        @RequestBody request: OrderRequest,
        httpServletRequest: HttpServletRequest
    ): SubmitOrderResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- validate the request body --
        requireNotNull(request.addressId) {
            "field addressId cannot be null"
        }
        requireNotNull(request.sneakerIds) {
            "field sneakerIds cannot be null"
        }
        requireNotNull(request.serviceType) {
            "field serviceType cannot be null"
        }
        requireNotNull(request.totalPairs) {
            "field totalPairs cannot be null"
        }
        // -- submit new order --
        return orderService.create(
            accountId,
            request.addressId,
            request.sneakerIds,
            request.serviceType,
            request.totalPairs
        )
    }

    /**
     * a POST mapping to confirm the [Order].
     *
     * @param uscId the urban sole care unique identifier.
     * @param request the [OrderConfirmationRequest] instance.
     * @param httpServletRequest the [HttpServletRequest] instance.
     */
    @PostMapping("/confirm/{uscId}")
    @Operation(summary = "Confirm the created order")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun confirm(
        @PathVariable uscId: String,
        @RequestBody request: OrderConfirmationRequest,
        httpServletRequest: HttpServletRequest
    ) {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- validate the transaction method --
        requireNotNull(request.transactionMethod) {
            "field transactionMethod cannot be null"
        }
        // -- confirm the order --
        return orderService.confirm(accountId, uscId, request.transactionMethod, request.voucherCode)
    }
}
