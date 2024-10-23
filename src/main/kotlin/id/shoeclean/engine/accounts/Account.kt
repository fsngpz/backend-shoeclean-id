package id.shoeclean.engine.accounts

import id.shoeclean.engine.authentications.users.User
import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * The entity class represent table of Account
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-22
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "accounts")
class Account(
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    val user: User,
) : AuditableBaseEntity() {
    var name: String? = null
    var profilePictureUrl: String? = null
}
