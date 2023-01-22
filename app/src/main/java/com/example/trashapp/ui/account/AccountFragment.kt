package com.example.trashapp.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trashapp.DBUtils
import com.example.trashapp.LoginActivity
import com.example.trashapp.databinding.FragmentAccountBinding

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

        context?.let { preferences?.getString("login", "")
            ?.let { it1 -> DBUtils.getAccount(it, binding, it1) } }

        binding.buttonAccountLogOut.setOnClickListener {
            val editor = preferences!!.edit()
            editor.putString("login", "")
            editor.putString("password", "")
            editor.apply()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}