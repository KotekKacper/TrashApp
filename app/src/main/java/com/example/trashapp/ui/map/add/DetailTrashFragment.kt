package com.example.trashapp.ui.map.add

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.trashapp.DBUtils
import com.example.trashapp.DatabaseHelper
import com.example.trashapp.MainActivity
import com.example.trashapp.databinding.FragmentDetailTrashBinding
import org.osmdroid.util.GeoPoint


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailTrashFragment : Fragment() {

    private var _binding: FragmentDetailTrashBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val PICK_IMG = 100;
    val chosen_imgs : ArrayList<Uri> = ArrayList<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailTrashBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getting the position of the trash given by the user
        val act: AddTrashActivity? = activity as AddTrashActivity?
        val givenPosition: List<String> = act!!.getStartPosition().split(",")

        binding.textviewLocation.text = givenPosition[0]+", "+givenPosition[1]

        val sizes = arrayOf("small", "medium", "big")
        binding.dropdownSize.adapter = context?.let {
            ArrayAdapter<String>(
                it, androidx.transition.R.layout.support_simple_spinner_dropdown_item, sizes)
        }

        binding.buttonLoadImg.setOnClickListener {
            chosen_imgs.clear()
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMG)
        }

        binding.buttonConfirm.setOnClickListener {
            val dbHelper = context?.let { it1 -> DatabaseHelper(it1) }
            DBUtils.addTrashToDB(
                dbHelper!!.writableDatabase,
                GeoPoint(givenPosition[0].toDouble(), givenPosition[1].toDouble()),
                chosen_imgs,
                binding.dropdownSize.selectedItem.toString()
            )
            Toast.makeText(context, "Trash reported", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, MainActivity::class.java))
            activity?.finishAffinity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === PICK_IMG && resultCode === RESULT_OK && null != data) {
            // Get the Images from data
            getImagesFromData(data)
        } else {
            // show this if no image is selected
            Toast.makeText(context, "You haven't picked any image", Toast.LENGTH_LONG).show()
            binding.buttonLoadImg.setText("IMAGE FROM FILES")
        }
    }

    private fun getImagesFromData(data: Intent) {
        if (data.getClipData() != null) {
            val mClipData: ClipData = data.getClipData()!!
            val cout: Int = data.getClipData()!!.getItemCount()
            for (i in 0 until cout) {
                // adding imageuri in array
                val imageurl: Uri = data.getClipData()!!.getItemAt(i).getUri()
                chosen_imgs.add(imageurl)
                if (chosen_imgs.size == 1){
                    binding.buttonLoadImg.setText(chosen_imgs.size.toString()+" image chosen")
                }
                else{
                    binding.buttonLoadImg.setText(chosen_imgs.size.toString()+" images chosen")
                }
            }
        } else {
            val imageurl: Uri = data.getData()!!
            chosen_imgs.add(imageurl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
