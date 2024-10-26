package id.shoeclean.engine.orders

import jakarta.persistence.PostPersist
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * The listener class for [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@Component
class OrderListener(private val orderRepository: OrderRepository) {
    private final val ORDER_ID_PREFIX = "USCID"

    /**
     * a post persist to generate the unique identifier for [Order].
     *
     * @param order the [Order] instance.
     */
    @PostPersist
    fun generateOrderId(order: Order) {
        // Generate the base parts of the order ID
        val orderDate = OffsetDateTime.now(ZoneOffset.UTC)
        // Get last two digits of the year
        val year = orderDate.year.toString().takeLast(2)
        // Get day of the year, zero-padded
        val dayOfYear = orderDate.dayOfYear.toString().padStart(3, '0')

        // Use the generated order ID as the running number. Default to "001" if id is null
        val runningNumber = order.id?.toString()?.padStart(3, '0') ?: "001"

        // Create the order ID
        order.orderId = "$ORDER_ID_PREFIX-$year$dayOfYear$runningNumber"

        // Save the order again to persist the generated orderId
        orderRepository.save(order)
    }
}
