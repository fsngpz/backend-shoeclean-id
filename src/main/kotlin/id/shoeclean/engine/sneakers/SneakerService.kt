package id.shoeclean.engine.sneakers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
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
    private val objectMapper: ObjectMapper,
    private val accountService: AccountService,
    private val sneakerRepository: SneakerRepository
) {

    /**
     * a function to find the [Sneaker] of a specific account and specific sneaker unique identifier.
     *
     * @param accountId the account unique identifier.
     * @param sneakerId the sneaker unique identifier.
     * @return the [Sneaker] instance.
     */
    fun get(accountId: Long, sneakerId: Long): Sneaker {
        val account = accountService.get(accountId)
        // -- find the sneaker or else throw an exception --
        return sneakerRepository.findByIdAndAccount(sneakerId, account)
            ?: throw SneakerNotFoundException("you dont have sneaker with id '$sneakerId'")
    }

    /**
     * a method to find the list of [Sneaker] with account unique identifier and list of sneaker ids.
     *
     * @param accountId the account unique identifier.
     * @param sneakerIds the list of sneaker unique identifier.
     * @return the list of [Sneaker].
     */
    fun findAll(accountId: Long, sneakerIds: List<Long>): List<Sneaker> {
        val account = accountService.get(accountId)
        // -- find the sneaker or else throw an exception --
        return sneakerRepository.findByIdInAndAccount(sneakerIds, account)

    }

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
     * a function to handle update of [Sneaker] instance.
     *
     * @param accountId the account unique identifier.
     * @param sneakerId the sneaker unique identifier.
     * @param request the [SneakerRequestNullable] instance.
     * @return the [Sneaker] instance.
     */
    fun put(accountId: Long, sneakerId: Long, request: SneakerRequestNullable): Sneaker {
        // -- convert the Nullable request to Non Nullable --
        val nonNullRequest = request.toRequest()
        // -- get the Address in database then update --
        val sneaker = get(accountId, sneakerId).apply {
            this.brand = nonNullRequest.brand
            this.color = nonNullRequest.color
        }
        // -- save and return the instance --
        return sneakerRepository.save(sneaker)
    }

    /**
     * a function to patch / partial update to [Sneaker].
     *
     * @param accountId the account unique identifier.
     * @param sneakerId the sneaker unique identifier.
     * @param request the [JsonNode] as a request body.
     * @return the [Sneaker] instance.
     */
    fun patch(accountId: Long, sneakerId: Long, request: JsonNode): Sneaker {
        // -- get the instance of Sneaker --
        val sneaker = get(accountId, sneakerId)
        // -- convert the Sneaker instance to SneakerRequestNullable --
        val body = sneaker.toRequestNullable()
        // -- read object value to update --
        val reader = objectMapper.readerForUpdating(body)
        // -- convert the request to SneakerRequestNullable --
        val updatedSneaker = reader.readValue<SneakerRequestNullable>(request)
        // -- do an update --
        return put(accountId, sneakerId, updatedSneaker)
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
