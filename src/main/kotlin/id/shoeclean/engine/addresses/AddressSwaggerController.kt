package id.shoeclean.engine.addresses

import com.fasterxml.jackson.databind.JsonNode
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * The class that hold the Swagger documentation of [AddressController].
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-12
 */
@Tag(name = "Addresses API")
interface AddressSwaggerController {
    @Operation(summary = "Get a specific address by id")
    fun getAddress(id: Long, httpServletRequest: HttpServletRequest): AddressResponse

    @Operation(summary = "Find All Address using Filter")
    fun findAll(filter: String?, pageable: Pageable, httpServletRequest: HttpServletRequest): Page<AddressResponse>

    @Operation(summary = "Set the main address")
    fun setMainAddress(addressId: Long, httpServletRequest: HttpServletRequest)

    @Operation(summary = "Create new address")
    fun create(request: AddressRequestNullable, httpServletRequest: HttpServletRequest): AddressResponse

    @Operation(summary = "Patch / Partial Update a specific address")
    fun patch(
        id: Long,
        @RequestBody(
            content = [
                Content(schema = Schema(implementation = AddressRequestNullable::class))
            ]
        )
        request: JsonNode,
        httpServletRequest: HttpServletRequest
    ): AddressResponse

    @Operation(summary = "Delete an Address with the given id")
    fun delete(id: Long, httpServletRequest: HttpServletRequest)
}
