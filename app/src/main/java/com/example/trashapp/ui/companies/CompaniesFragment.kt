package com.example.trashapp.ui.companies

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.trashapp.R
import com.example.trashapp.databinding.FragmentCompaniesBinding

class CompaniesFragment : Fragment() {

    private var _binding: FragmentCompaniesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(CompaniesViewModel::class.java)

        _binding = FragmentCompaniesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCompanies
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}