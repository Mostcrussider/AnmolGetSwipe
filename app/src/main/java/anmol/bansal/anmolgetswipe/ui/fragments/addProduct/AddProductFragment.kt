package anmol.bansal.anmolgetswipe.ui.fragments.addProduct

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import anmol.bansal.anmolgetswipe.databinding.FragmentAddProductBinding
import anmol.bansal.anmolgetswipe.utils.CommonUtils
import anmol.bansal.anmolgetswipe.utils.CommonUtils.hideKeyboard
import anmol.bansal.anmolgetswipe.utils.CommonUtils.resizeAndCompressImage
import anmol.bansal.anmolgetswipe.utils.FieldValidationHandler
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.File

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var viewModel: AddProductViewModel
    private lateinit var fieldValidationHandler: FieldValidationHandler
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        viewModel.fetchProductTypeList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        setupListeners()
        observeViewModel()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is AddProductViewModel.ResponseEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is AddProductViewModel.ResponseEvent.ProductTypeLoaded -> {
                        binding.progressBar.isVisible = false

                        fieldValidationHandler = FieldValidationHandler(
                            binding.etProductName,
                            binding.etSellingPrice,
                            binding.etTaxRate,
                            binding.productTypeList,
                            binding.btnAddProduct,
                            event.productTypeList
                        )
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            event.productTypeList
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.productTypeList.setAdapter(adapter)
                    }
                    is AddProductViewModel.ResponseEvent.NewProductAdded -> {
                        binding.progressBar.isVisible = false
                        if (event.addNewProductResponse.success)
                            CommonUtils.showSnackBar(
                                binding.root,
                                event.addNewProductResponse.message
                            )

                        delay(1000)
                        findNavController().popBackStack()
                    }

                    is AddProductViewModel.ResponseEvent.Failure -> {
                        binding.progressBar.isVisible = false
                        CommonUtils.showSnackBar(binding.root, event.errorText)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setupListeners() {

        binding.btnAddProduct.setOnClickListener {
            var imageFile: File? = null
            if (imageUri != null) {
                imageFile = resizeAndCompressImage(requireContext(), imageUri!!)
            }

            viewModel.addNewProduct(
                binding.productTypeList.text.toString(),
                binding.etProductName.text.toString(),
                binding.etSellingPrice.text.toString(),
                binding.etTaxRate.text.toString(),
                imageFile
            )
        }

        binding.btnSelectImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUri = it
                binding.ivSelectedImage.visibility = View.VISIBLE
                binding.ivSelectedImage.setImageURI(imageUri)
            }
        }
}