package id.shoeclean.engine.orders

import id.shoeclean.engine.transaction.EventNewTransactionRequest
import org.springframework.context.ApplicationEvent

/**
 * The application event class for new [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
class OrderApplicationEvent(payload: EventNewTransactionRequest) : ApplicationEvent(payload)
