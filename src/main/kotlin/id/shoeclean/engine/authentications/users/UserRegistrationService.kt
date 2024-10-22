package id.shoeclean.engine.authentications.users

import com.fasterxml.jackson.databind.ObjectMapper
import id.shoeclean.engine.authentications.isEmailValid
import id.shoeclean.engine.authentications.registration.AttributeRequest
import id.shoeclean.engine.authentications.roles.RoleService
import id.shoeclean.engine.exceptions.DuplicateEmailException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * The service class to handle user registration.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Service
class UserRegistrationService(
    private val roleService: RoleService,
    private val objectMapper: ObjectMapper,
    private val userRoleService: UserRoleService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userEventPublisher: UserEventPublisher
) : UserService(userRepository) {

    /**
     * a function to create new [User].
     *
     * @param email the email address.
     * @param password the password.
     * @param roleName the name of role.
     * @param attributes the [AttributeRequest] instance.
     * @return the [User] instance.
     */
    @Transactional
    fun create(
        email: String,
        password: String,
        roleName: String,
        attributes: AttributeRequest
    ): User {
        // -- validate is the email address valid --
        require(email.isEmailValid()) { "email address is not a valid email" }
        // -- convert the attributes instance to json string --
        val stringAttributes = objectMapper.writeValueAsString(attributes)
        // -- validate is the email address already exist in database --
        require(!isEmailAlreadyExist(email)) {
            throw DuplicateEmailException("the email address '$email' is already exist in database")
        }
        // -- setup new instance of User --
        val newUser = User(
            email = email,
            password = passwordEncoder.encode(password)
        ).apply {
            this.attributes = stringAttributes
        }
        // -- save the instance of user --
        userRepository.save(newUser)
        // -- setup the instance of Role --
        val role = roleService.getOrCreate(roleName)
        // -- assign user to the role --
        userRoleService.assign(newUser, role)
        // -- publish event --
        sendEvent(newUser)
        // -- return the instance of newUser --
        return newUser
    }

    /**
     * a private function to publish event.
     *
     * @param user the [User] instance.
     */
    private fun sendEvent(user: User) {
        // -- setup the instance of UserEventRequest --
        val eventRequest = UserEventRequest(user)
        // -- publish the event --
        userEventPublisher.publish(eventRequest)
    }
}
