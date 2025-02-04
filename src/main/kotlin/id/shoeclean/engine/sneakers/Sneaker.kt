package id.shoeclean.engine.sneakers

import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.orders.OrderSneaker
import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
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
    @JoinColumn(name = "account_id")
    val account: Account,

    var brand: String,
    var color: String
) : AuditableBaseEntity() {
    val imageUrl: String? = null

    // -- one to many --
    @OneToMany(mappedBy = "sneaker")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var orderSneakers: Set<OrderSneaker> = setOf()
}
