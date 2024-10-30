package id.shoeclean.engine.orders

/**
 * an extension function to convert the [Order] to [SubmitOrderResponse].
 *
 * @author Ferdinand Sangap.
 * @since 2024-10-30
 */
fun Order.toSubmitOrderResponse(): SubmitOrderResponse {
    val orderId = this.orderId
    // -- validate the field orderId --
    requireNotNull(orderId) { "Order ID must not be null" }
    // -- return the instance --
    return SubmitOrderResponse(orderId)
}
