package id.shoeclean.engine.transaction

import id.shoeclean.engine.orders.toOrderDetailResponse

/**
 * an extension class to map the [Transaction] to [TransactionResponse].
 *
 * @author Ferdinand Sangap.
 * @since 2024-11-01
 */
fun Transaction.toResponse(): TransactionResponse {
    val uscId = this.order.uscId
    // -- validate the uscId --
    requireNotNull(uscId) {
        throw IllegalArgumentException("the uscId is null")
    }
    val orderDetails = this.order.toOrderDetailResponse(this.order.voucher)
    // -- map the Transaction to TransactionResponse --
    return TransactionResponse(
        uscId = uscId,
        serviceType = this.order.catalog.serviceType,
        subtotal = orderDetails.subtotal,
        discount = orderDetails.discount,
        totalAmount = orderDetails.totalAmount,
        method = this.method,
        status = this.status,
        createdAt = this.createdAt
    )
}
