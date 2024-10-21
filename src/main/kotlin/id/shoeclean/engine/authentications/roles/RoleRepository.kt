package id.shoeclean.engine.authentications.roles

import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository of [Role].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
interface RoleRepository : JpaRepository<Role, Long> {

    /**
     * a function to handle find the [Role] by name.
     *
     * @param roleName the role name.
     * @return the [Role] or null.
     */
    fun findByNameIgnoreCase(roleName: String): Role?
}
