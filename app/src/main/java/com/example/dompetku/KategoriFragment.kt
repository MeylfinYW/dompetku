package com.example.dompetku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dompetku.databinding.FragmentKategoriBinding

class KategoriFragment : Fragment() {

    private lateinit var binding: FragmentKategoriBinding
    private lateinit var spHelper: SPHelper
    private lateinit var adapter: KategoriAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKategoriBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spHelper = SPHelper(requireContext())

        adapter = KategoriAdapter(spHelper.getSemuaKategori()) { kategori ->
            spHelper.hapusKategori(kategori.id)
            refreshList()
            Toast.makeText(requireContext(), "Kategori dihapus", Toast.LENGTH_SHORT).show()
        }

        binding.rvKategori.layoutManager = LinearLayoutManager(requireContext())
        binding.rvKategori.adapter = adapter

        binding.btnSimpanKategori.setOnClickListener {
            val nama = binding.etNamaKategori.text.toString().trim()
            if (nama.isEmpty()) {
                binding.etNamaKategori.error = "Nama kategori wajib diisi"
                return@setOnClickListener
            }

            val tipe = if (binding.rbIncome.isChecked) "INCOME" else "EXPENSE"
            val idBaru = "K-${System.currentTimeMillis()}"
            spHelper.tambahKategori(Kategori(idBaru, nama, tipe))

            binding.etNamaKategori.text.clear()
            refreshList()
            Toast.makeText(requireContext(), "Kategori berhasil disimpan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshList() {
        adapter.updateData(spHelper.getSemuaKategori())
    }
}