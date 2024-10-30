package id.shoeclean.engine.vouchers

import id.shoeclean.engine.orders.Order
import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.OffsetDateTime

/**
 * The class represent the entity of voucher.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "vouchers")
class Voucher(
    val code: String,
    
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    val type: VoucherType,

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    val amountType: AmountType,
    var amount: BigDecimal,
    var expiredAt: OffsetDateTime
) : AuditableBaseEntity() {
    var quantity: Int = 0

    @OneToOne(mappedBy = "voucher")
    val order: Order? = null
}
