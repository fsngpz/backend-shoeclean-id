package id.shoeclean.engine.orders

import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.addresses.Address
import id.shoeclean.engine.catalogs.Catalog
import id.shoeclean.engine.utils.AuditableBaseEntity
import id.shoeclean.engine.vouchers.Voucher
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
 * The class represent the entity of Order.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@Entity
@EntityListeners(value = [AuditingEntityListener::class, OrderListener::class])
@Table(name = "orders")
class Order(

    @OneToOne
    @JoinColumn(name = "account_id")
    val account: Account,

    @OneToOne
    @JoinColumn(name = "address_id")
    val address: Address,

    @OneToOne
    @JoinColumn(name = "catalog_id")
    val catalog: Catalog,

    val status: OrderStatus,
    val totalPairs: Int
) : AuditableBaseEntity() {
    var orderId: String? = null

    @OneToOne
    @JoinColumn(name = "voucher_id")
    val voucher: Voucher? = null

    // -- one to many --
    @OneToMany(mappedBy = "order")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var orderSneakers: Set<OrderSneaker> = setOf()
}
