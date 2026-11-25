package com.example.dompetku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class TransaksiAdapter(
    private var listTransaksi: MutableList<Transaksi>,
    private val onEditClick: (Transaksi) -> Unit,
    private val onHapusClick: (Transaksi) -> Unit
) : RecyclerView.Adapter<TransaksiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJudul: TextView = view.findViewById(R.id.tvJudulTransaksi)
        val tvInfo: TextView = view.findViewById(R.id.tvInfoTransaksi)
        val tvNominal: TextView = view.findViewById(R.id.tvNominalTransaksi)
        val tvCatatan: TextView = view.findViewById(R.id.tvCatatanTransaksi)
        val btnEdit: Button = view.findViewById(R.id.btnEditTransaksi)
        val btnHapus: Button = view.findViewById(R.id.btnHapusTransaksi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaksi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTransaksi[position]
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        holder.tvJudul.text = item.judul
        holder.tvInfo.text = "${item.kategori} • ${item.tanggal}"
        holder.tvNominal.text = formatRupiah.format(item.nominal)

        val warna = if (item.tipe == "INCOME") R.color.green_income else R.color.red_expense
        holder.tvNominal.setTextColor(
            ContextCompat.getColor(holder.itemView.context, warna)
        )

        if (item.catatan.isNotEmpty()) {
            holder.tvCatatan.visibility = View.VISIBLE
            holder.tvCatatan.text = item.catatan
        } else {
            holder.tvCatatan.visibility = View.GONE
        }

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnHapus.setOnClickListener { onHapusClick(item) }
    }

    override fun getItemCount(): Int = listTransaksi.size

    fun updateData(newList: List<Transaksi>) {
        listTransaksi.clear()
        listTransaksi.addAll(newList)
        notifyDataSetChanged()
    }
}