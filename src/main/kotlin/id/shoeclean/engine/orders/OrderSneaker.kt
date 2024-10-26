package id.shoeclean.engine.orders

import id.shoeclean.engine.sneakers.Sneaker
import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * The class represent the entity of Orders Sneakers
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "orders_sneakers")
class OrderSneaker(
    @ManyToOne
    @JoinColumn(name = "sneaker_id")
    val sneaker: Sneaker,

    @ManyToOne
    @JoinColumn(name = "order_id")
    val order: Order
) : AuditableBaseEntity()
