package com.example.dompetku

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class SPHelper(context: Context) {

    private val sp = context.getSharedPreferences("dompetku_sp", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_KATEGORI = "data_kategori"
        private const val KEY_TRANSAKSI = "data_transaksi"
        private const val KEY_INIT = "sudah_init"
    }

    init {
        if (!sp.getBoolean(KEY_INIT, false)) {
            val defaultList = listOf(
                Kategori("K1", "Gaji", "INCOME"),
                Kategori("K2", "Uang Saku", "INCOME"),
                Kategori("K3", "Makan & Minum", "EXPENSE"),
                Kategori("K4", "Transportasi", "EXPENSE"),
                Kategori("K5", "Belanja", "EXPENSE")
            )
            simpanSemuaKategori(defaultList)
            sp.edit().putBoolean(KEY_INIT, true).apply()
        }
    }


    fun getSemuaKategori(): MutableList<Kategori> {
        val list = mutableListOf<Kategori>()
        val jsonStr = sp.getString(KEY_KATEGORI, "[]") ?: "[]"
        val jsonArray = JSONArray(jsonStr)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                Kategori(
                    obj.getString("id"),
                    obj.getString("nama"),
                    obj.getString("tipe")
                )
            )
        }
        return list
    }

    fun getKategoriByTipe(tipe: String): List<Kategori> {
        return getSemuaKategori().filter { it.tipe == tipe }
    }

    fun simpanSemuaKategori(list: List<Kategori>) {
        val jsonArray = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("nama", item.nama)
            obj.put("tipe", item.tipe)
            jsonArray.put(obj)
        }
        sp.edit().putString(KEY_KATEGORI, jsonArray.toString()).apply()
    }

    fun tambahKategori(kategori: Kategori) {
        val list = getSemuaKategori()
        list.add(kategori)
        simpanSemuaKategori(list)
    }

    fun hapusKategori(id: String) {
        val list = getSemuaKategori()
        list.removeAll { it.id == id }
        simpanSemuaKategori(list)
    }


    fun getSemuaTransaksi(): MutableList<Transaksi> {
        val list = mutableListOf<Transaksi>()
        val jsonStr = sp.getString(KEY_TRANSAKSI, "[]") ?: "[]"
        val jsonArray = JSONArray(jsonStr)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                Transaksi(
                    obj.getString("id"),
                    obj.getString("judul"),
                    obj.getLong("nominal"),
                    obj.getString("kategori"),
                    obj.getString("tipe"),
                    obj.getString("tanggal"),
                    obj.optString("catatan", "")
                )
            )
        }
        return list
    }

    fun getTransaksiByTipe(tipe: String): MutableList<Transaksi> {
        return getSemuaTransaksi().filter { it.tipe == tipe }.toMutableList()
    }

    fun simpanSemuaTransaksi(list: List<Transaksi>) {
        val jsonArray = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("judul", item.judul)
            obj.put("nominal", item.nominal)
            obj.put("kategori", item.kategori)
            obj.put("tipe", item.tipe)
            obj.put("tanggal", item.tanggal)
            obj.put("catatan", item.catatan)
            jsonArray.put(obj)
        }
        sp.edit().putString(KEY_TRANSAKSI, jsonArray.toString()).apply()
    }

    fun tambahTransaksi(transaksi: Transaksi) {
        val list = getSemuaTransaksi()
        list.add(0, transaksi)
        simpanSemuaTransaksi(list)
    }

    fun ubahTransaksi(transaksiBaru: Transaksi) {
        val list = getSemuaTransaksi()
        val index = list.indexOfFirst { it.id == transaksiBaru.id }
        if (index != -1) {
            list[index] = transaksiBaru
            simpanSemuaTransaksi(list)
        }
    }

    fun hapusTransaksi(id: String) {
        val list = getSemuaTransaksi()
        list.removeAll { it.id == id }
        simpanSemuaTransaksi(list)
    }
}