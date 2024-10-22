package id.shoeclean.engine.authentications.users

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

/**
 * The publisher event for [User].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-22
 */
@Component
class UserEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * a function to publish the [UserEventRequest].
     *
     * @param payload the [UserEventRequest] instance.
     */
    fun publish(payload: UserEventRequest) {
        logger.info("Starting to publish event : {}", payload)
        // -- setup the application event --
        val event = UserApplicationEvent(payload)
        // -- publish the event --
        applicationEventPublisher.publishEvent(event)
        logger.info("Finished to publish event : {}", payload)
    }
}
