package anmol.bansal.anmolgetswipe.data.network

import anmol.bansal.anmolgetswipe.data.model.ProductDetails

data class ProductResponse(
    val message: String,
    val product_details: ProductDetails,
    val product_id: Int,
    val success: Boolean
)