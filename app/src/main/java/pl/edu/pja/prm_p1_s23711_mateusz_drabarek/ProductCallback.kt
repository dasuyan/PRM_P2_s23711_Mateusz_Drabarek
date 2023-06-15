package pl.edu.pja.prm_p1_s23711_mateusz_drabarek

import androidx.recyclerview.widget.DiffUtil
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.model.Product

class ProductCallback(private val notSorted: List<Product>, private val sorted: List<Product>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = notSorted.size

    override fun getNewListSize(): Int = sorted.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        notSorted[oldItemPosition] === sorted[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        notSorted[oldItemPosition] == sorted[newItemPosition]
}