package id.shoeclean.engine.orders

import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.addresses.Address
import id.shoeclean.engine.catalogs.Catalog
import id.shoeclean.engine.utils.AuditableBaseEntity
import id.shoeclean.engine.vouchers.Voucher
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcType
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * The class represent the entity of Order.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@Entity
@EntityListeners(value = [AuditingEntityListener::class])
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

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    val status: OrderStatus,
    val totalPairs: Int
) : AuditableBaseEntity() {
    var uscId: String? = null

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    var voucher: Voucher? = null

    // -- one to many --
    @OneToMany(mappedBy = "order")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var orderSneakers: Set<OrderSneaker> = setOf()

    @PrePersist
    fun generateUscId() {
        val uscIdPrefix = "USCID"
        // Generate the base parts of the USC ID
        val orderDate = OffsetDateTime.now(ZoneOffset.UTC)
        // Get last two digits of the year
        val year = orderDate.year.toString().takeLast(2)
        // Get day of the year, zero-padded
        val dayOfYear = orderDate.dayOfYear.toString().padStart(3, '0')

        // Generate running number using random or custom sequential logic
        val runningNumber = (1..9999).random().toString().padStart(3, '0')

        // Create the USC ID
        this.uscId = "$uscIdPrefix-$year$dayOfYear$runningNumber"
    }
}
