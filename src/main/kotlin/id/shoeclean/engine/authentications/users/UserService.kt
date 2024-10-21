package id.shoeclean.engine.authentications.users

import id.shoeclean.engine.exceptions.UserNotFoundException
import org.springframework.stereotype.Service

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
     * a function to check is the given email address already exist or not.
     *
     * @param email the email address of [User].
     * @return the boolean of is email already exist or not.
     */
    fun isEmailAlreadyExist(email: String): Boolean {
        return findByEmail(email) != null
    }
}
