package id.shoeclean.engine.orders

import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository of [OrderSneaker].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
interface OrderSneakerRepository : JpaRepository<OrderSneaker, Long>
