package com.example.trashapp.ui.workers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.databinding.FragmentWorkersBinding
import com.example.trashapp.ui.collectingpoints.AddPointActivity

class WorkersFragment : Fragment() {

    private var _binding: FragmentWorkersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(WorkersViewModel::class.java)

        _binding = FragmentWorkersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val addButton = binding.root.findViewById<Button>(R.id.buttonWorkersAdd)
        addButton.setOnClickListener {
            val intent = Intent(context, AddWorkerActivity::class.java)
            context?.startActivity(intent)
        }

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewWorkers)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getWorkers(it, recyclerView) }

        return root
    }

    override fun onResume(){
        super.onResume()

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewWorkers)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getWorkers(it, recyclerView) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}