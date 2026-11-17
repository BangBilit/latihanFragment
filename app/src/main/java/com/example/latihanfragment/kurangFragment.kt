package com.example.latihanfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class kurangFragment : Fragment() {
    private lateinit var tvHistory: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kurang, container, false)
        tvHistory = view.findViewById(R.id.tvHistory)
        refreshHistory()
        return view
    }

    override fun onResume() {
        super.onResume()
        refreshHistory()
    }

    fun refreshHistory() {
        if (!::tvHistory.isInitialized) return
        val mainActivity = activity as? MainActivity
        val list = mainActivity?.kurangHistory ?: emptyList<String>()
        if (list.isEmpty()) {
            tvHistory.text = "Belum ada history"
        } else {
            tvHistory.text = list.joinToString("\n")
        }
    }
}
