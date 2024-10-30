package id.shoeclean.engine.orders

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.PrePersist
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
class OrderListener {
    private final val ORDER_ID_PREFIX = "USCID"

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * a post persist to generate the unique identifier for [Order].
     *
     * @param order the [Order] instance.
     */
    @PrePersist
    fun generateUscId(order: Order) {
        // Generate the base parts of the USC ID
        val orderDate = OffsetDateTime.now(ZoneOffset.UTC)
        // Get last two digits of the year
        val year = orderDate.year.toString().takeLast(2)
        // Get day of the year, zero-padded
        val dayOfYear = orderDate.dayOfYear.toString().padStart(3, '0')

        // Use the generated order ID as the running number. Default to "001" if id is null
        val runningNumber = order.id?.toString()?.padStart(3, '0') ?: "001"

        // Create the USC ID
        order.uscId = "$ORDER_ID_PREFIX-$year$dayOfYear$runningNumber"

        // Merge the entity with the new orderId
        entityManager.merge(order)
    }
}
