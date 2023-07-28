package anmol.bansal.anmolgetswipe.ui.fragments.addProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import anmol.bansal.anmolgetswipe.data.model.AddNewProductModel
import anmol.bansal.anmolgetswipe.data.repository.MainRepository
import anmol.bansal.anmolgetswipe.utils.Constants
import anmol.bansal.anmolgetswipe.utils.DispatcherProvider
import anmol.bansal.anmolgetswipe.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class AddProductViewModel(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class ResponseEvent {
        class Failure(val errorText: String) : ResponseEvent()
        class ProductTypeLoaded(val currencyData: List<String>) : ResponseEvent()
        class NewProductAdded(val addNewProductResponse: AddNewProductModel) : ResponseEvent()
        object Loading : ResponseEvent()
        object Empty : ResponseEvent()
    }

    private val _conversion = MutableStateFlow<ResponseEvent>(ResponseEvent.Empty)
    val conversion: StateFlow<ResponseEvent> = _conversion

    fun fetchProductTypeList() {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = ResponseEvent.Loading
            when (val ratesResponse = repository.getProductTypeList()) {
                is Resource.Error -> _conversion.value =
                    ResponseEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val currencyData = ratesResponse.data
                    if (currencyData != null) {
                        _conversion.value = ResponseEvent.ProductTypeLoaded(currencyData)
                    } else {
                        _conversion.value = ResponseEvent.Failure(Constants.DATA_QUERY_FAILURE)
                    }
                }
            }
        }
    }

    fun addNewProduct(
        productType: String,
        productName: String,
        sellingPrice: String,
        taxRate: String,
        imageFile: File?
    ) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = ResponseEvent.Loading
            when (val ratesResponse = repository.addProduct(productType,productName,sellingPrice,taxRate,imageFile)) {
                is Resource.Error -> _conversion.value =
                    ResponseEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val response = ratesResponse.data
                    if (response != null) {
                        _conversion.value = ResponseEvent.NewProductAdded(response)
                    } else {
                        _conversion.value = ResponseEvent.Failure(Constants.DATA_QUERY_FAILURE)
                    }
                }
            }
        }
    }
}