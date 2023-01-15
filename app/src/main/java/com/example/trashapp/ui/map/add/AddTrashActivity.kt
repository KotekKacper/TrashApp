package com.example.trashapp.ui.map.add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.trashapp.R
import com.example.trashapp.databinding.ActivityAddTrashBinding

class AddTrashActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddTrashBinding

    private lateinit var startPosition: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startPosition = intent.getStringExtra("startPosition").toString()

        binding = ActivityAddTrashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_point_on_map)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_point_on_map)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun getStartPosition(): String{
        return this.startPosition
    }

    fun setStartPosition(startPosition: String){
        this.startPosition = startPosition
    }
}