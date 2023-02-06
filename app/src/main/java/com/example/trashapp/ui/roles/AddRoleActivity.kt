package com.example.trashapp.ui.roles

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.classes.Role
import com.example.trashapp.classes.Worker
import com.example.trashapp.watchers.*

class AddRoleActivity : AppCompatActivity() {
    private var adding = true
    private var roleName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_role)

        val tx1 = this.findViewById<TextView>(R.id.textRoleName)
        val s1 = SpannableString(tx1.text.toString()+" *")
        s1.setSpan(
            ForegroundColorSpan(Color.RED),
            s1.length-1, s1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx1.text = s1


        val extras = intent.extras;
        if (extras != null) {
            adding = false
            try {
                val roleNameEditText = this.findViewById<EditText>(R.id.editTextRoleName)
                roleNameEditText.text = SpannableStringBuilder(extras.getString("roleName"))
                roleName = extras.getString("roleName")!!

            } catch (e: Exception) {
                Log.e("IntentExtras", e.toString())
            }
        }

        val roleNameEditText = this.findViewById<EditText>(R.id.editTextRoleName)
        roleNameEditText.addTextChangedListener(SingleRoleWatcher(roleNameEditText))

        val applyButton = findViewById<Button>(R.id.buttonRoleConfirm)
        applyButton.setOnClickListener{
            if (roleNameEditText.text.isNotEmpty() && roleNameEditText.error == null){
                DBUtils.addRole(this, adding,
                    Role(
                        roleNameEditText.text.toString()
                    ), roleName
                )
            } else{
                Toast.makeText(this, "Invalid role data", Toast.LENGTH_SHORT).show()
            }
        }
        val cancelButton = findViewById<Button>(R.id.buttonRoleCancel)
        cancelButton.setOnClickListener {
            finish()
        }

        val deleteButton = findViewById<Button>(R.id.buttonRoleDelete)
        if (extras != null) {
            deleteButton.setOnClickListener {
                DBUtils.deleteRole(this, roleName)
                finish()
            }
        }else{
            deleteButton.isVisible = false
        }



    }
}