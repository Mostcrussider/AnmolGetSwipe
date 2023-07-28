package anmol.bansal.anmolgetswipe.data.model

data class AddNewProductModel(
    val message: String,
    val productDetails: ProductDetails,
    val productId: Int,
    val success: Boolean
)

data class ProductDetails(
    val image: String,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)