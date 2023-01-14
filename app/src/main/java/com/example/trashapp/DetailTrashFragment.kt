package com.example.trashapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.trashapp.databinding.FragmentDetailTrashBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailTrashFragment : Fragment() {

    private var _binding: FragmentDetailTrashBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailTrashBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getting the position of the trash given by the user
        val act: AddTrashActivity? = activity as AddTrashActivity?
        val givenPosition: List<String> = act!!.getStartPosition().split(",")

        binding.buttonSecond.setOnClickListener {
            Toast.makeText(context, "Trash reported", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}