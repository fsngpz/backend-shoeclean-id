package id.shoeclean.engine.accounts

import id.shoeclean.engine.authentications.users.User
import id.shoeclean.engine.authentications.users.UserApplicationEvent
import id.shoeclean.engine.authentications.users.UserEventRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

/**
 * The test class for [NewAccountEventListener]
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@SpringBootTest(classes = [NewAccountEventListener::class])
class NewAccountEventListenerTest(@Autowired val listener: NewAccountEventListener) {
    @MockBean
    private lateinit var mockAccountService: AccountService

    @Test
    fun `dependencies are not null`() {
        assertThat(listener).isNotNull
        assertThat(mockAccountService).isNotNull
    }

    @Test
    fun `onApplicationEvent, success`() {
        val user = User("example@mail.com", "pass")
        val request = UserEventRequest(user)
        val event = UserApplicationEvent(request)

        // -- execute and verify --
        assertAll({ listener.onApplicationEvent(event) })
        verify(mockAccountService).create(any<User>())
    }
}
