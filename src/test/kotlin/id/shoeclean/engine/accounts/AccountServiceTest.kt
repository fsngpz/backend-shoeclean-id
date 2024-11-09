package id.shoeclean.engine.accounts

import com.fasterxml.jackson.databind.ObjectMapper
import id.shoeclean.engine.addresses.Address
import id.shoeclean.engine.authentications.users.User
import id.shoeclean.engine.exceptions.AccountNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import java.util.Optional

/**
 * The test class for [AccountService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@SpringBootTest(classes = [AccountService::class])
@Import(value = [ObjectMapper::class])
class AccountServiceTest(@Autowired private val accountService: AccountService) {

    @MockBean
    private lateinit var mockAccountRepository: AccountRepository

    @Test
    fun `dependencies are not null`() {
        assertThat(accountService).isNotNull
        assertThat(mockAccountRepository).isNotNull
    }

    @Test
    fun `get, account not found`() {
        // -- mock --
        whenever(mockAccountRepository.findById(any<Long>())).thenReturn(Optional.empty())

        // -- execute --
        assertThrows<AccountNotFoundException> { accountService.get(1L) }

        // -- verify --
        verify(mockAccountRepository).findById(any<Long>())
    }

    @Test
    fun `get, account found`() {
        val mockAccount = Account(User("example@mail.com", "pass"))
        // -- mock --
        whenever(mockAccountRepository.findById(any<Long>())).thenReturn(Optional.of(mockAccount))

        // -- execute --
        val result = accountService.get(1L)
        assertThat(result).isSameAs(mockAccount)

        // -- verify --
        verify(mockAccountRepository).findById(any<Long>())
    }

    @Test
    fun `create WITHOUT attributes`() {
        val user = User("example@mail.com", "pass")
        val mockAccount = Account(user)
        // -- mock --
        whenever(mockAccountRepository.save(any<Account>())).thenReturn(mockAccount)

        // -- execute --
        val result = accountService.create(user)
        assertThat(result).isSameAs(mockAccount)

        // -- verify --
        verify(mockAccountRepository).save(any<Account>())

        // -- captor --
        val captor = argumentCaptor<Account>()
        verify(mockAccountRepository).save(captor.capture())
        val capturedAccount = captor.firstValue
        assertThat(capturedAccount.name).isNull()
    }

    @Test
    fun `create WITH attributes`() {
        val name = "John Doe"
        val user = User("example@mail.com", "pass").apply {
            this.attributes = """
                {"name": "$name"}
            """.trimIndent()
        }
        val mockAccount = Account(user)
        // -- mock --
        whenever(mockAccountRepository.save(any<Account>())).thenReturn(mockAccount)

        // -- execute --
        val result = accountService.create(user)
        assertThat(result).isSameAs(mockAccount)

        // -- verify --
        verify(mockAccountRepository).save(any<Account>())

        // -- captor --
        val captor = argumentCaptor<Account>()
        verify(mockAccountRepository).save(captor.capture())
        val capturedAccount = captor.firstValue
        assertThat(capturedAccount.name).isEqualTo(name)
    }

    @Test
    fun `getDetails, not found`() {
        // -- mock --
        whenever(mockAccountRepository.findById(any<Long>())).thenReturn(Optional.empty())

        // -- execute and verify --
        assertThrows<AccountNotFoundException> { accountService.get(1L) }
    }

    @Test
    fun `getDetails, success`() {
        val mockAccount = Account(user = User("example@mail.com", "pass")).apply {
            this.id = 1L
            this.name = "John Doe"
            this.profilePictureUrl = "image.url"
            this.address = Address(
                this,
                "Label",
                "Line",
                "City",
                "District",
                "Subdistrict",
                "Indonesia"
            ).apply { this.id = 1L }
        }
        // -- mock --
        whenever(mockAccountRepository.findById(any<Long>())).thenReturn(Optional.of(mockAccount))

        // -- execute and verify --
        val result = accountService.getDetails(1L)
        assertThat(result.name).isEqualTo(mockAccount.name)
        assertThat(result.profilePictureUrl).isEqualTo(mockAccount.profilePictureUrl)
        assertThat(result.email).isEqualTo(mockAccount.user.email)
        assertThat(result.mobile).isEqualTo(mockAccount.user.mobile)
        assertThat(result.isEmailVerified).isFalse()
        assertThat(result.mainAddress).isNotNull
    }
}
