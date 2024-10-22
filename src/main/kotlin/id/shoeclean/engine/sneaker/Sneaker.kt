package id.shoeclean.engine.sneaker

import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * The entity class represent table of Sneaker.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-22
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "sneakers")
class Sneaker(
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    val account: Account,

    var brand: String,
    var color: String
) : AuditableBaseEntity() {
    val imageUrl: String? = null
}
