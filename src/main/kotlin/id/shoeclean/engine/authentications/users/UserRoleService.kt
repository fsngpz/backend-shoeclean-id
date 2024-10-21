package id.shoeclean.engine.authentications.users

import id.shoeclean.engine.authentications.roles.Role
import id.shoeclean.engine.authentications.roles.RoleResponse
import id.shoeclean.engine.exceptions.UserNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * The service class to handle feature related the [User] and [Role] or [UserRole].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Service
class UserRoleService(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository
) {

    /**
     * a function to find all data with specified filter.
     *
     * @param search the parameter for filter data by email or username.
     * @param role the parameter to filter data by role.
     * @param pageable the [Pageable].
     * @return the [Page] of [UserRole].
     */
    fun findAll(search: String?, role: String?, pageable: Pageable): Page<UserRole> {
        return userRoleRepository.findAllCustom(search, role, pageable)
    }

    /**
     * a function to handle get the [Role] of [User] by user unique identifier.
     *
     * @param userId the user unique identifier.
     * @return the [RoleResponse] instance.
     */
    fun getRoles(userId: Long): RoleResponse {
        // -- find the user --
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("no user with id $userId was found")
        // -- find the UserRole by User instance --
        val userRole = userRoleRepository.findAllByUser(user)
        // -- map to the role name --
        val roles = userRole.map { it.role.name }
        // -- return the roles --
        return RoleResponse(roles)
    }

    /**
     * a function to assign the [User] to single [Role].
     *
     * @param user the [User] instance.
     * @param role the [Role] instance.
     */
    fun assign(user: User, role: Role) {
        // -- when the user and role is duplicate then just return to avoid redundant roles --
        if (isDuplicate(user, role)) {
            return
        }
        // -- setup the instance UserRole --
        val userRole = UserRole(user, role)
        // -- save the instance to database --
        userRoleRepository.save(userRole)
    }

    /**
     * a private function to check is specified [User] and [Role] already exist in database.
     *
     * @param user the [User] instance.
     * @param role the [Role] instance.
     * @return the boolean of is the [User] and [Role] duplicated.
     */
    private fun isDuplicate(user: User, role: Role): Boolean {
        return userRoleRepository.findByUserAndRole(user, role) != null
    }
}
