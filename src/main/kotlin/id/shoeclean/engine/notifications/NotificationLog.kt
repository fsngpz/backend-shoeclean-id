package id.shoeclean.engine.notifications

import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * The entity class represent the Notification Log.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "notification_logs")
class NotificationLog(
    @ManyToOne
    @JoinColumn(name = "account_id")
    val account: Account,

    val channel: NotificationChannel,
    val type: String,
    val content: String
) : AuditableBaseEntity() {
    var status: NotificationStatus = NotificationStatus.PENDING
}
