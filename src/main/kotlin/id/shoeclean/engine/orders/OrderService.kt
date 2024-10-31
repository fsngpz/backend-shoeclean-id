package id.shoeclean.engine.orders

import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.addresses.AddressService
import id.shoeclean.engine.catalogs.CatalogService
import id.shoeclean.engine.catalogs.ServiceType
import id.shoeclean.engine.exceptions.OrderNotFoundException
import id.shoeclean.engine.vouchers.Voucher
import id.shoeclean.engine.vouchers.VoucherService
import jakarta.transaction.Transactional
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
    private val voucherService: VoucherService,
    private val orderSneakerService: OrderSneakerService,
    private val orderRepository: OrderRepository
) {
    /**
     * a function to find the [Order] of a specific account and specific order unique identifier.
     *
     * @param accountId the account unique identifier.
     * @param uscId the urban sneaker care order unique identifier.
     * @return the [Order] instance.
     */
    fun get(accountId: Long, uscId: String): Order {
        val account = accountService.get(accountId)
        // -- find the sneaker or else throw an exception --
        return orderRepository.findByUscIdAndAccount(uscId, account)
            ?: throw OrderNotFoundException("you dont have order with id '$uscId'")
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
    @Transactional
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
     * @param uscId the order unique identifier.
     * @return the [OrderDetailResponse].
     */
    fun getDetails(accountId: Long, uscId: String): OrderDetailResponse {
        val order = get(accountId, uscId)
        // -- map and return the details of Order --
        return order.toOrderDetailResponse(order.voucher)
    }

    /**
     * a function to get the [OrderDetailResponse] of a specific [Order] with given voucher code.
     *
     * @param accountId the account unique identifier.
     * @param uscId the urban sneaker care unique identifier.
     * @param voucherCode the code of [Voucher].
     * @return the [OrderDetailResponse] instance.
     */
    fun applyVoucher(accountId: Long, uscId: String, voucherCode: String): OrderDetailResponse {
        val order = get(accountId, uscId)
        // -- get the voucher --
        val voucher = voucherService.get(voucherCode)
        // -- map and return the details of Order with Voucher --
        return order.toOrderDetailResponse(voucher)
    }
}
