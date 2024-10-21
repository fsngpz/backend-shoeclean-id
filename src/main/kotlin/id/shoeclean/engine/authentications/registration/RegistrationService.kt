package id.shoeclean.engine.authentications.registration

import id.shoeclean.engine.authentications.users.User
import id.shoeclean.engine.authentications.users.UserRegistrationService
import org.springframework.stereotype.Service

/**
 * The service class to handle feature related to User Onboarding.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Service
class RegistrationService(private val userRegistrationService: UserRegistrationService) {
    
    /**
     * a function to handle request register new [User].
     *
     * @param request the [RegistrationRequest].
     */
    fun register(request: RegistrationRequest) {
        // -- validate field 'email' --
        require(!request.email.isNullOrEmpty()) {
            "field 'email' cannot be null or empty"
        }
        // -- validate field 'password' --
        require(!request.password.isNullOrEmpty()) {
            "field 'password' cannot be null or empty"
        }
        // -- validate field 'attributes' --
        require(request.attributes != null) {
            "field 'attributes' cannot be null"
        }

        // -- create the new user --
        userRegistrationService.create(
            email = request.email,
            password = request.password,
            attributes = request.attributes,
            roleName = "ROLE_USER"
        )
    }
}
