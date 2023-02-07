package com.example.trashapp.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trashapp.DBUtils
import com.example.trashapp.LoginActivity
import com.example.trashapp.R
import com.example.trashapp.databinding.FragmentAccountBinding
import com.example.trashapp.ui.users.AddUserActivity
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
            DBUtils.getUser(binding.root.context, binding)


        val role = context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)
            ?.getString("role", "")?.split(",")
        if (role != null) {
            when {
                role.contains("ADMIN") -> {
                    binding.buttonAccountSettings.isVisible = false
                }
                role.contains("USER") -> {
                    binding.buttonAccountSettings.isVisible = true
                }
                else -> {
                    binding.buttonAccountSettings.isVisible = false
                }
            }
        }

        context?.let { DBUtils.getActiveTrashCount(it, binding) }
        context?.let { DBUtils.getArchiveTrashCount(it, binding) }

        return root
    }

    override fun onResume() {
        super.onResume()

        activity?.findViewById<ImageButton>(R.id.sortButton)?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()

        activity?.findViewById<ImageButton>(R.id.sortButton)?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}