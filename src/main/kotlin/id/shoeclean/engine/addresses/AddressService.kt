package id.shoeclean.engine.addresses

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
     * a function to handle get the [AddressResponse] by given account and address unique identifier.
     *
     * @param accountId the account unique identifier.
     * @param addressId the address unique identifier.
     * @return the [AddressResponse] instance.
     */
    fun get(accountId: Long, addressId: Long): AddressResponse {
        // -- get the account --
        val account = accountService.get(accountId)
        // -- find the data and return --
        return addressRepository.findByIdAndAccount(addressId, account)?.toResponse() ?: throw AddressNotFoundException(
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
     * a function to handle removing the [Address] from database.
     *
     * @param accountId the account unique identifier.
     * @param addressId the address unique identifier.
     */
    fun delete(accountId: Long, addressId: Long) {
        val account = accountService.get(accountId)
        // -- find the data and return --
        val address = addressRepository.findByIdAndAccount(addressId, account)
            ?: throw AddressNotFoundException("you dont have address with id $addressId")
        // -- delete the address --
        return addressRepository.delete(address)
    }
}
