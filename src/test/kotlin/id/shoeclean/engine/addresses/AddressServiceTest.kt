package id.shoeclean.engine.addresses

import com.fasterxml.jackson.databind.ObjectMapper
import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.exceptions.AddressNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
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
@Import(ObjectMapper::class)
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

    @Test
    fun `put, exception thrown because there is a null value in request`() {
        var request = AddressRequestNullable(
            label = null,
            line = null,
            city = null,
            district = null,
            subdistrict = null,
            state = null,
            isSelected = null
        )

        // -- execute with label null --
        assertThrows<IllegalArgumentException> { addressService.put(1L, 2L, request) }

        request = AddressRequestNullable(
            label = "Label",
            line = null,
            city = null,
            district = null,
            subdistrict = null,
            state = null,
            isSelected = null
        )
        // -- execute with line null --
        assertThrows<IllegalArgumentException> { addressService.put(1L, 2L, request) }

        request = AddressRequestNullable(
            label = "Label",
            line = "Line",
            city = null,
            district = null,
            subdistrict = null,
            state = null,
            isSelected = null
        )
        // -- execute with city null --
        assertThrows<IllegalArgumentException> { addressService.put(1L, 2L, request) }

        request = AddressRequestNullable(
            label = "Label",
            line = "Line",
            city = "City",
            district = null,
            subdistrict = null,
            state = null,
            isSelected = null
        )
        // -- execute with district null --
        assertThrows<IllegalArgumentException> { addressService.put(1L, 2L, request) }

        request = AddressRequestNullable(
            label = "Label",
            line = "Line",
            city = "City",
            district = "District",
            subdistrict = null,
            state = null,
            isSelected = null
        )
        // -- execute with subdistrict null --
        assertThrows<IllegalArgumentException> { addressService.put(1L, 2L, request) }

        request = AddressRequestNullable(
            label = "Label",
            line = "Line",
            city = "City",
            district = "District",
            subdistrict = "Subdistrict",
            state = null,
            isSelected = null
        )
        // -- execute with state null --
        assertThrows<IllegalArgumentException> { addressService.put(1L, 2L, request) }

        // -- verify --
        verify(mockAddressRepository, never()).findByIdAndAccount(any<Long>(), any<Account>())
        verify(mockAddressRepository, never()).save(any<Address>())
    }

    @Test
    fun `put, success`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
        val request = AddressRequestNullable(
            label = "Label",
            line = "Line",
            city = "City",
            district = "District",
            subdistrict = "Subdistrict",
            state = "State",
            isSelected = null
        )
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(mockAddress)
        whenever(mockAddressRepository.save(any<Address>())).thenReturn(mockAddress)

        // -- execute --
        val result = addressService.put(1L, 2L, request)
        assertThat(result.javaClass).isEqualTo(Address::class.java)

        // -- captor --
        val captor = argumentCaptor<Address>()
        verify(mockAddressRepository).save(captor.capture())
        val captured = captor.firstValue

        assertThat(captured.label).isEqualTo(mockAddress.label)
        assertThat(captured.line).isEqualTo(mockAddress.line)
        assertThat(captured.city).isEqualTo(mockAddress.city)
        assertThat(captured.district).isEqualTo(mockAddress.district)
        assertThat(captured.subdistrict).isEqualTo(mockAddress.subdistrict)
        assertThat(captured.state).isEqualTo(mockAddress.state)
        assertThat(captured.isMainAddress).isFalse
        // -- verify --
        verify(mockAddressRepository).findByIdAndAccount(any<Long>(), any<Account>())
    }

    @Test
    fun `patch, success update isSelected`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
        val jsonString = """
            {
            "isSelected": true
            }
        """.trimIndent()
        val mapper = ObjectMapper()
        val request = mapper.readTree(jsonString)
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(mockAddress)
        whenever(mockAddressRepository.save(any<Address>())).thenReturn(mockAddress)

        // -- execute --
        val result = addressService.patch(1L, 2L, request)
        assertThat(result.javaClass).isEqualTo(Address::class.java)

        // -- captor --
        val captor = argumentCaptor<Address>()
        verify(mockAddressRepository).save(captor.capture())
        val captured = captor.firstValue

        assertThat(captured.label).isEqualTo(mockAddress.label)
        assertThat(captured.line).isEqualTo(mockAddress.line)
        assertThat(captured.city).isEqualTo(mockAddress.city)
        assertThat(captured.district).isEqualTo(mockAddress.district)
        assertThat(captured.subdistrict).isEqualTo(mockAddress.subdistrict)
        assertThat(captured.state).isEqualTo(mockAddress.state)
        assertThat(captured.isMainAddress).isTrue()
        // -- verify --
        verify(mockAddressRepository, atLeastOnce()).findByIdAndAccount(any<Long>(), any<Account>())
    }

    @Test
    fun `setMainAddress, success`() {
        val addressId = 2L
        val mockAccount = mock<Account>()
        val mockAddressOne = createMockAddress().apply { this.id = 1 }
        val mockAddressTwo = createMockAddress().apply { this.id = 2 }
        val mockAddressThree = createMockAddress().apply { this.id = 3 }

        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressRepository.findAllByAccount(any<Account>())).thenReturn(
            listOf(
                mockAddressOne,
                mockAddressTwo,
                mockAddressThree
            )
        )

        // -- execute and verify --
        assertAll({ addressService.setMainAddress(1L, addressId) })

        // -- captor --
        val captor = argumentCaptor<List<Address>>()
        verify(mockAddressRepository).saveAll(captor.capture())
        val captured = captor.firstValue

        captured.forEach {
            if (it.id == addressId) {
                assertThat(it.isMainAddress).isTrue
            } else {
                assertThat(it.isMainAddress).isFalse
            }
        }
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
