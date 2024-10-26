package id.shoeclean.engine.catalogs

import id.shoeclean.engine.exceptions.CatalogNotFoundException
import org.springframework.stereotype.Service

/**
 * The class represent the service [Catalog].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
@Service
class CatalogService(private val catalogRepository: CatalogRepository) {

    /**
     * a function to handle request get the [Catalog] using service type.
     *
     * @param serviceType the [ServiceType] enum.
     * @return the [Catalog].
     */
    fun getByServiceType(serviceType: ServiceType): Catalog {
        // -- find the catalog using the service type --
        val catalog = catalogRepository.findByServiceType(serviceType)
        // -- return the Catalog otherwise throw an exception --
        return catalog ?: throw CatalogNotFoundException("no catalog was found with serviceType $serviceType")
    }
}
