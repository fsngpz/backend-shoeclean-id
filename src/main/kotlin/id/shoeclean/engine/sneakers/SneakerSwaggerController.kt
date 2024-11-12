package id.shoeclean.engine.sneakers

import com.fasterxml.jackson.databind.JsonNode
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated

/**
 * The class that hold the Swagger documentation of [SneakerController].
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-12
 */
@Tag(name = "Sneakers API")
@Validated
interface SneakerSwaggerController {

    @Operation(summary = "Get a specific address by id")
    fun getAddress(id: Long, httpServletRequest: HttpServletRequest): SneakerResponse

    @Operation(summary = "Find All Sneakers using Filter")
    fun findAll(filter: String?, pageable: Pageable, httpServletRequest: HttpServletRequest): Page<SneakerResponse>

    @Operation(summary = "Create a Sneaker")
    fun create(request: SneakerRequestNullable, httpServletRequest: HttpServletRequest): SneakerResponse


    @Operation(summary = "Patch / Partial Update a specific sneaker")
    fun patch(
        id: Long,
        @RequestBody(
            content = [
                Content(schema = Schema(implementation = SneakerRequestNullable::class))
            ]
        )
        request: JsonNode,
        httpServletRequest: HttpServletRequest
    ): SneakerResponse

    @Operation(summary = "Delete a Sneaker with the given id")
    fun delete(id: Long, httpServletRequest: HttpServletRequest)
}
