package com.example.trashapp.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trashapp.DBUtils
import com.example.trashapp.LoginActivity
import com.example.trashapp.R
import com.example.trashapp.databinding.FragmentAccountBinding
import com.example.trashapp.watchers.*

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

        val loginEditText = binding.editTextTextAccountLogin
        val loginWatcher = LoginWatcher(loginEditText)
        loginEditText.addTextChangedListener(loginWatcher)

        val passwordEditText = binding.editTextTextAccountPassword
        val passwordWatcher = PasswordWatcher(passwordEditText)
        passwordEditText.addTextChangedListener(passwordWatcher)

        val emailEditText = binding.editTextTextAccountEmail
        val emailWatcher = EmailWatcher(emailEditText)
        emailEditText.addTextChangedListener(emailWatcher)

        val fullnameEditText = binding.editTextTextAccountFullname
        val fullnameWatcher = FullnameWatcher(fullnameEditText)
        fullnameEditText.addTextChangedListener(fullnameWatcher)

        val phoneEditText = binding.editTextTextAccountPhone
        val phoneWatcher = PhoneWatcher(phoneEditText)
        phoneEditText.addTextChangedListener(phoneWatcher)

        val countryEditText = binding.editTextTextAccountCountry
        val countryWatcher = CountryWatcher(countryEditText)
        countryEditText.addTextChangedListener(countryWatcher)

        val cityEditText = binding.editTextTextAccountCity
        val cityWatcher = CityWatcher(cityEditText)
        cityEditText.addTextChangedListener(cityWatcher)

        val districtEditText = binding.editTextTextAccountDistrict
        val districtWatcher = DistrictWatcher(districtEditText)
        districtEditText.addTextChangedListener(districtWatcher)

        val streetEditText = binding.editTextTextAccountStreet
        val streetWatcher = StreetWatcher(streetEditText)
        streetEditText.addTextChangedListener(streetWatcher)

        val houseNumberEditText = binding.editTextTextAccountHouse
        val houseNumberWatcher = HouseNumberWatcher(houseNumberEditText)
        houseNumberEditText.addTextChangedListener(houseNumberWatcher)

        val flatNumberEditText = binding.editTextTextAccountFlat
        val flatNumberWatcher = FlatNumberWatcher(flatNumberEditText)
        flatNumberEditText.addTextChangedListener(flatNumberWatcher)

        val postCodeEditText = binding.editTextTextAccountPostCode
        val postCodeWatcher = PostCodeWatcher(postCodeEditText)
        postCodeEditText.addTextChangedListener(postCodeWatcher)

        val anyChangeWatcher = AnyChangeWatcher(arrayListOf(
                loginEditText,
                passwordEditText,
                emailEditText,
                fullnameEditText,
                phoneEditText,
                countryEditText,
                cityEditText,
                districtEditText,
                streetEditText,
                houseNumberEditText,
                flatNumberEditText,
                postCodeEditText),
            arrayListOf("login", "password", "", "", "", "", "", "", "", "", "", ""),
            binding.buttonAccountConfirm)
        loginEditText.addTextChangedListener(anyChangeWatcher)
        passwordEditText.addTextChangedListener(anyChangeWatcher)
        emailEditText.addTextChangedListener(anyChangeWatcher)
        fullnameEditText.addTextChangedListener(anyChangeWatcher)
        phoneEditText.addTextChangedListener(anyChangeWatcher)
        countryEditText.addTextChangedListener(anyChangeWatcher)
        cityEditText.addTextChangedListener(anyChangeWatcher)
        districtEditText.addTextChangedListener(anyChangeWatcher)
        streetEditText.addTextChangedListener(anyChangeWatcher)
        houseNumberEditText.addTextChangedListener(anyChangeWatcher)
        flatNumberEditText.addTextChangedListener(anyChangeWatcher)
        postCodeEditText.addTextChangedListener(anyChangeWatcher)

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