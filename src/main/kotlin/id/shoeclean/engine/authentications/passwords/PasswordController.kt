package id.shoeclean.engine.authentications.passwords

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * the REST Controller of feature related to password.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@RestController
@RequestMapping("/v1/auth/passwords")
@Tag(name = "Authentication API")
class PasswordController(private val passwordService: PasswordService) {

    /**
     * a POST mapping to do request forgot password.
     *
     * @param request the [ForgotPasswordRequest] instance.
     */
    @PostMapping("/forgot")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Forgot Password")
    fun forgotPassword(@RequestBody request: ForgotPasswordRequest) {
        // -- validate the request --
        requireNotNull(request.email) {
            "email cannot be null"
        }
        return passwordService.forgetPassword(request.email)
    }

    /**
     * a POST mapping to handle reset the password.
     *
     * @param request the [ResetPasswordRequest] instance.
     */
    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Reset Password")
    fun resetPassword(
        @RequestBody request: ResetPasswordRequest
    ) {
        // -- validate the request --
        requireNotNull(request.password) {
            "password cannot be null"
        }
        requireNotNull(request.tokenUid) {
            "token uid cannot be null"
        }
        return passwordService.resetPassword(request.tokenUid, request.password)
    }
}
