package anmol.bansal.anmolgetswipe.data.network

import anmol.bansal.anmolgetswipe.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {

    @GET(Constants.getProductList)
    suspend fun getProductList(): Response<List<ProductItemResponse>>

    @FormUrlEncoded
    @POST(Constants.addProduct)
    suspend fun addProductWithoutImage(
        @Field("product_type") productType: String,
        @Field("product_name") productName: String,
        @Field("price") price: String,
        @Field("tax") tax: String
    ): Response<ProductResponse>

    @Multipart
    @POST(Constants.addProduct)
    suspend fun addProductWithImage(
        @Part("product_type") productType: RequestBody,
        @Part("product_name") productName: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part files: MultipartBody.Part
    ): Response<ProductResponse>
}