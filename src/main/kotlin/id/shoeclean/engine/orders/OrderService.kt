package id.shoeclean.engine.orders

import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.addresses.AddressService
import id.shoeclean.engine.catalogs.CatalogService
import id.shoeclean.engine.catalogs.ServiceType
import id.shoeclean.engine.sneakers.Sneaker
import id.shoeclean.engine.sneakers.SneakerService
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
    private val sneakerService: SneakerService,
    private val catalogService: CatalogService,
    private val orderRepository: OrderRepository,
    private val orderSneakerRepository: OrderSneakerRepository
) {

    /**
     * a method to handle request create new [Order].
     *
     * @param accountId the account unique identifier.
     * @param addressId the address unique identifier.
     * @param sneakerIds the list of sneaker unique identifier.
     * @param serviceType the [ServiceType].
     * @param totalPairs the total pairs of sneaker.
     */
    fun create(accountId: Long, addressId: Long, sneakerIds: List<Long>, serviceType: ServiceType, totalPairs: Int) {
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
        createBulk(accountId, order, sneakerIds)
    }

    /**
     * a function to handle create the new [OrderSneaker] in bulk. This function will save one [Order]
     * with multiple [Sneaker].
     *
     * @param accountId the account unique identifier.
     * @param order the [Order] instance.
     * @param sneakerIds the list sneaker unique identifier.
     */
    fun createBulk(accountId: Long, order: Order, sneakerIds: List<Long>) {
        // -- find the sneakers by given ids --
        val sneakers = sneakerService.findAll(accountId, sneakerIds)
        // -- setup list of OrderSneaker --
        val orderSneakers = mutableListOf<OrderSneaker>()
        // -- iterate the sneakers to setup the OrderSneaker instance --
        sneakers.forEach {
            val orderSneaker = OrderSneaker(it, order)
            // -- add the orderSneaker to the list --
            orderSneakers.add(orderSneaker)
        }
        // -- save the orderSneakers --
        orderSneakerRepository.saveAll(orderSneakers)
    }
}
