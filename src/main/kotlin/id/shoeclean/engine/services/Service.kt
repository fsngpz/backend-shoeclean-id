package id.shoeclean.engine.services

import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal

/**
 * The class represent the entity of Service.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "services")
class Service(
    var type: ServiceType,
    var description: String,
    var price: BigDecimal
) : AuditableBaseEntity()
