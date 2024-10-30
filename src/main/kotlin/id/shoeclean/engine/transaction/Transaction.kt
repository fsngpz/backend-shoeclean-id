package id.shoeclean.engine.transaction

import id.shoeclean.engine.orders.Order
import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal

/**
 * The class represent the entity of transaction.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "transactions")
class Transaction(
    @OneToOne
    @JoinColumn(name = "order_id")
    val order: Order,
    val totalAmount: BigDecimal,
    val deduction: BigDecimal,
    val finalAmount: BigDecimal,
) : AuditableBaseEntity() {
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var status: TransactionStatus = TransactionStatus.UNPAID

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    var method: TransactionMethod = TransactionMethod.CASH_ON_DELIVERY
}
