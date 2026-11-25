package com.example.dompetku

data class Transaksi(
    val id: String,
    val judul: String,
    val nominal: Long,
    val kategori: String,
    val tipe: String,
    val tanggal: String,
    val catatan: String
)