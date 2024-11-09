package id.shoeclean.engine.accounts

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * The REST controller for feature related to [Account].
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-09
 */
@RestController
@RequestMapping("/v1/accounts")
@Tag(name = "Accounts API")
class AccountController(private val accountService: AccountService) {

    /**
     * a GET mapping to handle retrieving the [Account] details.
     *
     * @param httpServletRequest the [HttpServletRequest] instance.
     * @return the [AccountDetailsResponse] instance.
     */
    @GetMapping("/details")
    @Operation(summary = "Get Account Details")
    fun getAccountDetails(httpServletRequest: HttpServletRequest): AccountDetailsResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- get the account details --
        return accountService.getDetails(accountId)
    }
}
