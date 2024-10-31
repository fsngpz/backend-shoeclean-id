package id.shoeclean.engine.transaction

import id.shoeclean.engine.orders.Order
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.OffsetDateTime

/**
 * The test class of [TransactionService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
@SpringBootTest(classes = [TransactionService::class])
class TransactionServiceTest(@Autowired private val transactionService: TransactionService) {

    @MockBean
    private lateinit var mockTransactionRepository: TransactionRepository

    @Test
    fun `dependencies are not null`() {
        assertThat(transactionService).isNotNull
        assertThat(mockTransactionRepository).isNotNull
    }

    @Test
    fun `create succes`() {
        val mockOrder = mock<Order>()
        val request = EventNewTransactionRequest(
            mockOrder,
            BigDecimal(10_000_000),
            BigDecimal.ZERO,
            BigDecimal(10_000_000),
            TransactionMethod.BANK_TRANSFER
        )
        val mockTransaction = mock<Transaction>()
        // -- mock --
        whenever(mockTransactionRepository.save(any<Transaction>())).thenReturn(mockTransaction)

        // -- execute --
        val result = transactionService.create(request)
        assertThat(result.javaClass).isEqualTo(Transaction::class.java)

        // -- verify --
        val captor = argumentCaptor<Transaction>()
        verify(mockTransactionRepository).save(captor.capture())
        val captured = captor.firstValue
        assertThat(captured).isNotNull
    }

    @Test
    fun `findAll, return empty`() {
        // -- mock --
        whenever(
            mockTransactionRepository.findAll(
                any<Long>(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                any<Pageable>()
            )
        ).thenReturn(Page.empty())

        // -- execute --
        val result = transactionService.findAll(1L, null, null, null, Pageable.unpaged())
        assertThat(result).isEmpty()

        // -- verify --
        val captorCreatedAtFrom = argumentCaptor<OffsetDateTime>()
        val captorCreatedAtTo = argumentCaptor<OffsetDateTime>()
        verify(mockTransactionRepository).findAll(
            any<Long>(),
            anyOrNull(),
            captorCreatedAtFrom.capture(),
            captorCreatedAtTo.capture(),
            any<Pageable>()
        )
        val capturedCreatedAtFrom = captorCreatedAtFrom.firstValue
        val capturedCreatedAtTo = captorCreatedAtTo.firstValue
        assertThat(capturedCreatedAtFrom).isNotNull
        assertThat(capturedCreatedAtTo).isNotNull
    }

    @Test
    fun `findAll, success with data`() {
        val mockTransactionOne = mock<Transaction>()
        val mockTransactionTwo = mock<Transaction>()
        val mockTransactionThree = mock<Transaction>()
        // -- mock --
        whenever(
            mockTransactionRepository.findAll(
                any<Long>(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                any<Pageable>()
            )
        ).thenReturn(PageImpl(listOf(mockTransactionOne, mockTransactionTwo, mockTransactionThree)))

        // -- execute --
        val result = transactionService.findAll(1L, null, null, null, Pageable.unpaged())
        assertThat(result).isNotEmpty.hasSize(3)

        // -- verify --
        val captorCreatedAtFrom = argumentCaptor<OffsetDateTime>()
        val captorCreatedAtTo = argumentCaptor<OffsetDateTime>()
        verify(mockTransactionRepository).findAll(
            any<Long>(),
            anyOrNull(),
            captorCreatedAtFrom.capture(),
            captorCreatedAtTo.capture(),
            any<Pageable>()
        )
        val capturedCreatedAtFrom = captorCreatedAtFrom.firstValue
        val capturedCreatedAtTo = captorCreatedAtTo.firstValue
        assertThat(capturedCreatedAtFrom).isNotNull
        assertThat(capturedCreatedAtTo).isNotNull
    }
}
