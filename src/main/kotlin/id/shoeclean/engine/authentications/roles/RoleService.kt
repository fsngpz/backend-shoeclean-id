package id.shoeclean.engine.authentications.roles

import org.springframework.stereotype.Service

/**
 * The service class to handle feature related [Role].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Service
class RoleService(private val roleRepository: RoleRepository) {

    /**
     * a function to handle get the [Role] or creating the new one if there is no role before.
     *
     * @param name the role name.
     * @param description the description of role.
     * @return the [Role] instance.
     */
    fun getOrCreate(name: String, description: String? = null): Role {
        // -- find the role with given name --
        val role = roleRepository.findByNameIgnoreCase(name) ?: Role(name).apply {
            this.description = description
        }
        // -- save the instance--
        return roleRepository.save(role)
    }
}
