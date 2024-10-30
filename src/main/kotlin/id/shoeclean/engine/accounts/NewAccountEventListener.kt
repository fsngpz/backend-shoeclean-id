package id.shoeclean.engine.accounts

import id.shoeclean.engine.authentications.users.UserApplicationEvent
import id.shoeclean.engine.authentications.users.UserEventRequest
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

/**
 * The listener class to handle new [Account].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@Component
class NewAccountEventListener(private val accountService: AccountService) : ApplicationListener<UserApplicationEvent> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * an override function to handle the [ApplicationListener] of [UserApplicationEvent].
     *
     * @param event the [UserApplicationEvent] instance.
     */
    @Async
    override fun onApplicationEvent(event: UserApplicationEvent) {
        logger.info("Receiving the event with value: $event")
        // convert the source to instance of User --
        val eventResponse = event.source as UserEventRequest
        // -- get the user instance --
        val user = eventResponse.user
        // -- create the new account --
        accountService.create(user)
        logger.info("Finished handle event with value: $event")
    }

}
