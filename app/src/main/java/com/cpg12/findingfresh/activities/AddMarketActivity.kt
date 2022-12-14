package com.cpg12.findingfresh.activities


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.database.Markets
import com.cpg12.findingfresh.databinding.ActivityAddMarketBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*


class AddMarketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMarketBinding

    /** Firebase object initialization **/
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    /** pointer to location of image**/
    private lateinit var imageUri: Uri

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** object for firebase authentication**/
        auth  = FirebaseAuth.getInstance()

        /** References the unique id of the current user that is logged in**/
        val uid = auth.currentUser?.uid

        /** References the node to which market data is stored**/
        databaseReference = FirebaseDatabase.getInstance().getReference("Markets")

        /** Upload image button chooses 1 picture from your gallery**/
        binding.marketUploadImageBtn.setOnClickListener {
            selectImage()
        }

        // TODO: create a function to house the repetitive portion
        // Clicking on time input launches a time picker
        val marketStartTimeET = findViewById<EditText>(R.id.marketOpenTimeET)

        marketStartTimeET.setOnClickListener {
            val timePicker: MaterialTimePicker = MaterialTimePicker
                .Builder()
                .setTitleText("Select market start time")
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build()
            timePicker.show(supportFragmentManager, "TIME_PICKER")

            timePicker.addOnPositiveButtonClickListener {
                val selectedTime = Calendar.getInstance()
                selectedTime[0, 0, 0, timePicker.hour, timePicker.minute] = 0

                val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedTime.time)

                marketStartTimeET.setText(formattedTime)
            }
        }

        val marketCloseTimeET = findViewById<EditText>(R.id.marketCloseTimeET)

        marketCloseTimeET.setOnClickListener {
            val timePicker: MaterialTimePicker = MaterialTimePicker
                .Builder()
                .setTitleText("Select market close time")
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build()
            timePicker.show(supportFragmentManager, "TIME_PICKER")

            timePicker.addOnPositiveButtonClickListener {
                val selectedTime = Calendar.getInstance()
                selectedTime[0, 0, 0, timePicker.hour, timePicker.minute] = 0

                val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedTime.time)

                marketCloseTimeET.setText(formattedTime)
            }
        }


        /** Cancel button intents into main listing**/
        binding.marketCancelBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.startActivity(intent)
        }


        /** Submit button saves market to firebase**/
        binding.marketSubmitBtn.setOnClickListener {

            showProgressBar()

            val marketName = binding.marketNameET.text.toString()
            val marketLocation = binding.marketLocationET.text.toString()
            val marketEmail = binding.marketEmailET.text.toString()
            val marketDescription = binding.marketDescriptionET.text.toString()
            val marketOpenTime = binding.marketOpenTimeET.text.toString()
            val marketCloseTime = binding.marketCloseTimeET.text.toString()
            val marketSunday = binding.sunday.isChecked
            val marketMonday = binding.monday.isChecked
            val marketTuesday = binding.tuesday.isChecked
            val marketWednesday = binding.wednesday.isChecked
            val marketThursday = binding.thursday.isChecked
            val marketFriday = binding.friday.isChecked
            val marketSaturday = binding.saturday.isChecked

            /** create instance of market class**/
            val newMarket = Markets(
                marketName,
                marketLocation,
                marketEmail,
                marketDescription,
                marketOpenTime,
                marketCloseTime,
                marketSunday,
                marketMonday,
                marketTuesday,
                marketWednesday,
                marketThursday,
                marketFriday,
                marketSaturday,
            )

            // Creates a document in Firestore with the given information
            val firestore = FirebaseFirestore.getInstance()
            val docRef = firestore.collection("farms")
            docRef.add(newMarket)
                .addOnSuccessListener { documentReference ->
                    uploadImage(documentReference.id)
                    hideProgressBar()
                    Toast.makeText(this@AddMarketActivity,"$marketName Listing Successful",Toast.LENGTH_LONG).show()
                    /**Go back to main screen**/
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    this.startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.w("Finding Fresh Error", "Error adding document", e)
                }

            /** temporary because no authentication implemented yet**/
/*            if (uid != null){
               databaseReference.child(uid).setValue(markets).addOnCompleteListener {
                    if (it.isSuccessful){
                        uploadPic()
                        hideProgressBar()
                        val intent = Intent(this, MainActivity::class.java)
                        this.startActivity(intent)
                    }else{
                        hideProgressBar()
                        Toast.makeText(this@AddMarketActivity,"Failed to list your market",Toast.LENGTH_SHORT).show()
                    }
                }
            }*/

        }


    }

    /** Function intents into image gallery**/
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }


    /** Function for when image selected from gallery
    Shows the image in the imageView
    Assigns pointer imageUri the image **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){

            imageUri = data?.data!!
            binding.marketPictureIV.setImageURI(imageUri)

        }
    }

    private fun uploadImage(marketDocumentId: String) {
        /** References where to store the image**/
        storageReference = FirebaseStorage.getInstance().getReference("Markets/$marketDocumentId")

        /** putFile() stores in firebase **/
        storageReference.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(this@AddMarketActivity,"Image upload Successful",Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            hideProgressBar()
            Toast.makeText(this@AddMarketActivity,"Image upload Failed",Toast.LENGTH_LONG).show()
        }
    }

    private fun showProgressBar(){
        dialog = Dialog(this@AddMarketActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar(){
        dialog.dismiss()
    }
}