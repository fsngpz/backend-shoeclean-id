package id.shoeclean.engine.orders

import id.shoeclean.engine.addresses.AddressResponse
import id.shoeclean.engine.catalogs.ServiceType
import java.math.BigDecimal

/**
 * The data class represent the detail of [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-30
 */
data class OrderDetailResponse(
    val uscId: String,
    val address: AddressResponse,
    val totalPairs: Int,
    val serviceType: ServiceType,
    val price: BigDecimal,
    val discount: BigDecimal,
    val deliveryFee: BigDecimal,
    val subtotal: BigDecimal, // Subtotal represent the price * total pairs
    val totalAmount: BigDecimal // Total amount represent the subtotal + delivery fee - discount
)
