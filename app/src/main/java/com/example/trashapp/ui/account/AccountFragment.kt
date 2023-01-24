package com.example.trashapp.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trashapp.DBUtils
import com.example.trashapp.LoginActivity
import com.example.trashapp.MainActivity
import com.example.trashapp.R
import com.example.trashapp.classes.User
import com.example.trashapp.databinding.FragmentAccountBinding
import com.example.trashapp.watchers.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val preferences = this.activity?.getSharedPreferences("credentials", Context.MODE_PRIVATE)

        binding.buttonAccountLogOut.setOnClickListener {
            val editor = preferences!!.edit()
            editor.putString("login", "")
            editor.putString("password", "")
            editor.apply()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

        // TODO - count archive trash function
        CoroutineScope(Dispatchers.IO).launch {
            try {

                withContext(Dispatchers.Main) {

//                    Log.i("ServerSQL", json.toString())
//                    if (DBUtils.checkForError(context!!, json.toString())) {
//                        return@withContext
//                    }
                    binding.archiveCount.text = "5"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }

        // TODO - count active trash function
        CoroutineScope(Dispatchers.IO).launch {
            try {

                withContext(Dispatchers.Main) {

//                    Log.i("ServerSQL", json.toString())
//                    if (DBUtils.checkForError(context!!, json.toString())) {
//                        return@withContext
//                    }
                    binding.activeCount.text = "2"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}