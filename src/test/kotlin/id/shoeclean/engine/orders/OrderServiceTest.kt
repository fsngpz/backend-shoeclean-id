package id.shoeclean.engine.orders

import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.addresses.Address
import id.shoeclean.engine.addresses.AddressService
import id.shoeclean.engine.catalogs.Catalog
import id.shoeclean.engine.catalogs.CatalogService
import id.shoeclean.engine.catalogs.ServiceType
import id.shoeclean.engine.exceptions.OrderNotFoundException
import id.shoeclean.engine.transaction.EventNewTransactionRequest
import id.shoeclean.engine.transaction.TransactionMethod
import id.shoeclean.engine.vouchers.AmountType
import id.shoeclean.engine.vouchers.Voucher
import id.shoeclean.engine.vouchers.VoucherService
import id.shoeclean.engine.vouchers.VoucherType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
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
    private lateinit var mockVoucherService: VoucherService

    @MockBean
    private lateinit var mockOrderRepository: OrderRepository

    @MockBean
    private lateinit var mockOrderEventPublisher: OrderEventPublisher

    @Test
    fun `dependencies are not null`() {
        assertThat(orderService).isNotNull
        assertThat(mockAccountService).isNotNull
        assertThat(mockAddressService).isNotNull
        assertThat(mockCatalogService).isNotNull
        assertThat(mockOrderSneakerService).isNotNull
        assertThat(mockOrderRepository).isNotNull
        assertThat(mockOrderEventPublisher).isNotNull
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
        assertThat(captured.status).isEqualTo(OrderStatus.PENDING_CONFIRMATION)
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
        assertThat(captured.status).isEqualTo(OrderStatus.PENDING_CONFIRMATION)
    }

    @Test
    fun `get, not found`() {
        val mockAccount = mock<Account>()
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(null)

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
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

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
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        assertThrows<IllegalArgumentException> { orderService.getDetails(1L, "USC111") }

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByUscIdAndAccount(any<String>(), any<Account>())
    }

    /**
     * This test will return zero of the total amount because the total pairs of order is 2 while the voucher amount
     * is 3 with type [VoucherType.FREE_PAIR].
     *
     */
    @Test
    fun `getDetails, total pairs not suffice the voucher amount`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
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
            OrderStatus.PENDING_CONFIRMATION,
            2
        ).apply {
            this.uscId = "USC111"
            this.voucher = mockVoucher
        }
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        val result = orderService.getDetails(1L, "USC111")
        assertThat(result.totalAmount).isZero

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByUscIdAndAccount(any<String>(), any<Account>())
    }

    /**
     * This test will return the total amount to 30_000 because the subtotal of order is 30_000 while the voucher a
     * mount is 50_000 with type [VoucherType.DISCOUNT].
     *
     */
    @Test
    fun `getDetails, voucher amount greater than subtotal`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
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
            OrderStatus.PENDING_CONFIRMATION,
            2
        ).apply {
            this.uscId = "USC111"
            this.voucher = mockVoucher
        }
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- set the expected discount which is TotalPairs * Price --
        val expectedDiscount = BigDecimal(mockOrder.totalPairs).multiply(mockCatalog.price)
        // -- execute --
        val result = orderService.getDetails(1L, "USC111")
        assertThat(result.discount).isEqualTo(expectedDiscount)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByUscIdAndAccount(any<String>(), any<Account>())
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
            OrderStatus.PENDING_CONFIRMATION,
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
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        val result = orderService.getDetails(1L, "USC111")
        assertThat(result.uscId).isEqualTo(mockOrder.uscId)
        assertThat(result.totalPairs).isEqualTo(mockOrder.totalPairs)
        assertThat(result.serviceType).isEqualTo(mockCatalog.serviceType)
        assertThat(result.price).isEqualTo(expectedPrice)
        assertThat(result.discount).isEqualTo(expectedDiscount)
        assertThat(result.subtotal).isEqualTo(expectedSubtotal)
        assertThat(result.totalAmount).isEqualTo(expectedTotalAmount)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByUscIdAndAccount(any<String>(), any<Account>())
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
            OrderStatus.PENDING_CONFIRMATION,
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
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        val result = orderService.getDetails(1L, "USC111")
        assertThat(result.uscId).isEqualTo(mockOrder.uscId)
        assertThat(result.totalPairs).isEqualTo(mockOrder.totalPairs)
        assertThat(result.serviceType).isEqualTo(mockCatalog.serviceType)
        assertThat(result.price).isEqualTo(expectedPrice)
        assertThat(result.discount).isEqualTo(expectedDiscount)
        assertThat(result.subtotal).isEqualTo(expectedSubtotal)
        assertThat(result.totalAmount).isEqualTo(expectedTotalAmount)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByUscIdAndAccount(any<String>(), any<Account>())
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
            OrderStatus.PENDING_CONFIRMATION,
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
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        val result = orderService.getDetails(1L, "USC111")
        assertThat(result.uscId).isEqualTo(mockOrder.uscId)
        assertThat(result.totalPairs).isEqualTo(mockOrder.totalPairs)
        assertThat(result.serviceType).isEqualTo(mockCatalog.serviceType)
        assertThat(result.price).isEqualTo(expectedPrice)
        assertThat(result.discount).isEqualTo(expectedDiscount)
        assertThat(result.subtotal).isEqualTo(expectedSubtotal)
        assertThat(result.totalAmount).isEqualTo(expectedTotalAmount)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByUscIdAndAccount(any<String>(), any<Account>())
    }

    /**
     * This test will return total amount ZERO because the total pairs of order is 2 while the voucher amount is 3 with
     * type [VoucherType.FREE_PAIR].
     *
     */
    @Test
    fun `applyVoucher, voucher free pairs greater than total pairs`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
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
            OrderStatus.PENDING_CONFIRMATION,
            2
        ).apply {
            this.uscId = "USC111"
        }
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)
        whenever(mockVoucherService.get(any<String>())).thenReturn(mockVoucher)

        // -- execute --
        val result = orderService.applyVoucher(
            1L,
            "USC111",
            "USCXUnitTest"
        )
        assertThat(result.totalAmount).isZero

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByUscIdAndAccount(any<String>(), any<Account>())
    }

    /**
     * This test will return the discount 30_000 because the subtotal of order is 30_000 while the voucher amount
     * is 50_000 with type [VoucherType.DISCOUNT].
     *
     */
    @Test
    fun `applyVoucher, voucher amount greater than subtotal`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
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
            OrderStatus.PENDING_CONFIRMATION,
            2
        ).apply {
            this.uscId = "USC111"
        }
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)
        whenever(mockVoucherService.get(any<String>())).thenReturn(mockVoucher)

        val expectedDiscount = BigDecimal(mockOrder.totalPairs).multiply(mockCatalog.price)
        // -- execute --
        val result = orderService.applyVoucher(
            1L,
            "USC111",
            "USCXUnitTest"
        )
        assertThat(result.discount).isEqualTo(expectedDiscount)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByUscIdAndAccount(any<String>(), any<Account>())
    }

    @Test
    fun `applyVoucher, with free pair voucher`() {
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
            OrderStatus.PENDING_CONFIRMATION,
            2
        ).apply {
            this.uscId = "USC111"
        }

        val expectedPrice = mockCatalog.price
        val expectedDiscount = mockCatalog.price.multiply(mockVoucher.amount)
        val expectedSubtotal = mockCatalog.price.multiply(BigDecimal(mockOrder.totalPairs))
        val expectedTotalAmount = expectedSubtotal.subtract(expectedDiscount)
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)
        whenever(mockVoucherService.get(any<String>())).thenReturn(mockVoucher)

        // -- execute --
        val result = orderService.applyVoucher(
            1L,
            "USC111",
            "USCXUnitTest"
        )
        assertThat(result.uscId).isEqualTo(mockOrder.uscId)
        assertThat(result.totalPairs).isEqualTo(mockOrder.totalPairs)
        assertThat(result.serviceType).isEqualTo(mockCatalog.serviceType)
        assertThat(result.price).isEqualTo(expectedPrice)
        assertThat(result.discount).isEqualTo(expectedDiscount)
        assertThat(result.subtotal).isEqualTo(expectedSubtotal)
        assertThat(result.totalAmount).isEqualTo(expectedTotalAmount)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByUscIdAndAccount(any<String>(), any<Account>())
    }

    @Test
    fun `applyVoucher, with discount voucher`() {
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
            OrderStatus.PENDING_CONFIRMATION,
            2
        ).apply {
            this.uscId = "USC111"
        }

        val expectedPrice = mockCatalog.price
        val expectedDiscount = mockVoucher.amount
        val expectedSubtotal = mockCatalog.price.multiply(BigDecimal(mockOrder.totalPairs))
        val expectedTotalAmount = expectedSubtotal.subtract(expectedDiscount)
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)
        whenever(mockVoucherService.get(any<String>())).thenReturn(mockVoucher)

        // -- execute --
        val result = orderService.applyVoucher(
            1L,
            "USC111",
            "USCXUnitTest"
        )
        assertThat(result.uscId).isEqualTo(mockOrder.uscId)
        assertThat(result.totalPairs).isEqualTo(mockOrder.totalPairs)
        assertThat(result.serviceType).isEqualTo(mockCatalog.serviceType)
        assertThat(result.price).isEqualTo(expectedPrice)
        assertThat(result.discount).isEqualTo(expectedDiscount)
        assertThat(result.subtotal).isEqualTo(expectedSubtotal)
        assertThat(result.totalAmount).isEqualTo(expectedTotalAmount)

        // -- verify --
        verify(mockAccountService).get(any<Long>())
        verify(mockOrderRepository).findByUscIdAndAccount(any<String>(), any<Account>())
    }

    @Test
    fun `confirm, success WITHOUT voucher code`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
        val mockCatalog = Catalog(ServiceType.DEEP_CLEANING, "Test", BigDecimal("15000"))
        val mockOrder = Order(mockAccount, mockAddress, mockCatalog, OrderStatus.PENDING_CONFIRMATION, 1).apply {
            this.id = 1
            this.uscId = "USC111"
        }
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        assertAll({
            orderService.confirm(
                1L,
                "USCID11",
                TransactionMethod.CASH_ON_DELIVERY,
                null
            )
        })

        // -- verify --
        verify(mockVoucherService, never()).get(any<String>())
        verify(mockOrderRepository).save(any<Order>())
        verify(mockOrderEventPublisher).publish(any<EventNewTransactionRequest>())
    }

    @Test
    fun `confirm, success WITH voucher code`() {
        val mockAccount = mock<Account>()
        val mockAddress = createMockAddress()
        val mockCatalog = Catalog(ServiceType.DEEP_CLEANING, "Test", BigDecimal("15000"))
        val mockOrder = Order(mockAccount, mockAddress, mockCatalog, OrderStatus.PENDING_CONFIRMATION, 2).apply {
            this.id = 1
            this.uscId = "USC111"
        }
        val mockVoucher = Voucher(
            "TEST",
            VoucherType.FREE_PAIR,
            AmountType.AMOUNT,
            BigDecimal(1),
            OffsetDateTime.now()
        )
        // -- mock --
        whenever(mockAccountService.get(any<Long>())).thenReturn(mockAccount)
        whenever(mockVoucherService.get(any<String>())).thenReturn(mockVoucher)
        whenever(mockOrderRepository.findByUscIdAndAccount(any<String>(), any<Account>())).thenReturn(mockOrder)

        // -- execute --
        assertAll({
            orderService.confirm(
                1L,
                "USCID11",
                TransactionMethod.CASH_ON_DELIVERY,
                "VOUCHX1"
            )
        })

        // -- verify --
        verify(mockVoucherService).get(any<String>())
        verify(mockOrderRepository).save(any<Order>())
        verify(mockOrderEventPublisher).publish(any<EventNewTransactionRequest>())
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

