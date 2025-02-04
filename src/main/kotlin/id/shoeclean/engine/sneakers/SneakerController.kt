package id.shoeclean.engine.sneakers

import com.fasterxml.jackson.databind.JsonNode
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
 * The REST Controller of feature related to [Sneaker].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@RestController
@RequestMapping("/v1/sneakers")
class SneakerController(private val sneakerService: SneakerService) : SneakerSwaggerController {


    /**
     * a GET mapping to handle retrieving the sneaker by given unique identifier.
     *
     * @param id the sneaker unique identifier.
     * @param httpServletRequest the [HttpServletRequest].
     * @return the [SneakerResponse] instance.
     */
    @GetMapping("/{id}")
    override fun getAddress(@PathVariable id: Long, httpServletRequest: HttpServletRequest): SneakerResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- get the address by id --
        return sneakerService.get(accountId, id).toResponse()
    }

    /**
     * a GET mapping to handle request fetching the [Sneaker] using filter.
     *
     * @param filter the parameter to filter the sneaker by brand or colorl
     * @param pageable the [Pageable].
     * @param httpServletRequest the [HttpServletRequest].
     * @return the [Page] of [SneakerResponse].
     */
    @GetMapping
    override fun findAll(
        @RequestParam filter: String?,
        @PageableDefault(sort = ["brand"], direction = Sort.Direction.ASC) pageable: Pageable,
        httpServletRequest: HttpServletRequest
    ): Page<SneakerResponse> {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- find all sneaker --
        return sneakerService.findAll(accountId, filter, pageable)
    }

    /**
     * a POST mapping to handle request create new [Sneaker].
     *
     * @param request the [SneakerRequest] instance.
     * @param httpServletRequest the [HttpServletRequest].
     * @return the [SneakerResponse].
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @RequestBody request: SneakerRequestNullable,
        httpServletRequest: HttpServletRequest
    ): SneakerResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- validate the request body --
        requireNotNull(request.brand) {
            "field brand cannot be null"
        }
        requireNotNull(request.color) {
            "field color cannot be null"
        }
        // -- create the new sneaker --
        return sneakerService.create(accountId, request.brand, request.color)
    }

    /**
     * a PATCH mapping to handle request update partial the [Sneaker].
     *
     * @param id the sneaker unique identifier.
     * @param request the [JsonNode] as a request body.
     * @param httpServletRequest the [HttpServletRequest].
     * @return
     */
    @PatchMapping("/{id}")
    override fun patch(
        @PathVariable id: Long,
        @RequestBody request: JsonNode,
        httpServletRequest: HttpServletRequest
    ): SneakerResponse {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- do patch the address --
        return sneakerService.patch(accountId, id, request).toResponse()
    }

    /**
     * a DELETE mapping to handle request removing the [Sneaker] by id.
     *
     * @param id the sneaker unique identifier.
     * @param httpServletRequest the [HttpServletRequest].
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable id: Long, httpServletRequest: HttpServletRequest) {
        val accountId = httpServletRequest.getHeader("ID").toLong()
        // -- delete the sneaker --
        sneakerService.delete(accountId, id)
    }
}
