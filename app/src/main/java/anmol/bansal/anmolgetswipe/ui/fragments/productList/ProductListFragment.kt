package anmol.bansal.anmolgetswipe.ui.fragments.productList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import anmol.bansal.anmolgetswipe.R
import anmol.bansal.anmolgetswipe.databinding.FragmentProductListBinding
import anmol.bansal.anmolgetswipe.ui.adapters.ProductListAdapter
import anmol.bansal.anmolgetswipe.utils.CommonUtils
import anmol.bansal.anmolgetswipe.utils.CommonUtils.hideKeyboard
import anmol.bansal.anmolgetswipe.utils.Constants
import anmol.bansal.anmolgetswipe.utils.DividerItemDecoration
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ProductListFragment : Fragment() {

    private lateinit var binding: FragmentProductListBinding
    private lateinit var viewModel: ProductListViewModel
    private lateinit var currencyAdapter: ProductListAdapter
    private var lastBackPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        viewModel.fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        setupViews()
        setupListeners()
        observeViewModel()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }

    private fun setupViews() {
        currencyAdapter = ProductListAdapter()
        binding.rvCurrencyList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = currencyAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), R.color.colorGigas))
        }
    }

    private fun setupListeners() {

        binding.etProductNameSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s?.toString()
                hideKeyboard()
                viewModel.searchProduct(input)
            }
        })

        binding.etProductNameSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.btnRetry.setOnClickListener {
            binding.linearError.visibility = View.GONE
            viewModel.fetchData()
        }

        binding.btnAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_productListFragment_to_addProductFragment)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is ProductListViewModel.ResponseEvent.DataLoaded -> {
                        binding.progressBar.isVisible = false
                        currencyAdapter.setData(event.currencyData)
                    }

                    is ProductListViewModel.ResponseEvent.Failure -> {
                        binding.progressBar.isVisible = false
                        if (event.errorText == Constants.ERROR_NO_NETWORK) {
                            binding.linearError.visibility = View.VISIBLE
                            binding.textError.text = event.errorText
                        } else {
                            CommonUtils.showSnackBar(binding.root, event.errorText)
                        }
                    }
                    is ProductListViewModel.ResponseEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastBackPressedTime > Constants.BACK_PRESS_INTERVAL) {
                    lastBackPressedTime = currentTime
                    CommonUtils.showSnackBar(binding.root, resources.getString(R.string.exit_app))
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }
}