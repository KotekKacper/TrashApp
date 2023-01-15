package com.example.trashapp

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trashapp.databinding.FragmentPointOnMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PointOnMapFragment : Fragment() {

    private var _binding: FragmentPointOnMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var map : MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPointOnMapBinding.inflate(inflater, container, false)

        val act: AddTrashActivity? = activity as AddTrashActivity?
        val startPosition: List<String> = act!!.getStartPosition().split(",")

        Configuration.getInstance().load(activity, PreferenceManager.getDefaultSharedPreferences(activity))
        map = binding.root.findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)

        map.controller.setZoom(18)
        map.setExpectedCenter(GeoPoint(startPosition[0].toDouble(), startPosition[1].toDouble()))


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonChooseLoc.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onPause() {
        super.onPause()

        val act: AddTrashActivity? = activity as AddTrashActivity?
        act!!.setStartPosition((map.mapCenter as GeoPoint).toDoubleString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}