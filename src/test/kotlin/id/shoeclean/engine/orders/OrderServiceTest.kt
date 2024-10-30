package id.shoeclean.engine.orders

import id.shoeclean.engine.accounts.Account
import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.addresses.Address
import id.shoeclean.engine.addresses.AddressService
import id.shoeclean.engine.catalogs.Catalog
import id.shoeclean.engine.catalogs.CatalogService
import id.shoeclean.engine.catalogs.ServiceType
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
            order.orderId = "USCID-1021213"
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
            order.orderId = null
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
}
