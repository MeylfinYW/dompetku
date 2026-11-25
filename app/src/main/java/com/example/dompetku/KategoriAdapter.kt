package com.example.dompetku

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dompetku.databinding.ItemKategoriBinding

class KategoriAdapter(
    private var listKategori: MutableList<Kategori>,
    private val onHapusClick: (Kategori) -> Unit
) : RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemKategoriBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKategoriBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listKategori[position]
        holder.binding.tvNamaKategori.text = item.nama
        holder.binding.tvTipeKategori.text = item.tipe

        val warna = if (item.tipe == "INCOME") R.color.green_income else R.color.red_expense
        holder.binding.tvTipeKategori.setTextColor(
            ContextCompat.getColor(holder.itemView.context, warna)
        )

        holder.binding.btnHapusKategori.setOnClickListener {
            onHapusClick(item)
        }
    }

    override fun getItemCount(): Int = listKategori.size

    fun updateData(newList: List<Kategori>) {
        listKategori.clear()
        listKategori.addAll(newList)
        notifyDataSetChanged()
    }
}