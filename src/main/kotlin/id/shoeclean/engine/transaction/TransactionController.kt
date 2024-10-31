package id.shoeclean.engine.transaction

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

/**
 * The REST controller of feature related to [Transaction].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
@RestController
@RequestMapping("/v1/transactions")
@Tag(name = "Transactions API")
class TransactionController(private val transactionService: TransactionService) {

    /**
     * a GET mapping to find all transaction of the logged in user.
     *
     * @param filter the parameter to filter the transaction by order id.
     * @param pageable the [Pageable].
     * @return the Page of [TransactionResponse].
     */
    @GetMapping
    @Operation(summary = "Find all transactions of a specific user")
    fun findAll(
        @RequestParam filter: String?,
        @RequestParam createdAtFrom: OffsetDateTime?,
        @RequestParam createdAtTo: OffsetDateTime?,
        @PageableDefault(size = 5, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        httpServletRequest: HttpServletRequest
    ): Page<TransactionResponse> {
        // -- get the account id from header --
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- find all transaction --
        return transactionService.findAll(accountId, filter, createdAtFrom, createdAtTo, pageable).map {
            it.toResponse()
        }
    }
}

