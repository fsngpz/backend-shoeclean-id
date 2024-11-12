package id.shoeclean.engine.sneakers

import com.fasterxml.jackson.databind.ObjectMapper
import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.exceptions.SneakerNotFoundException
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

/**
 * The test class for [SneakerService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@SpringBootTest(classes = [SneakerService::class])
@Import(ObjectMapper::class)
class SneakerServiceTest(@Autowired private val sneakerService: SneakerService) {
    @MockBean
    private lateinit var mockSneakerRepository: SneakerRepository

    @MockBean
    private lateinit var mockAccountService: AccountService

    @Test
    fun `dependencies are not null`() {
        assertThat(sneakerService).isNotNull
        assertThat(mockSneakerRepository).isNotNull
        assertThat(mockAccountService).isNotNull
    }

    @Test
    fun `create, success`() {
        val brand = "Adidas"
        val color = "Red"
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockSneakerRepository.save(any<Sneaker>())).thenAnswer { invocation ->
            val savedSneaker = invocation.getArgument<Sneaker>(0)
            savedSneaker.id = 100L
            savedSneaker
        }

        // -- execute --
        val result = sneakerService.create(1L, brand, color)
        assertThat(result.color).isEqualTo(color)
        assertThat(result.brand).isEqualTo(brand)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockSneakerRepository).save(any<Sneaker>())
    }

    @Test
    fun `delete, no sneaker was found`() {
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockSneakerRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(null)

        // -- execute --
        assertThrows<SneakerNotFoundException> { sneakerService.delete(1L, 2L) }

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockSneakerRepository).findByIdAndAccount(any<Long>(), any<Account>())
        verify(mockSneakerRepository, never()).delete(any<Sneaker>())
    }

    @Test
    fun `delete, success`() {
        val mockAccount = mock<Account>()
        val mockSneaker = mock<Sneaker>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockSneakerRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(mockSneaker)

        // -- execute --
        assertAll({ sneakerService.delete(1L, 2L) })

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockSneakerRepository).findByIdAndAccount(any<Long>(), any<Account>())
        verify(mockSneakerRepository).delete(any<Sneaker>())
    }

    @Test
    fun `findAll, success with empty data`() {
        // -- mock --
        whenever(mockSneakerRepository.findAllByFilter(any<Long>(), anyOrNull(), any<Pageable>())).thenReturn(
            Page.empty()
        )

        // -- execute --
        val result = sneakerService.findAll(1L, null, Pageable.unpaged())
        assertThat(result).isEmpty()

        // -- verify --
        verify(mockSneakerRepository).findAllByFilter(any<Long>(), anyOrNull(), any<Pageable>())
    }

    @Test
    fun `findAll, success with data`() {
        val brand = "SHOE"
        val color = "RED"
        val mockAccount = mock<Account>()
        val mockSneakerOne = Sneaker(mockAccount, brand, color).apply { this.id = 1 }
        val mockSneakerTwo = Sneaker(mockAccount, brand, color).apply { this.id = 2 }
        val mockSneakerThree = Sneaker(mockAccount, brand, color).apply { this.id = 3 }
        val mockSneakers = listOf(mockSneakerOne, mockSneakerTwo, mockSneakerThree)
        // -- mock --
        whenever(mockSneakerRepository.findAllByFilter(any<Long>(), anyOrNull(), any<Pageable>())).thenReturn(
            PageImpl(mockSneakers)
        )

        // -- execute --
        val result = sneakerService.findAll(1L, null, Pageable.unpaged())
        assertThat(result).isNotEmpty
        assertThat(result.size).isEqualTo(mockSneakers.size)
        result.content.forEach {
            assertThat(it.brand).isEqualTo(brand)
            assertThat(it.color).isEqualTo(color)
        }

        // -- verify --
        verify(mockSneakerRepository).findAllByFilter(any<Long>(), anyOrNull(), any<Pageable>())
    }

    @Test
    fun `get, sneaker not found`() {
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockSneakerRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(null)

        // -- execute --
        assertThrows<SneakerNotFoundException> { sneakerService.get(1L, 2L) }

        // -- verify --
        verify(mockSneakerRepository).findByIdAndAccount(any<Long>(), any<Account>())
    }

    @Test
    fun `get and success`() {
        val mockAccount = mock<Account>()
        val mockSneaker = Sneaker(mockAccount, "Sneaker", "Blue").apply { this.id = 1 }
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockSneakerRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(mockSneaker)

        // -- execute --
        val result = sneakerService.get(1L, 2L)
        assertThat(result.color).isEqualTo(mockSneaker.color)
        assertThat(result.brand).isEqualTo(mockSneaker.brand)
        assertThat(result.id).isEqualTo(mockSneaker.id)

        // -- verify --
        verify(mockSneakerRepository).findByIdAndAccount(any<Long>(), any<Account>())
    }

    @Test
    fun `findAll list of sneakers, success but return empty`() {
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockSneakerRepository.findByIdInAndAccount(any<List<Long>>(), any<Account>())).thenReturn(listOf())

        // -- execute --
        val result = sneakerService.findAll(1L, listOf(1L, 2L))
        assertThat(result).isEmpty()

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockSneakerRepository).findByIdInAndAccount(any<List<Long>>(), any<Account>())
    }

    @Test
    fun `findAll list of sneakers, success with data`() {
        val mockAccount = mock<Account>()
        val mockSneaker = mock<Sneaker>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockSneakerRepository.findByIdInAndAccount(any<List<Long>>(), any<Account>())).thenReturn(
            listOf(mockSneaker, mockSneaker, mockSneaker)
        )

        // -- execute --
        val result = sneakerService.findAll(1L, listOf(1L, 2L))
        assertThat(result).isNotEmpty.hasSize(3)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockSneakerRepository).findByIdInAndAccount(any<List<Long>>(), any<Account>())
    }

    @Test
    fun `put, exception thrown because there is a null value in request`() {
        var request = SneakerRequestNullable(
            brand = null,
            color = null
        )

        // -- execute with label null --
        assertThrows<IllegalArgumentException> { sneakerService.put(1L, 2L, request) }

        request = SneakerRequestNullable(
            brand = "Brand",
            color = null
        )
        // -- execute with line null --
        assertThrows<IllegalArgumentException> { sneakerService.put(1L, 2L, request) }

        // -- verify --
        verify(mockSneakerRepository, never()).findByIdAndAccount(any<Long>(), any<Account>())
        verify(mockSneakerRepository, never()).save(any<Sneaker>())
    }

    @Test
    fun `put, success`() {
        val mockAccount = mock<Account>()
        val mockSneaker = Sneaker(mockAccount, "Brand New", "Black")
        val request = SneakerRequestNullable(
            brand = "Brand",
            color = "Color",
        )
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockSneakerRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(mockSneaker)
        whenever(mockSneakerRepository.save(any<Sneaker>())).thenReturn(mockSneaker)

        // -- execute --
        val result = sneakerService.put(1L, 2L, request)
        assertThat(result.javaClass).isEqualTo(Sneaker::class.java)

        // -- captor --
        val captor = argumentCaptor<Sneaker>()
        verify(mockSneakerRepository).save(captor.capture())
        val captured = captor.firstValue

        assertThat(captured.brand).isEqualTo(mockSneaker.brand)
        assertThat(captured.color).isEqualTo(mockSneaker.color)
        // -- verify --
        verify(mockSneakerRepository).findByIdAndAccount(any<Long>(), any<Account>())
    }

    @Test
    fun `patch, success`() {
        val brand = "Erspo"
        val color = "Blue"
        val mockAccount = mock<Account>()
        val mockSneaker = Sneaker(mockAccount, "Brand New", "Black")
        val jsonString = """
            {
            "brand": "$brand",
            "color": "$color"
            }
        """.trimIndent()
        val mapper = ObjectMapper()
        val request = mapper.readTree(jsonString)
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockSneakerRepository.findByIdAndAccount(any<Long>(), any<Account>())).thenReturn(mockSneaker)
        whenever(mockSneakerRepository.save(any<Sneaker>())).thenReturn(mockSneaker)

        // -- execute --
        val result = sneakerService.patch(1L, 2L, request)
        assertThat(result.javaClass).isEqualTo(Sneaker::class.java)

        // -- captor --
        val captor = argumentCaptor<Sneaker>()
        verify(mockSneakerRepository).save(captor.capture())
        val captured = captor.firstValue

        assertThat(captured.brand).isEqualTo(brand)
        assertThat(captured.color).isEqualTo(color)
        // -- verify --
        verify(mockSneakerRepository, atLeastOnce()).findByIdAndAccount(any<Long>(), any<Account>())
    }
}
