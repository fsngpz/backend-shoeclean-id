package id.shoeclean.engine.transaction

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * The service class to handle business logic related to [Transaction].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
@Service
class TransactionService(private val transactionRepository: TransactionRepository) {

    /**
     * a function to find all the [Transaction] of a specific account using some parameter to
     * filter the data.
     *
     * @param accountId the account unique identifier.
     * @param filter the parameter to filter the data using USC ID.
     * @param createdAtFrom the starts date to filter the data.
     * @param createdAtTo the ends date to filter the data.
     * @param pageable the [Pageable].
     * @return the [Page] of [Transaction].
     */
    fun findAll(
        accountId: Long,
        filter: String?,
        createdAtFrom: OffsetDateTime?,
        createdAtTo: OffsetDateTime?,
        pageable: Pageable
    ): Page<Transaction> {
        return transactionRepository.findAll(
            accountId,
            filter,
            createdAtFrom ?: OffsetDateTime.of(LocalDate.EPOCH, LocalTime.MIN, ZoneOffset.UTC),
            createdAtTo ?: OffsetDateTime.now(),
            pageable
        )
    }

    /**
     * a function to handle creating new [Transaction]. This function was used by the application event listener.
     *
     * @param request the [EventNewTransactionRequest].
     * @return the [Transaction] instance.
     */
    fun create(request: EventNewTransactionRequest): Transaction {
        // -- setup new transaction --
        val transaction = Transaction(
            request.order,
            request.totalAmount,
            request.deduction
        ).apply {
            this.method = request.method
        }
        // -- save the instance --
        return transactionRepository.save(transaction)
    }
}
