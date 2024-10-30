package id.shoeclean.engine.orders

import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.addresses.Address
import id.shoeclean.engine.addresses.AddressService
import id.shoeclean.engine.catalogs.Catalog
import id.shoeclean.engine.catalogs.CatalogService
import id.shoeclean.engine.catalogs.ServiceType
import id.shoeclean.engine.exceptions.OrderNotFoundException
import id.shoeclean.engine.exceptions.VoucherNotSufficeOrderQtyException
import id.shoeclean.engine.exceptions.VoucherNotSufficeOrderSubtotalException
import id.shoeclean.engine.vouchers.AmountType
import id.shoeclean.engine.vouchers.Voucher
import id.shoeclean.engine.vouchers.VoucherType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.math.BigDecimal
import java.time.OffsetDateTime
import kotlin.random.Random

/**
 * The test class for [OrderService].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-30
 */
@SpringBootTest(classes = [OrderService::class])
class OrderServiceTest(@Autowired private val orderService: OrderService) {
    @MockBean
    private lateinit var mockAccountService: AccountService

    @MockBean
    private lateinit var mockAddressService: AddressService

    @MockBean
    private lateinit var mockCatalogService: CatalogService

    @MockBean
    private lateinit var mockOrderSneakerService: OrderSneakerService

    @MockBean
    private lateinit var mockOrderRepository: OrderRepository

    @Test
    fun `dependencies are not null`() {
        assertThat(orderService).isNotNull
        assertThat(mockAccountService).isNotNull
        assertThat(mockAddressService).isNotNull
        assertThat(mockCatalogService).isNotNull
        assertThat(mockOrderSneakerService).isNotNull
        assertThat(mockOrderRepository).isNotNull
    }

    @Test
    fun `create, success`() {
        val mockAccount = mock<Account>()
        val mockAddress = mock<Address>()
        val mockCatalog = mock<Catalog>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressService.get(any<Long>(), any<Long>())).thenReturn(mockAddress)
        whenever(mockCatalogService.getByServiceType(any<ServiceType>())).thenReturn(mockCatalog)
        whenever(mockOrderRepository.save(any<Order>())).thenAnswer { invocation ->
            val order = invocation.getArgument<Order>(0)
            order.uscId = "USCID-1021213"
            order
        }

        // -- execute --
        assertAll({ orderService.create(1L, 1L, listOf(1L, 2L), ServiceType.REPAIR, 2) })

        // -- verify --
        verify(mockOrderSneakerService).createBulk(any<Long>(), any<Order>(), any<List<Long>>())

