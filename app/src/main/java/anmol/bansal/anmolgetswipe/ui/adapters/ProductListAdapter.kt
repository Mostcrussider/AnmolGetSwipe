package anmol.bansal.anmolgetswipe.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import anmol.bansal.anmolgetswipe.R
import anmol.bansal.anmolgetswipe.data.model.ProductItemModel
import anmol.bansal.anmolgetswipe.databinding.ItemProductBinding
import com.squareup.picasso.Picasso

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.CurrencyViewHolder>() {

    private val productList = mutableListOf<ProductItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    fun setData(data: List<ProductItemModel>) {
        productList.clear()
        productList.addAll(data)
        notifyDataSetChanged()
    }

    inner class CurrencyViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productItem: ProductItemModel) {
            if (productItem.getImageExist()) {
                Picasso.get()
                    .load(productItem.image)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fit()
                    .centerInside()
                    .into(binding.imageView)
            } else {
                binding.imageView.setImageResource(R.drawable.placeholder)
            }

            binding.tvProductName.text = productItem.productName
            binding.tvProductType.text = productItem.productType
            binding.tvProductPrice.text = productItem.getPrice()
            binding.tvProductTax.text = productItem.getTax()
        }
    }
}
