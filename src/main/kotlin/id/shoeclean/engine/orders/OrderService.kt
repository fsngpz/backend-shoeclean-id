package id.shoeclean.engine.orders

import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.addresses.AddressService
import id.shoeclean.engine.catalogs.CatalogService
import id.shoeclean.engine.catalogs.ServiceType
import id.shoeclean.engine.exceptions.OrderNotFoundException
import org.springframework.stereotype.Service

/**
 * The service class to handle feature related to [Order].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@Service
class OrderService(
    private val accountService: AccountService,
    private val addressService: AddressService,
    private val catalogService: CatalogService,
    private val orderSneakerService: OrderSneakerService,
    private val orderRepository: OrderRepository
) {
    /**
     * a function to find the [Order] of a specific account and specific order unique identifier.
     *
     * @param accountId the account unique identifier.
     * @param orderId the order unique identifier.
     * @return the [Order] instance.
     */
    fun get(accountId: Long, orderId: String): Order {
        val account = accountService.get(accountId)
        // -- find the sneaker or else throw an exception --
        return orderRepository.findByOrderIdAndAccount(orderId, account)
            ?: throw OrderNotFoundException("you dont have order with id '$orderId'")
    }

    /**
     * a method to handle request create new [Order].
     *
     * @param accountId the account unique identifier.
     * @param addressId the address unique identifier.
     * @param sneakerIds the list of sneaker unique identifier.
     * @param serviceType the [ServiceType].
     * @param totalPairs the total pairs of sneaker.
     * @return the [SubmitOrderResponse].
     */
    fun create(
        accountId: Long,
        addressId: Long,
        sneakerIds: List<Long>,
        serviceType: ServiceType,
        totalPairs: Int
    ): SubmitOrderResponse {
        // -- get the account using given id --
        val account = accountService.get(accountId)
        // -- get the address using given id --
        val address = addressService.get(accountId, addressId)
        // -- get the catalog by service type --
        val catalog = catalogService.getByServiceType(serviceType)
        // -- setup new Order instance --
        val order = Order(account, address, catalog, OrderStatus.PENDING, totalPairs)
        // -- save the instance order --
        orderRepository.save(order)
        // -- create the OrderSneaker --
        orderSneakerService.createBulk(accountId, order, sneakerIds)
        // -- map then return the instance --
        return order.toSubmitOrderResponse()
    }

    /**
     * a function to retrieve the [OrderDetailResponse].
     *
     * @param accountId the account unique identifier.
     * @param orderId the order unique identifier.
     * @return the [OrderDetailResponse].
     */
    fun getDetails(accountId: Long, orderId: String): OrderDetailResponse {
        val order = get(accountId, orderId)
        // -- map and return the details of Order --
        return order.toOrderDetailResponse()
    }
}
