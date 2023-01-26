package com.example.trashapp.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trashapp.DBUtils
import com.example.trashapp.LoginActivity
import com.example.trashapp.databinding.FragmentAccountBinding
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

        binding.textAccountLogin.text = preferences?.getString("login", "user login")

        binding.buttonAccountLogOut.setOnClickListener {
            val editor = preferences!!.edit()
            editor.putString("login", "")
            editor.putString("password", "")
            editor.apply()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

        context?.let { DBUtils.getActiveTrashCount(it, binding) }
        context?.let { DBUtils.getArchiveTrashCount(it, binding) }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}