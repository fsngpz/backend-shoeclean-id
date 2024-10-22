package id.shoeclean.engine.authentications

import id.shoeclean.engine.authentications.roles.RoleResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * The REST Controller to handle feature related to Authentication.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-22
 */
@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Authentication API")
class AuthenticationController(private val service: AuthenticationService) {

    /**
     * a POST request to handle login.
     *
     * @param request the [AuthenticationRequest] payload.
     * @return the [AuthenticationResponse] instance.
     */
    @PostMapping("/login")
    @Operation(summary = "Login to Account")
    fun login(@RequestBody request: AuthenticationRequest): AuthenticationResponse {
        // -- validate field email --
        requireNotNull(request.email) {
            "field 'email' cannot be null"
        }
        // -- validate field password --
        requireNotNull(request.password) {
            "field 'password' cannot be null"
        }
        // -- execute the service --
        return service.login(request.email, request.password)
    }

    /**
     * a function to handle request get the roles of user.
     *
     * @param httpServletRequest the [HttpServletRequest] instance.
     * @return the [RoleResponse] instance.
     */
    @GetMapping("/roles")
    @Operation(summary = "Get User Roles")
    fun getRoles(httpServletRequest: HttpServletRequest): RoleResponse {
        val id = httpServletRequest.getHeader("ID").toLong()
        // -- get the roles through service --
        return service.getRoles(id)
    }
}
