package com.example.dompetku

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var tvHeaderTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHeaderTitle = findViewById(R.id.tvHeaderTitle)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)

        if (savedInstanceState == null) {
            gantiFragment(KategoriFragment(), "Dompetku - Master Kategori")
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_kategori -> {
                    gantiFragment(KategoriFragment(), "Dompetku - Master Kategori")
                    true
                }
                R.id.nav_income -> {
                    gantiFragment(IncomeFragment(), "Dompetku - Pemasukan")
                    true
                }
                R.id.nav_expense -> {
                    gantiFragment(ExpenseFragment(), "Dompetku - Pengeluaran")
                    true
                }
                else -> false
            }
        }
    }

    private fun gantiFragment(fragment: Fragment, judul: String) {
        tvHeaderTitle.text = judul
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}