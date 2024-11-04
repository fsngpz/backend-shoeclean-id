package id.shoeclean.engine.addresses

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import id.shoeclean.engine.accounts.AccountService
import id.shoeclean.engine.exceptions.AddressNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * The service class for feature related to [Address].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-25
 */
@Service
class AddressService(
    private val objectMapper: ObjectMapper,
    private val accountService: AccountService,
    private val addressRepository: AddressRepository
) {

    /**
     * a function to find all data with filter.
     *
     * @param accountId the account unique identifier.
     * @param filter the parameter to find the [Address] by label, line, city, district, subdistrict or state.
     * @param pageable the [Pageable].
     * @return the [Page] of [AddressResponse].
     */
    fun findAll(accountId: Long, filter: String?, pageable: Pageable): Page<AddressResponse> {
        // -- find the data then map --
        return addressRepository.findAllByFilter(accountId, filter, pageable).map { it.toResponse() }
    }

    /**
     * a function to handle get the [Address] by given account and address unique identifier.
     *
     * @param accountId the account unique identifier.
     * @param addressId the address unique identifier.
     * @return the [Address] instance.
     */
    fun get(accountId: Long, addressId: Long): Address {
        // -- get the account --
        val account = accountService.get(accountId)
        // -- find the data and return --
        return addressRepository.findByIdAndAccount(addressId, account) ?: throw AddressNotFoundException(
            "address with id $addressId does not exist"
        )
    }

    /**
     * a function to handle create new [Address].
     *
     * @param accountId the account unique identifier.
     * @param label the label of address.
     * @param line the line of address.
     * @param city the city of address.
     * @param district the district of address.
     * @param subdistrict the subdistrict of address.
     * @param state the state or province of address.
     * @return the [AddressResponse] instance.
     */
    fun create(
        accountId: Long,
        label: String,
        line: String,
        city: String,
        district: String,
        subdistrict: String,
        state: String
    ): AddressResponse {
        // -- get the account by given id --
        val account = accountService.get(accountId)
        // -- setup the instance of Address --
        val address = Address(account, label, line, city, district, subdistrict, state)
        // -- save the instance to database --
        addressRepository.save(address)
        // -- map and return --
        return address.toResponse()
    }


    /**
     * a function to handle update of [Address] instance.
     *
     * @param accountId the account unique identifier.
     * @param addressId the address unique identifier.
     * @param request the [AddressRequestNullable] instance.
     * @return the [Address] instance.
     */
    fun put(accountId: Long, addressId: Long, request: AddressRequestNullable): Address {
        // -- convert the Nullable request to Non Nullable --
        val nonNullRequest = request.toRequest()
        // -- get the Address in database then update --
        val address = get(accountId, addressId).apply {
            this.label = nonNullRequest.label
            this.line = nonNullRequest.line
            this.city = nonNullRequest.city
            this.district = nonNullRequest.district
            this.subdistrict = nonNullRequest.subdistrict
            this.state = nonNullRequest.state
            this.isSelected = nonNullRequest.isSelected
        }
        // -- save and return the instance --
        return addressRepository.save(address)
    }

    /**
     * a function to patch / partial update to [Address].
     *
     * @param accountId the account unique identifier.
     * @param addressId the address unique identifier.
     * @param request the [JsonNode] as a request body.
     * @return the [Address] instance.
     */
    fun patch(accountId: Long, addressId: Long, request: JsonNode): Address {
        // -- get the instance Address --
        val address = get(accountId, addressId)
        // -- convert the Address instance to AddressRequestNullable --
        val body = address.toRequestNullable()
        // -- read object value to update --
        val reader = objectMapper.readerForUpdating(body)
        // -- convert the request to AddressRequestNullable --
        val updatedAddress = reader.readValue<AddressRequestNullable>(request)
        // -- do an update --
        return put(accountId, addressId, updatedAddress)
    }

    /**
     * a function to handle removing the [Address] from database.
     *
     * @param accountId the account unique identifier.
     * @param addressId the address unique identifier.
     */
    fun delete(accountId: Long, addressId: Long) {
        // -- get the address  --
        val address = get(accountId, addressId)
        // -- delete the address --
        return addressRepository.delete(address)
    }
}
