package id.shoeclean.engine.authentications.users

import id.shoeclean.engine.authentications.roles.Role
import id.shoeclean.engine.utils.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

/**
 * The entity class of join table for [User] and [Role].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Entity
@Table(name = "users_roles")
class UserRole(
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne
    @JoinColumn(name = "role_id")
    val role: Role
) : BaseEntity()
