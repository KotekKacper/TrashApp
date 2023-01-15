package com.example.trashapp.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trashapp.R
import com.example.trashapp.databinding.FragmentHomeBinding
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import com.example.trashapp.AddTrashActivity
import com.example.trashapp.DBUtils


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var map : MapView
    private lateinit var mLocationOverlay : MyLocationNewOverlay


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Fragment setup
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Map setup
        getInstance().load(activity, PreferenceManager.getDefaultSharedPreferences(activity))
        map = binding.root.findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)

        // Add button setup
        binding.fab.setOnClickListener { view ->
            val intent = Intent(context, AddTrashActivity::class.java)
            intent.putExtra("startPosition", (map.mapCenter as GeoPoint).toDoubleString())
            startActivity(intent)
        }

        // Add icons to map
        var items = DBUtils.getAllActiveFromDB()
        var collectedItems = ArrayList<String>()
        addIconsToMap(items, collectedItems)

        // Enable pinch to zoom
        map.setMultiTouchControls(true)

        // Starting position setup
        map.controller.setZoom(18)
        map.setExpectedCenter(GeoPoint(52.40440, 16.94950))

//        // Enable map rotation
//        enableMapRotation()

//        // Get current user loaction (not working)
//        getUserCurrentLocation()

        return root
    }

    private fun getUserCurrentLocation() {
        mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), map);
        this.mLocationOverlay.enableMyLocation();
        map.overlays.add(mLocationOverlay)
    }

    private fun enableMapRotation() {
        val rotationGestureOverlay = RotationGestureOverlay(map)
        rotationGestureOverlay.isEnabled
        map.overlays.add(rotationGestureOverlay)
    }

    private fun addIconsToMap(items: ArrayList<OverlayItem>, collectedItems: ArrayList<String>) {
        var overlay = ItemizedOverlayWithFocus<OverlayItem>(
            items,
            object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                    return if (collectedItems.indexOf(item.uid) == -1){
                        Toast.makeText(context, "Hold to collect", Toast.LENGTH_SHORT).show()
                        true
                    } else{
                        Toast.makeText(context, "Trash already collected", Toast.LENGTH_SHORT).show()
                        false
                    }

                }

                override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                    if (collectedItems.indexOf(item.uid) == -1){
                        item.setMarker(resources.getDrawable(R.drawable.ic_menu_trash))
                        DBUtils.delFromDB(item);
                        collectedItems.add(item.uid)
                        Toast.makeText(context, "Item marked as collected", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "Trash already collected", Toast.LENGTH_SHORT)
                            .show()
                    }
                    return false
                }
            }, context
        )
        overlay.setFocusItemsOnTap(true);
        overlay.setMarkerBackgroundColor(Color.CYAN)
        map.overlays.add(overlay);
    }



    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}