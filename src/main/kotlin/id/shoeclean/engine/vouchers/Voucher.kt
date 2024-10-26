package id.shoeclean.engine.vouchers

import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Table
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
    val type: VoucherType,
    val amountType: AmountType,
    var amount: BigDecimal,
    var expiredAt: OffsetDateTime
) : AuditableBaseEntity() {
    var quantity: Int = 0
}
