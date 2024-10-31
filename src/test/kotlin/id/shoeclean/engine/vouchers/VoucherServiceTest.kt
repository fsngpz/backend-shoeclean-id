package id.shoeclean.engine.vouchers

import id.shoeclean.engine.exceptions.VoucherExpiredException
import id.shoeclean.engine.exceptions.VoucherNotFoundException
import id.shoeclean.engine.exceptions.VoucherQuotaExceededException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Optional

/**
 * The test class for [VoucherService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-31
 */
@SpringBootTest(classes = [VoucherService::class])
internal class VoucherServiceTest(@Autowired private val service: VoucherService) {
    @MockBean
    private lateinit var mockVoucherRepository: VoucherRepository

    @Test
    fun `dependencies are not null`() {
        assertThat(service).isNotNull
        assertThat(mockVoucherRepository).isNotNull
    }

    @Test
    fun `get, not found`() {
        // -- mock --
        whenever(mockVoucherRepository.findById(any<Long>())).thenReturn(Optional.empty())

        // -- execute and verify --
        assertThrows<VoucherNotFoundException> { service.get(1L) }
    }

    @Test
    fun `get, success`() {
        val mockVoucher = mock<Voucher>()
        // -- mock --
        whenever(mockVoucherRepository.findById(any<Long>())).thenReturn(Optional.of(mockVoucher))

        // -- execute and verify --
        val result = service.get(1L)
        assertThat(result.javaClass).isEqualTo(Voucher::class.java)
    }

    @Test
    fun `get using code, not found`() {
        // -- mock --
        whenever(mockVoucherRepository.findByCode(any<String>())).thenReturn(null)

        // -- execute and verify --
        assertThrows<VoucherNotFoundException> { service.get("CODE111") }
    }

    @Test
    fun `get using code, quota exceeded or empty`() {
        val mockVoucher = Voucher(
            "CODE111",
            VoucherType.DISCOUNT,
            AmountType.AMOUNT,
            BigDecimal.ONE,
            OffsetDateTime.now()
        ).apply {
            this.quantity = 0
        }
        // -- mock --
        whenever(mockVoucherRepository.findByCode(any<String>())).thenReturn(mockVoucher)

        // -- execute and verify --
        assertThrows<VoucherQuotaExceededException> { service.get("CODE111") }
    }

    @Test
    fun `get using code, voucher expired`() {
        val mockVoucher = Voucher(
            "CODE111",
            VoucherType.DISCOUNT,
            AmountType.AMOUNT,
            BigDecimal.ONE,
            OffsetDateTime.now().minusDays(1)
        ).apply {
            this.quantity = 10
        }
        // -- mock --
        whenever(mockVoucherRepository.findByCode(any<String>())).thenReturn(mockVoucher)

        // -- execute and verify --
        assertThrows<VoucherExpiredException> { service.get("CODE111") }
    }

    @Test
    fun `get using code, success`() {
        val mockVoucher = Voucher(
            "CODE111",
            VoucherType.DISCOUNT,
            AmountType.AMOUNT,
            BigDecimal.ONE,
            OffsetDateTime.now().plusDays(1)
        ).apply {
            this.quantity = 10
        }
        // -- mock --
        whenever(mockVoucherRepository.findByCode(any<String>())).thenReturn(mockVoucher)

        // -- execute and verify --
        val result = service.get("CODE111")
        assertThat(result).isSameAs(mockVoucher)
    }
}
