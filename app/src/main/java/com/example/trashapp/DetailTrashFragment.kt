package com.example.trashapp

import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
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

        binding.buttonLoadImg.setOnClickListener {
            chosen_imgs.clear()
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMG)
        }

        binding.buttonSecond.setOnClickListener {
            DBUtils.addToDB(GeoPoint(givenPosition[0].toDouble(), givenPosition[1].toDouble()), chosen_imgs)
            Toast.makeText(context, "Trash reported", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMG) {
            val imageUri: Uri? = data?.getData()
            binding.buttonLoadImg.setText(imageUri.toString())
        }

        if (requestCode === PICK_IMG && resultCode === RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                val mClipData: ClipData = data.getClipData()!!
                val cout: Int = data.getClipData()!!.getItemCount()
                for (i in 0 until cout) {
                    // adding imageuri in array
                    val imageurl: Uri = data.getClipData()!!.getItemAt(i).getUri()
                    chosen_imgs.add(imageurl)
                    binding.buttonLoadImg.setText("Photos chosen")
                }
            } else {
                val imageurl: Uri = data.getData()!!
                chosen_imgs.add(imageurl)
            }
        } else {
            // show this if no image is selected
            Toast.makeText(context, "You haven't picked any image", Toast.LENGTH_LONG).show()
            binding.buttonLoadImg.setText("IMAGE FROM FILES")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
