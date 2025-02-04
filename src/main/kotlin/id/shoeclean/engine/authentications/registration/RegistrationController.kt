package id.shoeclean.engine.authentications.registration

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * The REST Controller of feature related to Registration.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Authentication API")
class RegistrationController(private val registrationService: RegistrationService) {

    /**
     * a POST mapping to do register the new User.
     *
     * @param request the [RegistrationRequest] payload.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register an Account")
    fun register(@RequestBody request: RegistrationRequest) {
        // -- register the user --
        return registrationService.register(request)
    }
}
