package id.shoeclean.engine.orders

import id.shoeclean.engine.sneakers.Sneaker
import id.shoeclean.engine.sneakers.SneakerService
import org.springframework.stereotype.Service

/**
 * The service class to handle feature related to [OrderSneaker].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-30
 */
@Service
class OrderSneakerService(
    private val sneakerService: SneakerService,
    private val orderSneakerRepository: OrderSneakerRepository
) {


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
