package id.shoeclean.engine.authentications.users

import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository of [User].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
interface UserRepository : JpaRepository<User, Long> {
}
