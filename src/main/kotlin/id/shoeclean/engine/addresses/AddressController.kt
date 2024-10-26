package id.shoeclean.engine.addresses

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * The REST controller for feature related to [Address].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@RestController
@RequestMapping("/v1/addresses")
@Tag(name = "Addresses API")
class AddressController(private val addressService: AddressService) {

    /**
     * a GET mapping to handle retrieving the address by given unique identifier.
     *
     * @param id the address unique identifier.
     * @param httpServletRequest the [HttpServletRequest].
     * @return the [AddressResponse] instance.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a specific address by id")
    fun getAddress(@PathVariable id: Long, httpServletRequest: HttpServletRequest): AddressResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- get the address by id --
        return addressService.get(accountId, id).toResponse()
    }

    /**
     * a GET mapping to handle request fetching the [Address] using filter.
     *
     * @param filter the parameter to filter the sneaker by brand or colorl
     * @param pageable the [Pageable].
     * @param httpServletRequest the [HttpServletRequest].
     * @return the [Page] of [AddressResponse].
     */
    @GetMapping
    @Operation(summary = "Find All Address using Filter")
    fun findAll(
        @RequestParam filter: String?,
        @PageableDefault(sort = ["brand"], direction = Sort.Direction.ASC) pageable: Pageable,
        httpServletRequest: HttpServletRequest
    ): Page<AddressResponse> {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- find all address --
        return addressService.findAll(accountId, filter, pageable)
    }

    /**
     * a POST mapping to handle request create new [Address].
     *
     * @param request the [AddressRequest] instance.
     * @param httpServletRequest the [HttpServletRequest].
     * @return the [AddressResponse].
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a Sneaker")
    fun create(@RequestBody request: AddressRequest, httpServletRequest: HttpServletRequest): AddressResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- validate the request body --
        requireNotNull(request.label) {
            "field label cannot be null"
        }
        requireNotNull(request.line) {
            "field line cannot be null"
        }
        requireNotNull(request.city) {
            "field city cannot be null"
        }
        requireNotNull(request.district) {
            "field district cannot be null"
        }
        requireNotNull(request.subdistrict) {
            "field subdistrict cannot be null"
        }
        requireNotNull(request.state) {
            "field state cannot be null"
        }
        // -- create the new address --
        return addressService.create(
            accountId,
            request.label,
            request.line,
            request.city,
            request.district,
            request.subdistrict,
            request.subdistrict
        )
    }

    /**
     * a DELETE mapping to handle request removing the [Address] by id.
     *
     * @param id the sneaker unique identifier.
     * @param httpServletRequest the [HttpServletRequest].
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Address with the given id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long, httpServletRequest: HttpServletRequest) {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- delete the address --
        addressService.delete(accountId, id)
    }
}
