package anmol.bansal.anmolgetswipe.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import anmol.bansal.anmolgetswipe.data.model.AddNewProductModel
import anmol.bansal.anmolgetswipe.data.model.ProductItemModel
import anmol.bansal.anmolgetswipe.data.network.ProductApi
import anmol.bansal.anmolgetswipe.data.network.ProductItemResponse
import anmol.bansal.anmolgetswipe.data.network.ProductResponse
import anmol.bansal.anmolgetswipe.utils.Constants
import anmol.bansal.anmolgetswipe.utils.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class MainRepositoryImpl(
    private val api: ProductApi,
    private val connectivityManager: ConnectivityManager
) : MainRepository {

    private var mProductListResponse: List<ProductItemResponse> = emptyList()

    override suspend fun getProductList(): Resource<List<ProductItemModel>> {
        return try {
            if (isNetworkAvailable()) {
                val listResponse = api.getProductList()
                if (listResponse.isSuccessful && listResponse.body() != null) {
                    mProductListResponse = emptyList()
                    mProductListResponse = listResponse.body()!!
                    Resource.Success(mapDataToModal(mProductListResponse))
                } else {
                    Resource.Error(listResponse.message())
                }
            } else {
                Resource.Error(Constants.ERROR_NO_NETWORK)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: Constants.ERROR_BASE)
        }
    }

    override suspend fun addProduct(
        productType: String,
        productName: String,
        sellingPrice: String,
        taxRate: String,
        imageFile: File?
    ): Resource<AddNewProductModel> {
        return try {
            if (isNetworkAvailable()) {
                val response = if (imageFile != null) {
                    val productTypeRequestBody = productType.toRequestBody()
                    val productNameRequestBody = productName.toRequestBody()
                    val sellingPriceRequestBody = sellingPrice.toRequestBody()
                    val taxRateRequestBody = taxRate.toRequestBody()
                    val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart =
                        MultipartBody.Part.createFormData("files", imageFile.name, imageRequestBody)
                    api.addProductWithImage(
                        productTypeRequestBody,
                        productNameRequestBody,
                        sellingPriceRequestBody,
                        taxRateRequestBody,
                        imagePart
                    )
                } else {
                    api.addProductWithoutImage(productType, productName, sellingPrice, taxRate)
                }

                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(mapNewProductDataToModal(response.body()!!))
                } else {
                    Resource.Error(response.message())
                }
            } else {
                Resource.Error(Constants.ERROR_NO_NETWORK)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: Constants.ERROR_BASE)
        }
    }

    override suspend fun getProductTypeList(): Resource<List<String>> {
        return try {
            if (mProductListResponse.isNotEmpty())
                Resource.Success(getProductTypeList(mProductListResponse))
            else {
                Resource.Error(Constants.ERROR_NO_PRODUCT_TYPE_FOUND)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: Constants.ERROR_BASE)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetworkInfo =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetworkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetworkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetworkInfo.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    private fun getProductTypeList(list: List<ProductItemResponse>): List<String> {
        val productTypes = list.mapNotNull { it.product_type?.trim() }
            .filter { it.isNotBlank() }
            .sortedWith { str1, str2 ->
                when {
                    str1.first().isDigit() && !str2.first()
                        .isDigit() -> -1
                    !str1.first().isDigit() && str2.first()
                        .isDigit() -> 1
                    else -> str1.compareTo(str2)
                }
            }
        return productTypes.distinct()
    }


    private fun mapDataToModal(list: List<ProductItemResponse>): List<ProductItemModel> {
        return list.map {
            ProductItemModel(
                it.image,
                it.price,
                it.product_name,
                it.product_type,
                it.tax
            )
        }
    }

    private fun mapNewProductDataToModal(productResponse: ProductResponse): AddNewProductModel {
        return AddNewProductModel(
            productResponse.message,
            productResponse.product_details,
            productResponse.product_id,
            productResponse.success
        )
    }
}