package anmol.bansal.anmolgetswipe.ui.fragments.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import anmol.bansal.anmolgetswipe.data.model.ProductItemModel
import anmol.bansal.anmolgetswipe.data.repository.MainRepository
import anmol.bansal.anmolgetswipe.utils.Constants
import anmol.bansal.anmolgetswipe.utils.DispatcherProvider
import anmol.bansal.anmolgetswipe.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class ResponseEvent {
        class Failure(val errorText: String) : ResponseEvent()
        class DataLoaded(val currencyData: List<ProductItemModel>) : ResponseEvent()
        object Loading : ResponseEvent()
        object Empty : ResponseEvent()
    }

    private val _conversion = MutableStateFlow<ResponseEvent>(ResponseEvent.Empty)
    val conversion: StateFlow<ResponseEvent> = _conversion

    fun fetchData() {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = ResponseEvent.Loading
            when (val ratesResponse = repository.getProductList()) {
                is Resource.Error -> _conversion.value =
                    ResponseEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val currencyData = ratesResponse.data
                    if (currencyData != null) {
                        _conversion.value = ResponseEvent.DataLoaded(currencyData)
                    } else {
                        _conversion.value = ResponseEvent.Failure(Constants.DATA_QUERY_FAILURE)
                    }
                }
            }
        }
    }

    fun searchProduct(input: String?) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = ResponseEvent.Loading
            val originalList = mutableListOf<ProductItemModel>()
            when (val ratesResponse = repository.getProductList()) {
                is Resource.Error -> _conversion.value =
                    ResponseEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    originalList.addAll(ratesResponse.data ?: emptyList())

                    if (input.isNullOrBlank()) {
                        _conversion.value = ResponseEvent.DataLoaded(originalList)
                        return@launch
                    }

                    val filteredList = originalList.filter { productItem ->
                        productItem.productName.contains(input, ignoreCase = true)
                    }

                    if (filteredList.isEmpty()) {
                        _conversion.value = ResponseEvent.Failure(Constants.ERROR_NO_MATCH)
                    } else {
                        _conversion.value = ResponseEvent.DataLoaded(filteredList)
                    }
                }
            }
        }
    }

}