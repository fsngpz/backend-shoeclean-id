package id.shoeclean.engine.orders

import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.addresses.AddressService
import id.shoeclean.engine.catalogs.CatalogService
import id.shoeclean.engine.catalogs.ServiceType
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

    fun create(accountId: Long, addressId: Long, sneakerId: Long, serviceType: ServiceType, totalPairs: Int) {
        // -- get the account using given id --
        val account = accountService.get(accountId)
        // -- get the address using given id --
        val address = addressService.get(accountId, addressId)
        // -- get the sneaker using given id --
        val sneaker = sneakerService.get(accountId, sneakerId)
        // -- get the catalog by service type --
        val catalog = catalogService.getByServiceType(serviceType)
        // -- setup new Order instance --
        val order = Order(account, address, catalog, OrderStatus.PENDING, totalPairs)
    }
}
