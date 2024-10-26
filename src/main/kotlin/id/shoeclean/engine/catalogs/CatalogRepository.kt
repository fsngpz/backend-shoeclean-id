package id.shoeclean.engine.catalogs

import org.springframework.data.jpa.repository.JpaRepository

/**
 * The interface represent the repository of [Catalog].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-26
 */
interface CatalogRepository : JpaRepository<Catalog, Long> {

    /**
     * a method to find the [Catalog] by the service type.
     *
     * @param serviceType the [ServiceType] instance.
     * @return the [Catalog] or null.
     */
    fun findByServiceType(serviceType: ServiceType): Catalog?
}
