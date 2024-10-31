package id.shoeclean.engine.orders

/**
 * The enum class represent the status of [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
enum class OrderStatus {
    PENDING, // Order created and confirmed by user, waiting for backoffice moving the status
    PENDING_CONFIRMATION, // Order created but not confirmed by user
    ON_PICKUP,
    ON_DELIVERY,
    CLEANING,
    DELIVERED,
    CANCELED
}
