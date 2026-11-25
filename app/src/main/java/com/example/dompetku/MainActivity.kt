package com.example.dompetku //

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dompetku.databinding.ActivityMainBinding //

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            gantiFragment(KategoriFragment(), "Dompetku - Master Kategori")
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_kategori -> {
                    gantiFragment(KategoriFragment(), "Dompetku - Master Kategori")
                    true
                }
                else -> false
            }
        }
    }

    private fun gantiFragment(fragment: Fragment, judul: String) {
        binding.tvHeaderTitle.text = judul
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}