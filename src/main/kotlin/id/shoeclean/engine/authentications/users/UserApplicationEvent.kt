package id.shoeclean.engine.authentications.users

import org.springframework.context.ApplicationEvent

/**
 * The application event class for [User].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-22
 */
class UserApplicationEvent(payload: UserEventRequest) : ApplicationEvent(payload)
