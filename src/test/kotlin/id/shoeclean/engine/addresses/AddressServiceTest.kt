package id.shoeclean.engine.addresses

import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.exceptions.AddressNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import kotlin.random.Random

/**
 * The test class for [AddressService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-25
 */
@SpringBootTest(classes = [AddressService::class])
class AddressServiceTest(@Autowired private val addressService: AddressService) {
    @MockBean
    private lateinit var mockAccountService: AccountService

    @MockBean
    private lateinit var mockAddressRepository: AddressRepository

    @Test
    fun `dependencies are not null`() {
        assertThat(mockAccountService).isNotNull
        assertThat(mockAddressRepository).isNotNull
    }

    @Test
    fun `create new address, success`() {
        val label = "My House"
        val line = "St Downtown"
        val city = "Jurong East"
        val district = "Jurong"
        val subdistrict = "Selong"
        val state = "Singapore"
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressRepository.save(any<Address>())).thenAnswer { invocation ->
            val address = invocation.getArgument<Address>(0)
            address.id = 1L
            address
        }
        // -- execute --
        val result = addressService.create(1L, label, line, city, district, subdistrict, state)
        assertThat(result.label).isEqualTo(label)
        assertThat(result.line).isEqualTo(line)
        assertThat(result.city).isEqualTo(city)
        assertThat(result.district).isEqualTo(district)
        assertThat(result.subdistrict).isEqualTo(subdistrict)
        assertThat(result.state).isEqualTo(state)
        // -- verify --
        verify(mockAddressRepository).save(any<Address>())
    }

    @Test
    fun `create new address, id is null`() {
        val label = "My House"
        val line = "St Downtown"
        val city = "Jurong East"
        val district = "Jurong"
        val subdistrict = "Selong"
        val state = "Singapore"
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressRepository.save(any<Address>())).thenAnswer { invocation ->
            val address = invocation.getArgument<Address>(0)
            address.id = null
            address
        }
        // -- execute --
        assertThrows<IllegalArgumentException> {
            addressService.create(
                1L,
                label,
                line,
                city,
                district,
                subdistrict,
                state
            )
        }
        // -- verify --
        verify(mockAddressRepository).save(any<Address>())
    }

    @Test
    fun `findAll, success but empty`() {
        // -- mock --
        whenever(
            mockAddressRepository.findAllByFilter(
                any<Long>(),
                anyOrNull(),
                any<Pageable>()
            )
        ).thenReturn(Page.empty())

        // -- execute --
        val result = addressService.findAll(1L, null, Pageable.unpaged())
        assertThat(result.content).isEmpty()

        // -- verify --
        verify(mockAddressRepository).findAllByFilter(any<Long>(), anyOrNull(), any<Pageable>())
    }

    @Test
    fun `findAll, success with data`() {
        val mockAddressOne = createMockAddress()
        val mockAddressTwo = createMockAddress()
        // -- mock --
        whenever(
            mockAddressRepository.findAllByFilter(
                any<Long>(),
                anyOrNull(),
                any<Pageable>()
            )
        ).thenReturn(PageImpl(listOf(mockAddressOne, mockAddressTwo)))

        // -- execute --
        val result = addressService.findAll(1L, null, Pageable.unpaged())
        assertThat(result.content).isNotEmpty.hasSize(2)

        // -- verify --
        verify(mockAddressRepository).findAllByFilter(any<Long>(), anyOrNull(), any<Pageable>())
    }

    @Test
    fun `delete, but no address found`() {
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(null)

        // -- execute --
        assertThrows<AddressNotFoundException> { addressService.delete(1L, 2L) }

        // -- verify --
        verify(mockAddressRepository, never()).delete(any<Address>())
    }

    @Test
    fun `delete, success`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(mockAddress)

        // -- execute --
        assertAll({ addressService.delete(1L, 2L) })

        // -- verify --
        verify(mockAddressRepository).delete(any<Address>())
    }

    @Test
    fun `get, but no address found`() {
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(null)

        // -- execute --
        assertThrows<AddressNotFoundException> { addressService.get(1L, 2L) }

        // -- verify --
        verify(mockAddressRepository).findByIdAndAccount(any<Long>(), any<Account>())
    }

    @Test
    fun `get, success`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(mockAddress)

        // -- execute --
        val result = addressService.get(1L, 2L)
        assertThat(result.label).isEqualTo(mockAddress.label)
        assertThat(result.line).isEqualTo(mockAddress.line)
        assertThat(result.city).isEqualTo(mockAddress.city)
        assertThat(result.district).isEqualTo(mockAddress.district)
        assertThat(result.subdistrict).isEqualTo(mockAddress.subdistrict)
        assertThat(result.state).isEqualTo(mockAddress.state)
        // -- verify --
        verify(mockAddressRepository).findByIdAndAccount(any<Long>(), any<Account>())
    }

    private fun createMockAddress(): Address {
        val mockAccount = mock<Account>()
        val label = "My House"
        val line = "St Downtown"
        val city = "Jurong East"
        val district = "Jurong"
        val subdistrict = "Selong"
        val state = "Singapore"
        return Address(mockAccount, label, line, city, district, subdistrict, state).apply {
            this.id = Random(10L).nextLong()
        }
    }
}
