package com.example.dompetku

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IncomeFragment : Fragment() {

    private lateinit var spHelper: SPHelper
    private lateinit var adapter: TransaksiAdapter
    private lateinit var tvFormTitle: TextView
    private lateinit var etJudul: EditText
    private lateinit var etNominal: EditText
    private lateinit var spKategori: Spinner
    private lateinit var etTanggal: EditText
    private lateinit var etCatatan: EditText
    private lateinit var btnSimpan: Button
    private lateinit var btnBatal: Button

    private var listKategoriIncome: List<String> = emptyList()
    private var editingId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaksi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spHelper = SPHelper(requireContext())

        tvFormTitle = view.findViewById(R.id.tvFormTitle)
        val tvListTitle: TextView = view.findViewById(R.id.tvListTitle)
        etJudul = view.findViewById(R.id.etJudul)
        etNominal = view.findViewById(R.id.etNominal)
        spKategori = view.findViewById(R.id.spKategori)
        etTanggal = view.findViewById(R.id.etTanggal)
        etCatatan = view.findViewById(R.id.etCatatan)
        btnSimpan = view.findViewById(R.id.btnSimpanTransaksi)
        btnBatal = view.findViewById(R.id.btnBatalEdit)
        val rvTransaksi: RecyclerView = view.findViewById(R.id.rvTransaksi)

        tvFormTitle.text = "Catat Pemasukan (Income)"
        tvListTitle.text = "Daftar Pemasukan"

        setupSpinnerKategori()
        setupDatePicker()

        adapter = TransaksiAdapter(
            spHelper.getTransaksiByTipe("INCOME"),
            onEditClick = { transaksi -> masukModeEdit(transaksi) },
            onHapusClick = { transaksi ->
                spHelper.hapusTransaksi(transaksi.id)
                if (editingId == transaksi.id) resetForm()
                refreshList()
                Toast.makeText(requireContext(), "Pemasukan dihapus", Toast.LENGTH_SHORT).show()
            }
        )

        rvTransaksi.layoutManager = LinearLayoutManager(requireContext())
        rvTransaksi.adapter = adapter

        btnSimpan.setOnClickListener { simpanData() }
        btnBatal.setOnClickListener { resetForm() }
    }

    private fun setupSpinnerKategori() {
        listKategoriIncome = spHelper.getKategoriByTipe("INCOME").map { it.nama }
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listKategoriIncome
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKategori.adapter = spinnerAdapter
    }

    private fun setupDatePicker() {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etTanggal.setText(sdf.format(cal.time))

        etTanggal.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val dipilih = Calendar.getInstance()
                    dipilih.set(year, month, day)
                    etTanggal.setText(sdf.format(dipilih.time))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun simpanData() {
        val judul = etJudul.text.toString().trim()
        val nominalStr = etNominal.text.toString().trim()
        val tanggal = etTanggal.text.toString().trim()
        val catatan = etCatatan.text.toString().trim()

        if (judul.isEmpty()) {
            etJudul.error = "Judul wajib diisi"
            return
        }
        if (nominalStr.isEmpty() || nominalStr.toLongOrNull() == null) {
            etNominal.error = "Nominal wajib angka"
            return
        }
        if (listKategoriIncome.isEmpty()) {
            Toast.makeText(requireContext(), "Tambahkan kategori Income dulu!", Toast.LENGTH_SHORT).show()
            return
        }

        val kategoriDipilih = spKategori.selectedItem.toString()
        val nominal = nominalStr.toLong()

        if (editingId == null) {
            val baru = Transaksi(
                id = "INC-${System.currentTimeMillis()}",
                judul = judul,
                nominal = nominal,
                kategori = kategoriDipilih,
                tipe = "INCOME",
                tanggal = tanggal,
                catatan = catatan
            )
            spHelper.tambahTransaksi(baru)
            Toast.makeText(requireContext(), "Pemasukan dicatat", Toast.LENGTH_SHORT).show()
        } else {
            val update = Transaksi(
                id = editingId!!,
                judul = judul,
                nominal = nominal,
                kategori = kategoriDipilih,
                tipe = "INCOME",
                tanggal = tanggal,
                catatan = catatan
            )
            spHelper.ubahTransaksi(update)
            Toast.makeText(requireContext(), "Pemasukan diperbarui", Toast.LENGTH_SHORT).show()
        }

        resetForm()
        refreshList()
    }

    private fun masukModeEdit(t: Transaksi) {
        editingId = t.id
        tvFormTitle.text = "Ubah Pemasukan"
        etJudul.setText(t.judul)
        etNominal.setText(t.nominal.toString())
        etTanggal.setText(t.tanggal)
        etCatatan.setText(t.catatan)

        val pos = listKategoriIncome.indexOf(t.kategori)
        if (pos >= 0) spKategori.setSelection(pos)

        btnSimpan.text = "Update"
        btnBatal.visibility = View.VISIBLE
    }

    private fun resetForm() {
        editingId = null
        tvFormTitle.text = "Catat Pemasukan (Income)"
        etJudul.text.clear()
        etNominal.text.clear()
        etCatatan.text.clear()
        btnSimpan.text = "Simpan"
        btnBatal.visibility = View.GONE
    }

    private fun refreshList() {
        adapter.updateData(spHelper.getTransaksiByTipe("INCOME"))
    }
}