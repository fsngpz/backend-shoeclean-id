package id.shoeclean.engine.transaction

import id.shoeclean.engine.orders.Order
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.math.BigDecimal

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
}
