package id.shoeclean.engine.transaction

import id.shoeclean.engine.orders.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.OffsetDateTime

/**
 * The interface represent the repository of [Transaction].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
interface TransactionRepository : JpaRepository<Transaction, Long> {

    /**
     * a method to find the [Transaction] using the filter and range date of created at.
     *
     * @param filter the parameter to filter the data by USC ID.
     * @param createdAtFrom the starts date to filter data.
     * @param createdAtTo the ends date to filter data.
     * @param pageable the [Pageable].
     * @return the [Page] of [Transaction].
     */
    @Query(
        value = """
            FROM Transaction t
            WHERE t.order.account.id = :accountId
            AND (:filter IS NULL OR t.order.uscId = :filter)
            AND t.createdAt >= :createdAtFrom
            AND t.createdAt <= :createdAtTo
        """
    )
    fun findAll(
        accountId: Long,
        filter: String?,
        createdAtFrom: OffsetDateTime,
        createdAtTo: OffsetDateTime,
        pageable: Pageable
    ): Page<Transaction>

    /**
     * a method to find the [Transaction] using the [Order] instance.
     *
     * @param order the [Order] instance.
     * @return the [Transaction] or null.
     */
    fun findByOrder(order: Order): Transaction?
}
