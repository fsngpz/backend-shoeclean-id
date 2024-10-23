package id.shoeclean.engine.authentications.users

import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository of [User].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
interface UserRepository : JpaRepository<User, Long> {

    /**
     * a method to find the [User] by given email address.
     *
     * @param email the email address of [User].
     * @return the [User] or null.
     */
    fun findByEmail(email: String): User?

    /**
     * a method to find the [User] using the token uid.
     *
     * @param tokenUid the token unique identifier.
     * @return the [User] or null.
     */
    fun findByTokenUid(tokenUid: String): User?
}
