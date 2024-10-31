package id.shoeclean.engine.orders

import id.shoeclean.engine.transaction.EventNewTransactionRequest
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

/**
 * The publisher event for [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
@Component
class OrderEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * a function to publish the [EventNewTransactionRequest].
     *
     * @param payload the [EventNewTransactionRequest] instance.
     */
    fun publish(payload: EventNewTransactionRequest) {
        log.info("Publishing new transaction request with payload : {}", payload.toString())
        // -- setup the application event --
        val event = OrderApplicationEvent(payload)
        // -- publish the event --
        applicationEventPublisher.publishEvent(event)
        log.info("Published new transaction request with payload : {}", payload.toString())
    }
}
