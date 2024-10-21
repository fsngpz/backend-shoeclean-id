package id.shoeclean.engine.authentications.roles

import id.shoeclean.engine.authentications.users.UserRole
import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * The entity class of Role.
s *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "roles")
class Role(
    val name: String
) : AuditableBaseEntity() {
    // -- optional --
    var description: String? = null

    // -- one to many --
    @OneToMany(mappedBy = "role")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var users: Set<UserRole> = setOf()
}
