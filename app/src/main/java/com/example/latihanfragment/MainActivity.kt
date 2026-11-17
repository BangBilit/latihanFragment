package com.example.latihanfragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    val tambahHistory = mutableListOf<String>()
    val kurangHistory = mutableListOf<String>()
    val kaliHistory = mutableListOf<String>()
    val bagiHistory = mutableListOf<String>()

    private lateinit var mainLayout: View
    private lateinit var angkaPertama: EditText
    private lateinit var angkaKedua: EditText
    private lateinit var tvResult: TextView
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private lateinit var tambahFrag: tambahFragment
    private lateinit var kurangFrag: kurangFragment
    private lateinit var kaliFrag: kaliFragment
    private lateinit var bagiFrag: bagiFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        angkaPertama = findViewById(R.id.angkaPertama)
        angkaKedua = findViewById(R.id.angkaKedua)

        val btnTambah = findViewById<Button>(R.id.btnTambah)
        val btnKurang = findViewById<Button>(R.id.btnKurang)
        val btnKali = findViewById<Button>(R.id.btnKali)
        val btnBagi = findViewById<Button>(R.id.btnBagi)

        tvResult = findViewById(R.id.tvResult)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        tambahFrag = tambahFragment()
        kurangFrag = kurangFragment()
        kaliFrag = kaliFragment()
        bagiFrag = bagiFragment()

        val pagerAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 4
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> tambahFrag
                    1 -> kurangFrag
                    2 -> kaliFrag
                    3 -> bagiFrag
                    else -> tambahFrag
                }
            }
        }

        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Tambah"
                1 -> "Kurang"
                2 -> "Kali"
                3 -> "Bagi"
                else -> ""
            }
        }.attach()

        btnTambah.setOnClickListener { calculate("+") }
        btnKurang.setOnClickListener { calculate("-") }
        btnKali.setOnClickListener { calculate("*") }
        btnBagi.setOnClickListener { calculate("/") }
    }

    private fun calculate(operator: String) {
        val s1 = angkaPertama.text.toString()
        val s2 = angkaKedua.text.toString()

        if (s1.isEmpty() || s2.isEmpty()) {
            Toast.makeText(this, "Mohon masukkan kedua angka terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val num1 = s1.toDoubleOrNull()
        val num2 = s2.toDoubleOrNull()

        if (num1 == null || num2 == null) {
            Toast.makeText(this, "Format angka tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        val result: Double = when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            "/" -> {
                if (num2 == 0.0) {
                    Toast.makeText(this, "Tidak dapat membagi dengan nol", Toast.LENGTH_SHORT).show()
                    return
                }
                num1 / num2
            }
            else -> 0.0
        }

        val formattedResult = if (result % 1.0 == 0.0) {
            result.toInt().toString()
        } else {
            result.toString()
        }

        val historyItem = "$num1 $operator $num2 = $formattedResult"

        when (operator) {
            "+" -> {
                tambahHistory.add(historyItem)
                if (::tambahFrag.isInitialized) tambahFrag.refreshHistory()
            }
            "-" -> {
                kurangHistory.add(historyItem)
                if (::kurangFrag.isInitialized) kurangFrag.refreshHistory()
            }
            "*" -> {
                kaliHistory.add(historyItem)
                if (::kaliFrag.isInitialized) kaliFrag.refreshHistory()
            }
            "/" -> {
                bagiHistory.add(historyItem)
                if (::bagiFrag.isInitialized) bagiFrag.refreshHistory()
            }
        }

        tvResult.text = "Hasil: $formattedResult"

        // Requirement 5: Apabila nilai yang dihasilkan dari perhitungan kurang dari 0, maka ubah background menjadi berwarna merah
        if (result < 0) {
            mainLayout.setBackgroundColor(Color.parseColor("#FFCDD2")) // Light red
        } else {
            mainLayout.setBackgroundColor(Color.WHITE)
        }
    }
}
