package id.shoeclean.engine.orders

/**
 * The enum class represent the status of [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
enum class OrderStatus {
    PENDING,
    ON_PICKUP,
    ON_DELIVERY,
    CLEANING,
    DELIVERED,
    CANCELED
}
