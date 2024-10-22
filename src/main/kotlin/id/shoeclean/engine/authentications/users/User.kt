package id.shoeclean.engine.authentications.users

import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime

/**
 * The entity model class for User.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-21
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "users")
class User(
    val email: String,
    val password: String,
) : AuditableBaseEntity() {
    // -- optional --
    var mobile: String? = null
    var username: String? = null
    var attributes: String? = null
    var tokenUid: String? = null
    var emailVerifiedAt: OffsetDateTime? = null

    // -- many to one --
    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var roles: Set<UserRole> = setOf()
}
