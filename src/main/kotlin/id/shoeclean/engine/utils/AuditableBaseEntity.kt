package id.shoeclean.engine.utils

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime

/**
 * The class that contains the auditing field/column such as createdAt, creatorId, updatedAt and
 * updaterId. This class also implements the [BaseEntity].
 *
 * @author Ferdinand Sangap
 * @since 2024-10-21
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditableBaseEntity(
    @CreatedBy
    var creatorId: String = "SYSTEM",
    @CreationTimestamp
    var createdAt: OffsetDateTime = OffsetDateTime.now(),

    @LastModifiedBy
    var updaterId: String? = null,
    @UpdateTimestamp
    var updatedAt: OffsetDateTime? = null
) : BaseEntity()
