package com.example.trashapp.ui.collectingpoints

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.adapters.CollectingPointItemAdapter
import com.example.trashapp.adapters.GroupItemAdapter
import com.example.trashapp.databinding.FragmentCollectingPointsBinding

class CollectingPointsFragment : Fragment() {

    private var _binding: FragmentCollectingPointsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(CollectingPointsViewModel::class.java)

        _binding = FragmentCollectingPointsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewCollectingPoints)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val pointsArray = context?.let { DBUtils.getCollectingPoints(it, "admin") }
        val adapter = CollectingPointItemAdapter(pointsArray)
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}