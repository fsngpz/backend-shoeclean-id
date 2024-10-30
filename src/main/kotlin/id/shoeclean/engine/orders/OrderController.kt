package id.shoeclean.engine.orders

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
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
     * a GET mapping to handle retrieving the [OrderDetailResponse].
     *
     * @param uscId the urban sneaker care order unique identifier.
     * @param httpServletRequest the [HttpServletRequest].
     * @return the [OrderDetailResponse] instance.
     */
    @GetMapping("/{uscId}")
    @Operation(summary = "Get details order by id")
    fun get(@PathVariable uscId: String, httpServletRequest: HttpServletRequest): OrderDetailResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- get the details of order --
        return orderService.getDetails(accountId, uscId)
    }
}
