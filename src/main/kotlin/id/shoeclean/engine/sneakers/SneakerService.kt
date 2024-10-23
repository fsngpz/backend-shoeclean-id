package id.shoeclean.engine.sneakers

import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.exceptions.SneakerNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * The service class to handle the feature related to [Sneaker].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-23
 */
@Service
class SneakerService(
    private val accountService: AccountService,
    private val sneakerRepository: SneakerRepository
) {

    /**
     * a function to find the [Sneaker] using some filter.
     *
     * @param accountId the account unique identifier.
     * @param filter the parameter to find the [Sneaker] by brand or color.
     * @param pageable the [Pageable] instance.
     * @return the [Page] of [SneakerResponse].
     */
    fun findAll(accountId: Long, filter: String?, pageable: Pageable): Page<SneakerResponse> {
        return sneakerRepository.findAllByFilter(accountId, filter, pageable).map { it.toResponse() }
    }

    /**
     * a function to handle creating new [Sneaker].
     *
     * @param accountId the account unique identifier.
     * @param brand the brand of [Sneaker].
     * @param color the color of [Sneaker].
     * @return the [SneakerResponse] instance.
     */
    fun create(accountId: Long, brand: String, color: String): SneakerResponse {
        // -- get the account by id --
        val account = accountService.get(accountId)
        // -- setup new instance of sneaker --
        val sneaker = Sneaker(account, brand, color)
        // -- save the instance --
        sneakerRepository.save(sneaker)
        // -- map and return --
        return sneaker.toResponse()
    }

    /**
     * a function to handle deleting the [Sneaker] of a specific account.
     *
     * @param accountId the account unique identifier.
     * @param sneakerId the sneaker unique identifier.
     */
    fun delete(accountId: Long, sneakerId: Long) {
        val account = accountService.get(accountId)
        // -- find the sneaker by the id and account --
        val sneaker = sneakerRepository.findByIdAndAccount(sneakerId, account)
            ?: throw SneakerNotFoundException("you dont have sneaker with id '$sneakerId'")
        // -- remove the sneaker from db --
        sneakerRepository.delete(sneaker)
    }
}
