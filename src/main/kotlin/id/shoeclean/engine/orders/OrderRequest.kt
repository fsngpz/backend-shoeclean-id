package id.shoeclean.engine.orders

import id.shoeclean.engine.catalogs.ServiceType

/**
 * The request class for [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
data class OrderRequest(
    val serviceType: ServiceType?,
    val totalPairs: Int?,
    val sneakerIds: List<Long>?,
    val addressId: Long?
)
