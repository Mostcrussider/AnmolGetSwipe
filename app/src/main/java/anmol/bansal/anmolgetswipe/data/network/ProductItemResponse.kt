package anmol.bansal.anmolgetswipe.data.network

data class ProductItemResponse(
    val image: String,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)