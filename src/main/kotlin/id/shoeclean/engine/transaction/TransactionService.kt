package id.shoeclean.engine.transaction

import org.springframework.stereotype.Service

/**
 * The service class to handle business logic related to [Transaction].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
@Service
class TransactionService(private val transactionRepository: TransactionRepository) {

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
            request.deduction,
            request.finalAmount
        ).apply {
            this.method = request.method
        }
        // -- save the instance --
        return transactionRepository.save(transaction)
    }
}
