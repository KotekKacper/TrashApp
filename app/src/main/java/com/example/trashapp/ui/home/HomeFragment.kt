package com.example.trashapp.ui.home

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trashapp.R
import com.example.trashapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


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

        // Add icons to map
        var items = ArrayList<OverlayItem>()
        items.add(OverlayItem("Trash", "Desc1", GeoPoint(52.40339, 16.95057)))
        items.add(OverlayItem("Trash2", "Desc2", GeoPoint(52.40349, 16.95057)))
        addIconsToMap(items)

        // Enable pinch to zoom
        map.setMultiTouchControls(true)

        // Starting position setup
        map.controller.setZoom(17)
        map.setExpectedCenter(GeoPoint(52.40458, 16.94948))

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

    private fun addIconsToMap(items: ArrayList<OverlayItem>) {
        var overlay = ItemizedOverlayWithFocus<OverlayItem>(
            items,
            object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                    Toast.makeText(context, "Hold to collect", Toast.LENGTH_SHORT).show()
                    return true
                }

                override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                    item.setMarker(resources.getDrawable(R.drawable.ic_menu_trash))
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