        // -- captor --
        val captor = argumentCaptor<Order>()
        verify(mockOrderRepository).save(captor.capture())
        val captured = captor.firstValue
        assertThat(captured.status).isEqualTo(OrderStatus.PENDING)
    }

    @Test
    fun `create, order id is null`() {
        val mockAccount = mock<Account>()
        val mockAddress = mock<Address>()
        val mockCatalog = mock<Catalog>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockAddressService.get(any<Long>(), any<Long>())).thenReturn(mockAddress)
        whenever(mockCatalogService.getByServiceType(any<ServiceType>())).thenReturn(mockCatalog)
        whenever(mockOrderRepository.save(any<Order>())).thenAnswer { invocation ->
            val order = invocation.getArgument<Order>(0)
            order.uscId = null
            order
        }

        // -- execute --
        assertThrows<IllegalArgumentException> {
            orderService.create(
                1L,
                1L,
                listOf(1L, 2L),
                ServiceType.REPAIR,
                2
            )
        }

        // -- verify --
        verify(mockOrderSneakerService).createBulk(any<Long>(), any<Order>(), any<List<Long>>())

        // -- captor --
        val captor = argumentCaptor<Order>()
        verify(mockOrderRepository).save(captor.capture())
        val captured = captor.firstValue
        assertThat(captured.status).isEqualTo(OrderStatus.PENDING)
    }

    @Test
    fun `get, not found`() {
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByOrderIdAndAccount(any<String>(), any<Account>())).thenReturn(null)

        // -- execute --
        assertThrows<OrderNotFoundException> { orderService.get(1L, "USC111") }

        // -- verify --
        verify(mockAccountService).get(any<Long>())
    }

    @Test
    fun `get, success`() {
        val mockOrder = mock<Order>()
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByOrderIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        val result = orderService.get(1L, "USC111")
        assertThat(result.javaClass).isEqualTo(Order::class.java)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
    }

    @Test
    fun `getDetails, order id null`() {
        val mockOrder = mock<Order>()
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByOrderIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        assertThrows<IllegalArgumentException> { orderService.getDetails(1L, "USC111") }

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByOrderIdAndAccount(any<String>(), any<Account>())
    }

    /**
     * This test will throw an exception because the total pairs of order is 2 while the voucher amount is 3 with
     * type [VoucherType.FREE_PAIR].
     *
     */
    @Test
    fun `getDetails, total pairs not suffice the voucher amount`() {
        val mockAccount = mock<Account>()
        val mockAddress = mock<Address>()
        val mockCatalog = Catalog(ServiceType.REPAIR, "Test", BigDecimal("15000"))
        val mockVoucher = Voucher(
            "TEST",
            VoucherType.FREE_PAIR,
            AmountType.AMOUNT,
            BigDecimal(3),
            OffsetDateTime.now()
        )

        val mockOrder = Order(
            mockAccount,
            mockAddress,
            mockCatalog,
            OrderStatus.PENDING,
            2
        ).apply {
            this.uscId = "USC111"
            this.voucher = mockVoucher
        }
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByOrderIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        assertThrows<VoucherNotSufficeOrderQtyException> { orderService.getDetails(1L, "USC111") }

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByOrderIdAndAccount(any<String>(), any<Account>())
    }

    /**
     * This test will throw an exception because the subtotal of order is 30_000 while the voucher amount is 50_000 with
     * type [VoucherType.DISCOUNT].
     *
     */
    @Test
    fun `getDetails, subtotal not suffice the voucher amount`() {
        val mockAccount = mock<Account>()
        val mockAddress = mock<Address>()
        val mockCatalog = Catalog(ServiceType.REPAIR, "Test", BigDecimal("15000"))
        val mockVoucher = Voucher(
            "TEST",
            VoucherType.DISCOUNT,
            AmountType.AMOUNT,
            BigDecimal(50_000),
            OffsetDateTime.now()
        )

        val mockOrder = Order(
            mockAccount,
            mockAddress,
            mockCatalog,
            OrderStatus.PENDING,
            2
        ).apply {
            this.uscId = "USC111"
            this.voucher = mockVoucher
        }
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByOrderIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        assertThrows<VoucherNotSufficeOrderSubtotalException> { orderService.getDetails(1L, "USC111") }

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByOrderIdAndAccount(any<String>(), any<Account>())
    }

    @Test
    fun `getDetails, with free pair voucher`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
        val mockCatalog = Catalog(ServiceType.DEEP_CLEANING, "Test", BigDecimal("15000"))
        val mockVoucher = Voucher(
            "TEST",
            VoucherType.FREE_PAIR,
            AmountType.AMOUNT,
            BigDecimal(1),
            OffsetDateTime.now()
        )

        val mockOrder = Order(
            mockAccount,
            mockAddress,
            mockCatalog,
            OrderStatus.PENDING,
            2
        ).apply {
            this.uscId = "USC111"
            this.voucher = mockVoucher
        }

        val expectedPrice = mockCatalog.price
        val expectedDiscount = mockCatalog.price.multiply(mockVoucher.amount)
        val expectedSubtotal = mockCatalog.price.multiply(BigDecimal(mockOrder.totalPairs))
        val expectedTotalAmount = expectedSubtotal.subtract(expectedDiscount)
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByOrderIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        val result = orderService.getDetails(1L, "USC111")
        assertThat(result.orderId).isEqualTo(mockOrder.uscId)
        assertThat(result.totalPairs).isEqualTo(mockOrder.totalPairs)
        assertThat(result.serviceType).isEqualTo(mockCatalog.serviceType)
        assertThat(result.price).isEqualTo(expectedPrice)
        assertThat(result.discount).isEqualTo(expectedDiscount)
        assertThat(result.subtotal).isEqualTo(expectedSubtotal)
        assertThat(result.totalAmount).isEqualTo(expectedTotalAmount)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByOrderIdAndAccount(any<String>(), any<Account>())
    }

    @Test
    fun `getDetails, with discount voucher`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
        val mockCatalog = Catalog(ServiceType.DEEP_CLEANING, "Test", BigDecimal("15000"))
        val mockVoucher = Voucher(
            "TEST",
            VoucherType.DISCOUNT,
            AmountType.AMOUNT,
            BigDecimal(10_000),
            OffsetDateTime.now()
        )

        val mockOrder = Order(
            mockAccount,
            mockAddress,
            mockCatalog,
            OrderStatus.PENDING,
            2
        ).apply {
            this.uscId = "USC111"
            this.voucher = mockVoucher
        }

        val expectedPrice = mockCatalog.price
        val expectedDiscount = mockVoucher.amount
        val expectedSubtotal = mockCatalog.price.multiply(BigDecimal(mockOrder.totalPairs))
        val expectedTotalAmount = expectedSubtotal.subtract(expectedDiscount)
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByOrderIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        val result = orderService.getDetails(1L, "USC111")
        assertThat(result.orderId).isEqualTo(mockOrder.uscId)
        assertThat(result.totalPairs).isEqualTo(mockOrder.totalPairs)
        assertThat(result.serviceType).isEqualTo(mockCatalog.serviceType)
        assertThat(result.price).isEqualTo(expectedPrice)
        assertThat(result.discount).isEqualTo(expectedDiscount)
        assertThat(result.subtotal).isEqualTo(expectedSubtotal)
        assertThat(result.totalAmount).isEqualTo(expectedTotalAmount)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByOrderIdAndAccount(any<String>(), any<Account>())
    }

    @Test
    fun `getDetails, without voucher`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
        val mockCatalog = Catalog(ServiceType.DEEP_CLEANING, "Test", BigDecimal("15000"))

        val mockOrder = Order(
            mockAccount,
            mockAddress,
            mockCatalog,
            OrderStatus.PENDING,
            2
        ).apply {
            this.uscId = "USC111"
        }

        val expectedPrice = mockCatalog.price
        val expectedDiscount = BigDecimal.ZERO
        val expectedSubtotal = mockCatalog.price.multiply(BigDecimal(mockOrder.totalPairs))
        val expectedTotalAmount = expectedSubtotal.subtract(expectedDiscount)
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByOrderIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        val result = orderService.getDetails(1L, "USC111")
        assertThat(result.orderId).isEqualTo(mockOrder.uscId)
        assertThat(result.totalPairs).isEqualTo(mockOrder.totalPairs)
        assertThat(result.serviceType).isEqualTo(mockCatalog.serviceType)
        assertThat(result.price).isEqualTo(expectedPrice)
        assertThat(result.discount).isEqualTo(expectedDiscount)
        assertThat(result.subtotal).isEqualTo(expectedSubtotal)
        assertThat(result.totalAmount).isEqualTo(expectedTotalAmount)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByOrderIdAndAccount(any<String>(), any<Account>())
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
