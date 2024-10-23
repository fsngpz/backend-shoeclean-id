package id.shoeclean.engine.authentications.passwords

import id.shoeclean.engine.authentications.users.UserService
import id.shoeclean.engine.notifications.events.EmailEventNotificationPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * The service class to handle feature related to password.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@Service
class PasswordService(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val emailEventNotificationPublisher: EmailEventNotificationPublisher
) {

    /**
     * a function to handle forget the password. This function will send the email to reset the password.
     *
     * @param email the email address of user.
     */
    fun forgetPassword(email: String) {
        // -- get the user by email --
        val user = userService.getByEmail(email)
        // -- create the token uid for user instance --
        val tokenUid = userService.createTokenUid(user)
        // -- publish the event for reset password --
        emailEventNotificationPublisher.publishEmailForgotPassword(email)
    }

    /**
     * a function to handle resetting the password.
     *
     * @param tokenUid the token unique identifier.
     * @param newPassword the desire new password.
     */
    fun resetPassword(tokenUid: String, newPassword: String) {
        // -- find the user by token uid --
        val user = userService.findByTokenUid(tokenUid) ?: throw NoSuchElementException("token uid $tokenUid not found")
        // -- setup new password of user --
        val encodedPassword = passwordEncoder.encode(newPassword)
        return userService.updateUserPassword(user, encodedPassword)
    }
}
