package anmol.bansal.anmolgetswipe.data.model

data class ProductItemModel(
    val image: String,
    val price: Double,
    val productName: String,
    val productType: String,
    val tax: Double
) {
    fun getTax(): String {
        return String.format("%.2f", tax)
    }

    fun getPrice(): String {
        return String.format("%.2f", price)
    }

    fun getImageExist() : Boolean {
        return image != ""
    }
}