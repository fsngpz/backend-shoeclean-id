package id.shoeclean.engine.transaction

import id.shoeclean.engine.orders.OrderApplicationEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

/**
 * The listener class to handle the new [Transaction].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
@Component
class NewTransactionEventListener(private val transactionService: TransactionService) :
    ApplicationListener<OrderApplicationEvent> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * an override function to handle the event of [OrderApplicationEvent].
     *
     * @param event the [OrderApplicationEvent].
     */
    @Async
    override fun onApplicationEvent(event: OrderApplicationEvent) {
        logger.info("Recieving event order with value: $event")
        // convert the source to instance of User --
        val eventResponse = event.source as EventNewTransactionRequest
        // -- create new transaction --
        transactionService.create(eventResponse)
        logger.info("Finished handle event order with value: $event")
    }

}
