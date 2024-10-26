package id.shoeclean.engine.catalogs

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
class Catalog(
    var serviceType: ServiceType,
    var description: String,
    var price: BigDecimal
) : AuditableBaseEntity()
