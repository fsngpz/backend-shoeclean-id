package id.shoeclean.engine.orders

import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository of [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
interface OrderRepository : JpaRepository<Order, Long>
