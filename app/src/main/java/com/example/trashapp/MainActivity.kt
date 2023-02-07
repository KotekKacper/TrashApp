package com.example.trashapp

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.trashapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), SortButtonCallback {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    lateinit var sortButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        sortButton = binding.appBarMain.toolbar.findViewById<ImageButton>(R.id.sortButton)
        sortButton.setOnClickListener {
            // Call the function in the current fragment using the callback interface
            supportFragmentManager.fragments.lastOrNull()?.let { fragment ->
                (fragment as? SortButtonCallback)?.onSortButtonClicked()
            }
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_map, R.id.nav_reports, R.id.nav_groups,
                R.id.nav_collecting_points, R.id.nav_users,
                R.id.nav_companies, R.id.nav_vehicles,
                R.id.nav_workers, R.id.nav_roles, R.id.nav_account
            ), drawerLayout
        )

        val role = getSharedPreferences("credentials", Context.MODE_PRIVATE)
            .getString("role", "")?.split(",")?.sorted()?.get(0)
        val menu = navView.menu
        when (role){
            "ADMIN" -> {
                menu.findItem(R.id.nav_map).isVisible = true
                menu.findItem(R.id.nav_reports).isVisible = true
                menu.findItem(R.id.nav_reports).title = "All reports"
                menu.findItem(R.id.nav_groups).isVisible = true
                menu.findItem(R.id.nav_reports).title = "All reports"
                menu.findItem(R.id.nav_collecting_points).isVisible = true
                menu.findItem(R.id.nav_users).isVisible = true
                menu.findItem(R.id.nav_companies).isVisible = true
                menu.findItem(R.id.nav_vehicles).isVisible = true
                menu.findItem(R.id.nav_workers).isVisible = true
                menu.findItem(R.id.nav_roles).isVisible = true
                menu.findItem(R.id.nav_account).isVisible = true
            }
            "USER" -> {
                menu.findItem(R.id.nav_map).isVisible = true
                menu.findItem(R.id.nav_reports).isVisible = true
                menu.findItem(R.id.nav_reports).title = "Your reports"
                menu.findItem(R.id.nav_groups).isVisible = true
                menu.findItem(R.id.nav_groups).title = "Your groups"
                menu.findItem(R.id.nav_collecting_points).isVisible = true
                menu.findItem(R.id.nav_users).isVisible = false
                menu.findItem(R.id.nav_companies).isVisible = false
                menu.findItem(R.id.nav_vehicles).isVisible = false
                menu.findItem(R.id.nav_workers).isVisible = false
                menu.findItem(R.id.nav_roles).isVisible = false
                menu.findItem(R.id.nav_account).isVisible = true
            }
            else -> {
                menu.findItem(R.id.nav_map).isVisible = true
                menu.findItem(R.id.nav_reports).isVisible = true
                menu.findItem(R.id.nav_reports).title = "Your reports"
                menu.findItem(R.id.nav_groups).isVisible = true
                menu.findItem(R.id.nav_groups).title = "Your groups"
                menu.findItem(R.id.nav_collecting_points).isVisible = true
                menu.findItem(R.id.nav_users).isVisible = false
                menu.findItem(R.id.nav_companies).isVisible = false
                menu.findItem(R.id.nav_vehicles).isVisible = false
                menu.findItem(R.id.nav_workers).isVisible = false
                menu.findItem(R.id.nav_roles).isVisible = false
                menu.findItem(R.id.nav_account).isVisible = true
            }
        }


        val headerView: View = binding.navView.getHeaderView(0)
        val navUsername = headerView.findViewById<View>(R.id.textViewLoginNav) as TextView
        navUsername.text = getSharedPreferences("credentials", Context.MODE_PRIVATE).getString("login", "")
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onSortButtonClicked() {
        TODO("Implemented in fragments")
    }
}