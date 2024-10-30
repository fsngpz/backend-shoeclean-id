package id.shoeclean.engine.orders

import id.shoeclean.engine.accounts.Account
import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository of [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
interface OrderRepository : JpaRepository<Order, Long> {

    /**
     * a method to find the [Order] by [Account] and order ID.
     *
     * @param orderId the order unique identifier.
     * @param account the [Order] instance.
     * @return the [Order] or null.
     */
    fun findByOrderIdAndAccount(orderId: String, account: Account): Order?
}
