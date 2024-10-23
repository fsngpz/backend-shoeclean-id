package id.shoeclean.engine.authentications.users

import id.shoeclean.engine.exceptions.UserNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * The service class to handle the feature related to [User].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Service
class UserService(private val userRepository: UserRepository) {

    /**
     * a function to handle getting the [User] by email address. When not found this function will throw the
     * [UserNotFoundException].
     *
     * @param email the email address.
     * @return the [User] instance.
     */
    fun getByEmail(email: String): User {
        return findByEmail(email) ?: throw UserNotFoundException("user with email $email not found");
    }

    /**
     * a function to find the [User] by email address.
     *
     * @param email the email address.
     * @return the [User] or null
     */
    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    /**
     * a function to find the [User] by the token uid.
     *
     * @param tokenUid the token unique identifier.
     * @return the [User] or null.
     */
    fun findByTokenUid(tokenUid: String): User? {
        return userRepository.findByTokenUid(tokenUid)
    }

    /**
     * a function to check is the given email address already exist or not.
     *
     * @param email the email address of [User].
     * @return the boolean of is email already exist or not.
     */
    fun isEmailAlreadyExist(email: String): Boolean {
        return findByEmail(email) != null
    }

    /**
     * a function to create the token unique identifier for [User].
     *
     * @param user the [User] instance.
     * @return the created token uid.
     */
    fun createTokenUid(user: User): String {
        // -- generate the token unique identifier --
        val tokenUid = UUID.randomUUID().toString()
        // -- set the tokenUid to the User instance --
        user.apply {
            this.tokenUid = tokenUid
        }
        // -- save the instance --
        userRepository.save(user)
        return tokenUid
    }

    /**
     * a function to handle updating the new password of [User].
     *
     * @param user the [User] instance.
     * @param newPassword the new password.
     */
    fun updateUserPassword(user: User, newPassword: String) {
        // -- setup new password of user --
        user.apply {
            this.password = newPassword
        }
        userRepository.save(user)
    }
}
