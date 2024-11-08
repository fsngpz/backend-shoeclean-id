package id.shoeclean.engine.addresses

import com.fasterxml.jackson.databind.JsonNode
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
import org.springframework.web.bind.annotation.PatchMapping
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
        @PageableDefault(sort = ["label"], direction = Sort.Direction.ASC) pageable: Pageable,
        httpServletRequest: HttpServletRequest
    ): Page<AddressResponse> {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- find all address --
        return addressService.findAll(accountId, filter, pageable)
    }

    /**
     * a POST mapping to handle set the main address of user.
     *
     * @param addressId the address unique identifier.
     * @param httpServletRequest the [HttpServletRequest].
     */
    @PostMapping("/main/{addressId}")
    @Operation(summary = "Set the main address")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun setMainAddress(@PathVariable addressId: Long, httpServletRequest: HttpServletRequest) {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- set the main address --
        return addressService.setMainAddress(accountId, addressId)
    }

    /**
     * a POST mapping to handle request create new [Address].
     *
     * @param request the [AddressRequestNullable] instance.
     * @param httpServletRequest the [HttpServletRequest].
     * @return the [AddressResponse].
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new address")
    fun create(@RequestBody request: AddressRequestNullable, httpServletRequest: HttpServletRequest): AddressResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- convert the Nullable request to Non Nullable --
        val nonNullRequest = request.toRequest()
        // -- create the new address --
        return addressService.create(
            accountId,
            nonNullRequest.label,
            nonNullRequest.line,
            nonNullRequest.city,
            nonNullRequest.district,
            nonNullRequest.subdistrict,
            nonNullRequest.state
        )
    }

    /**
     * a PATCH mapping to handle request update partial the [Address].
     *
     * @param id the address unique identifier.
     * @param request the [JsonNode] as a request body.
     * @param httpServletRequest the [HttpServletRequest].
     * @return
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Patch / Partial Update a specific address")
    fun patch(
        @PathVariable id: Long,
        @RequestBody request: JsonNode,
        httpServletRequest: HttpServletRequest
    ): AddressResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- do patching of the Address --
        return addressService.patch(accountId, id, request).toResponse()
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
