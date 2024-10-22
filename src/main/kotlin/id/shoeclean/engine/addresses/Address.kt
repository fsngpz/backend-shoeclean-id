package id.shoeclean.engine.addresses

import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.utils.AuditableBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * The entity class represent table of Address.
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-22
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "addresses")
class Address(
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    val account: Account,

    var label: String,
    var line: String,
    var city: String,
    var district: String,
    var subdistrict: String
) : AuditableBaseEntity()
