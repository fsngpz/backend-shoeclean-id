package id.shoeclean.engine.authentications.users

import id.shoeclean.engine.authentications.roles.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * The interface represent the repository of [UserRole].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
interface UserRoleRepository : JpaRepository<UserRole, Long> {

    /**
     * a function to find the instance [UserRole] by user and role.
     *
     * @param user the [User] instance.
     * @param role the [Role] instance.
     * @return the [UserRole] or  null.
     */
    fun findByUserAndRole(user: User, role: Role): UserRole?


    /**
     * a function to handle find the [UserRole] by [User] instance.
     *
     * @param user the [User] instance.
     * @return the [List] of [UserRole].
     */
    fun findAllByUser(user: User): List<UserRole>

    /**
     * a function to find all [UserRole] with custom query.
     *
     * @param search the parameter for filter data by email or username.
     * @param role the parameter to filter data by role.
     * @param pageable the [Pageable].
     * @return the [Page] of [UserRole].
     */
    @Query(
        """
    FROM UserRole ur
    WHERE ((:search IS NULL)
            OR (ur.user.email ILIKE %:#{#search}%) 
            OR (ur.user.username ILIKE %:#{#search}%))
    AND ((:role IS NULL) OR (ur.role.name ILIKE %:#{#role}%))
  """
    )
    fun findAllCustom(search: String?, role: String?, pageable: Pageable): Page<UserRole>
}
