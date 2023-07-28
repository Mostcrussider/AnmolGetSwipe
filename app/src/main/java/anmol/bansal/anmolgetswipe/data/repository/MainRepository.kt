package anmol.bansal.anmolgetswipe.data.repository

import anmol.bansal.anmolgetswipe.data.model.AddNewProductModel
import anmol.bansal.anmolgetswipe.data.model.ProductItemModel
import anmol.bansal.anmolgetswipe.utils.Resource
import java.io.File

interface MainRepository {

    suspend fun getProductList(): Resource<List<ProductItemModel>>
    suspend fun addProduct(
        productType: String,
        productName: String,
        sellingPrice: String,
        taxRate: String,
        imageFile: File?
    ) : Resource<AddNewProductModel>

    suspend fun getProductTypeList(): Resource<List<String>>
}